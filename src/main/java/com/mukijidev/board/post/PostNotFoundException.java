package com.mukijidev.board.post;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Long id) {
        super("Post not found: " + id);
    }
}