package com.mukijidev.board.comment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateRequest(
    @NotBlank(message = "content is required")
    @Size(max = 500, message = "content must be 500 characters or less ")
    String content) {
}