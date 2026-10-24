package com.mukijidev.board.common;


public class NotAuthorException extends RuntimeException {

    public NotAuthorException() {
        super("You are not the author.");
    }
}