package com.devsuperior.demo.dto;

import com.devsuperior.demo.entities.User;
import jakarta.persistence.Column;

public class UserDTO {

    private Long id;
    private String name;

    public UserDTO() {
    }

    public UserDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserDTO(User entity) {
        id = entity.getId();
        name = entity.getUsername();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
