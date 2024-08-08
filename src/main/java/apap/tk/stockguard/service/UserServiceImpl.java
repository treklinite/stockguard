package apap.tk.stockguard.service;

import apap.tk.stockguard.model.UserModel;
import apap.tk.stockguard.repository.UserDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDb userDb;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserModel createUser(UserModel user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDb.save(user);
    }

    @Override
    public UserModel getUserById(Integer id) {
        Optional<UserModel> user = userDb.findById(id);
        return user.orElse(null);
    }

    @Override
    public UserModel updateUser(UserModel user) {
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        // return userDb.save(user);
        UserModel existingUser = userDb.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + user.getId()));

        // Update user details
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());
        // Handle password if necessary

        // Save the updated user
        return userDb.save(existingUser);
    }

    @Override
    public UserModel deleteUser(UserModel user) {
        userDb.delete(user);
        return user;    
    }

    @Override
    public List<UserModel> getAllUsers() {
        return userDb.findAll();
    }

    @Override
    public boolean isUsernameExistExceptCurrent(String username, Integer userId) {
        Optional<UserModel> existingUser = Optional.ofNullable(userDb.findByUsername(username));
        return existingUser.isPresent() && !existingUser.get().getId().equals(userId);
    }

    @Override
    public UserModel getUserByUsername(String username) {
        return userDb.findByUsername(username);
    }
}
