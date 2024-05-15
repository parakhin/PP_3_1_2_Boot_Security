package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String index(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setPassword("");
        model.addAttribute("user", user);
        return "/users/userInfo";
    }

    @PostMapping
    public String update (@ModelAttribute("user") User user) {
        User authorizedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User savedUser = userService.findOne(authorizedUser.getId());

        String password = user.getPassword().isEmpty() ? savedUser.getPassword() : passwordEncoder.encode(user.getPassword());

        savedUser.setPassword(password);
        savedUser.setAge(user.getAge());
        savedUser.setName(user.getName());
        savedUser.setAge(user.getAge());

        userService.save(savedUser);
        return "redirect:/user/";
    }
}
