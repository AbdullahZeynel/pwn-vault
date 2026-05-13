package com.redteam.vulndb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Entity representing an operator/user in the system.
 * Implements Spring Security authentication principles.
 */
@Entity
@Table(name = "operators")
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Kullanıcı adı boş bırakılamaz")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Şifre boş bırakılamaz")
    @Column(nullable = false)
    private String password;

    public Operator() {
    }

    public Operator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
