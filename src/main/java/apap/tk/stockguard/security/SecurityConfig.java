package apap.tk.stockguard.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

import apap.tk.stockguard.model.UserModel;
import apap.tk.stockguard.repository.UserDb;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final UserDb userDb;
    // private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    public SecurityConfig(UserDb userDb) {
        this.userDb = userDb;
        // this.customLogoutSuccessHandler = customLogoutSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserModel user = userDb.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            // return org.springframework.security.core.userdetails.User
            //     .withUsername(user.getUsername())
            //     .password(user.getPassword())
            //     .roles(user.getRole().name())
            //     .build();

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

            var userDetails = new User(user.getUsername(), user.getPassword(), true, true, true, true, authorities);

            return userDetails;
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((customizer) ->
                        customizer
                                .requestMatchers("/user/**").hasRole("ADMIN")
                                .requestMatchers("/kategori/**").hasRole("ADMIN")
                                .requestMatchers("/barang/**").hasAnyRole("MANAJER_GUDANG", "MANAJER_TOKO")
                                .requestMatchers("/notifikasi/**").hasAnyRole("MANAJER_GUDANG", "MANAJER_TOKO")
                                .anyRequest().authenticated())
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .defaultSuccessUrl("/", true)
                                .permitAll()
                )
                .logout(logout ->
                    logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();
    }
}
