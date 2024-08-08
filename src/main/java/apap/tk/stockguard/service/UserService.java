package apap.tk.stockguard.service;

import apap.tk.stockguard.model.UserModel;

import java.util.List;

public interface UserService {
    UserModel createUser(UserModel user);
    UserModel getUserById(Integer id);
    UserModel updateUser(UserModel user);
    UserModel deleteUser(UserModel user);
    List<UserModel> getAllUsers();
    boolean isUsernameExistExceptCurrent(String username, Integer id);
    UserModel getUserByUsername(String username);
}
