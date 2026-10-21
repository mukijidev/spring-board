package com.mukijidev.board.user;


import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected User() {
    }

    public User(String username, String encodedPassword) {
        this.username = username;
        this.password = encodedPassword;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {return id;}
    public String getUsername() {return username;}
    public String getPassword() { return password;}
    public LocalDateTime getCreatedAt() {return createdAt;}
}