package ru.kata.spring.boot_security.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.exceptions.UserBadRequestException;
import ru.kata.spring.boot_security.demo.exceptions.UserNotFoundException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.utils.ExceptionDetail;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    private final RoleService roleService;


    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersController(UserService userService, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<UserDTO> index() {
        return userService.findAll().stream().map(item -> {
            item.setPassword("");
            return modelMapper.map(item, UserDTO.class);
        }).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #email == authentication.principal.username")
    @GetMapping("/detail")
    public UserDTO detail(String email) {
        User user = userService.loadUserByEmail(email).orElseThrow(UserNotFoundException::new);
        user.setPassword("");
        return modelMapper.map(user, UserDTO.class);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserBadRequestException(getValidationMessage(bindingResult));
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userService.save(modelMapper.map(userDTO, User.class));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping
    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserBadRequestException(getValidationMessage(bindingResult));
        }
        User user = userService.loadUserByEmail(userDTO.getEmail()).orElseThrow(UserNotFoundException::new);
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setAge(userDTO.getAge());
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        if (!userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        List<Role> roles = userDTO.getRoles().
                stream().map(roleDTO -> roleService.findOne(roleDTO.getId()))
                .collect(Collectors.toList());
        user.setRoles(roles);

        userService.save(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HttpStatus> delete(@RequestParam(name = "email") String email) {
        userService.delete(userService.loadUserByEmail(email).orElseThrow(UserNotFoundException::new).getId());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    public String getValidationMessage(BindingResult bindingResult) {

        StringBuilder message = new StringBuilder();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }

        return message.toString();
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDetail> exceptionHandler(UserNotFoundException e) {
        ExceptionDetail exceptionDetail = new ExceptionDetail("User not found");
        return new ResponseEntity<>(exceptionDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDetail> exceptionHandler(UserBadRequestException e) {
        ExceptionDetail exceptionDetail = new ExceptionDetail(e.getMessage());
        return new ResponseEntity<>(exceptionDetail, HttpStatus.BAD_REQUEST);
    }
}
