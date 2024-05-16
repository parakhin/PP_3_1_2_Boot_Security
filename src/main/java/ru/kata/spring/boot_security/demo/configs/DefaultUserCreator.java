package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.List;

@Configuration
public class DefaultUserCreator {
    private final RoleService roleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultUserCreator(RoleService roleService, UserService userService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

        this.run();
    }

    public void run() {
        Role adminRole = new Role("ROLE_ADMIN");
        roleService.save(adminRole);

        Role userRole = new Role("ROLE_USER");
        roleService.save(userRole);

        User admin = new User("admin", "admin@rr.rr", passwordEncoder.encode("123456"), 200);
        admin.setRoles(List.of(adminRole, userRole));
        User user = new User("user", "user@rr.rr", passwordEncoder.encode("123456"), -1000);
        user.setRoles(List.of(userRole));

        userService.save(admin);
        userService.save(user);
    }
}
