package com.mukijidev.board.post;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostCreateRequest(
        @NotBlank(message = "title is required")
        @Size(max = 100, message = "title must be 100 characters or less")
        String title,

        @NotBlank(message = "content is required")
        @Size(max = 5000, message ="content must be 5000 chracters or less")
        String content) {
}