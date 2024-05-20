package ru.kata.spring.boot_security.demo.dto;

public class RoleDTO {

    private int id;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
