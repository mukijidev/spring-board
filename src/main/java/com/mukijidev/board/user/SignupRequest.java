package com.mukijidev.board.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank(message = "username is required")
        @Size(min = 3, max = 10, message = "username must be 3 to 10 characters")
        String username,

        // BCrypt는 72바이트까지만
        @NotBlank(message = "password is required")
        @Size(min = 6, max = 20, message = "password must be 6 to 20 characters")
        String password) {


}




