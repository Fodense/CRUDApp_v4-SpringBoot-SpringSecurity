package by.brel.springbootCRUD.controller;

import by.brel.springbootCRUD.model.Role;
import by.brel.springbootCRUD.model.Status;
import by.brel.springbootCRUD.model.User;
import by.brel.springbootCRUD.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('developers:read')")
    public String findAll(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);

        return "userList";
    }

    @GetMapping("/userCreate")
    @PreAuthorize("hasAuthority('developers:write')")
    public String createUserForm(User user) {
        return "userCreate";
    }

    @PostMapping("/userCreate")
    public String createUser(User user) {
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.saveUser(user);

        return "redirect:/api/users";
    }

    @GetMapping("userDelete/{id}")
    @PreAuthorize("hasAuthority('developers:write')")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);

        return "redirect:/api/users";
    }

    @GetMapping("/userUpdate/{id}")
    @PreAuthorize("hasAuthority('developers:write')")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute(user);

        return "/userUpdate";
    }

    @PostMapping("/userUpdate")
    public String updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.saveUser(user);

        return "redirect:/api/users";
    }
}
