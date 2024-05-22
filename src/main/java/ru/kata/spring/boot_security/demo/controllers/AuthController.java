package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.AuthDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @GetMapping
    public AuthDTO index () {
        AuthDTO authDTO = new AuthDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            return authDTO;
        }

        authDTO.setAuthenticated(true);
        User user = (User) auth.getPrincipal();
        authDTO.setLogin(user.getEmail());
        authDTO.setRoles(user.getRoles().stream().map(Role::getTitle).collect(Collectors.toList()));
        return authDTO;
    }
}
