package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String index(Model model, @ModelAttribute("newUser") User newUser) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("user", (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "/admin/index";
    }

    @GetMapping("/detail")
    public String show(@RequestParam("id") Long id, Model model) {
        User user = userService.findOne(id);
        user.setPassword("");
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAll());
        return "/admin/show";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        String password = user.getPassword().isEmpty()
                ? userService.findOne(user.getId()).getPassword() : passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        userService.update(user.getId(), user);
        redirectAttributes.addAttribute("id", user.getId());
        return "redirect:/admin/";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "/admin/new";
    }

    @PostMapping
    public String create(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/admin/";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin/";
    }

    @ModelAttribute("title")
    public String title() {
        return "Admin panel";
    }
}
