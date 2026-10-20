package com.mukijidev.board.comment;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(Long id) {super("Comment not found " + id);}
}