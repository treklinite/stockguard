package apap.tk.stockguard.controller;

import apap.tk.stockguard.model.UserModel;
import apap.tk.stockguard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
// @RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/all")
    public String getAllUsers(Model model) {
        List<UserModel> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("roles", UserModel.Role.values());
        return "view-all-user";
    }

    @GetMapping("/user/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new UserModel());
        model.addAttribute("roles", UserModel.Role.values());
        return "form-create-user";
    }

    @PostMapping("/user/create")
    public String createUserSubmit(@ModelAttribute UserModel user, Model model) {
        try {
            userService.createUser(user);
            model.addAttribute("message", "User berhasil dibuat");
            model.addAttribute("url", "/user/all");
            model.addAttribute("pageTitle", "Semua User");
            return "success-page";
        } catch (Exception e) {
            model.addAttribute("error", "User tidak berhasil dibuat");
            model.addAttribute("url", "/user/all");
            model.addAttribute("pageTitle", "Semua User");
            return "error-page";
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Integer id) {
        UserModel user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/update/{id}")
    public String updateUserForm(
        @PathVariable Integer id,
        Model model
    ) {
        UserModel user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", UserModel.Role.values());
        return "form-update-user";
    }

    @PostMapping("/user/update")
    public String updateUserSubmit(
            @ModelAttribute UserModel user,
            Model model
    ) {
        try {
            if (userService.isUsernameExistExceptCurrent(user.getUsername(), user.getId())) {
                throw new IllegalArgumentException("Username tidak tersedia. Silakan gunakan username lain.");
            }
            userService.updateUser(user);
            model.addAttribute("message", "User berhasil di-update");
            model.addAttribute("url", "/user/all");
            model.addAttribute("pageTitle", "Semua User");
            return "success-page";
        } catch (Exception e) {
            model.addAttribute("error", "Gagal meng-update user ");
            model.addAttribute("url", "/user/all");
            model.addAttribute("pageTitle", "Semua User");
            return "error-page";
        }
    }

    @DeleteMapping("/user/delete/{id}")
    public String deleteUserById(@PathVariable Integer id, Model model) {
        try {
            UserModel user = userService.getUserById(id);
            userService.deleteUser(user);
            model.addAttribute("message", "User berhasil dihapus");
            model.addAttribute("url", "/user/all");
            model.addAttribute("pageTitle", "Semua User");
            return "success-page";
        } catch (Exception e) {
            model.addAttribute("error", "User tidak berhasil dihapus");
            model.addAttribute("url", "/user/all");
            model.addAttribute("pageTitle", "Semua User");
            return "error-page";
        }
    }

}
