package com.mukijidev.board.user;



public record UserResponse(Long id, String username) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername());
    }

}