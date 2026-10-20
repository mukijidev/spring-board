package com.mukijidev.board.comment;


import java.time.LocalDateTime;

public record CommentResponse(Long id, Long postId, String content, LocalDateTime createdAt) {

    public static CommentResponse from(Comment comment){
        return new CommentResponse(
             comment.getId(),
             comment.getPost().getId(),
             comment.getContent(),
             comment.getCreatedAt()
        );
    }

}