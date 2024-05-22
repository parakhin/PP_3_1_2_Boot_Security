package ru.kata.spring.boot_security.demo.dto;

import java.util.List;

public class AuthDTO {
    private boolean isAuthenticated = false;
    private String login;

    private List<String> roles;

    public AuthDTO() {

    }

    public AuthDTO(boolean isAuthenticated, String login, List<String> roles) {
        this.isAuthenticated = isAuthenticated;
        this.login = login;
        this.roles = roles;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
