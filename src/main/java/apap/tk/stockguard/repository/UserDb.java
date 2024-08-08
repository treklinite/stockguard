package apap.tk.stockguard.repository;

import apap.tk.stockguard.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDb extends JpaRepository<UserModel, Integer> {
    UserModel findByUsername(String username);
    UserModel findByEmail(String email);
}
