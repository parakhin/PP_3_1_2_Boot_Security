package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;

@Controller
@RequestMapping("/admin/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("user", (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "/roles/index";
    }

    @GetMapping("/new")
    public String newRole(@ModelAttribute("role") Role role) {
        return "/roles/new";
    }

    @PostMapping
    public String create(@ModelAttribute("role") Role role) {
        roleService.save(role);
        return "redirect:/admin/roles/";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("role") Role role) {
        roleService.save(role);
        return "redirect:/admin/roles/";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        roleService.deleteById(id);
        return "redirect:/admin/roles/";
    }
}
