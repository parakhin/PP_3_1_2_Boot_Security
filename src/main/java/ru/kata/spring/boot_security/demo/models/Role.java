package ru.kata.spring.boot_security.demo.models;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;



    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @ManyToMany (mappedBy = "roles")
    private List<User> users;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public String getAuthority() {
        return title;
    }
}