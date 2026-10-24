package com.mukijidev.board.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class LoginUser extends org.springframework.security.core.userdetails.User {

    private final Long id;

    public LoginUser(User user) {
        super(user.getUsername(), user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.id = user.getId();
    }

    public Long getId() {
        return id;
    }
}
