package com.mukijidev.board.comment;


import java.time.LocalDateTime;

public record RecentCommentResponse(Long id, Long postId, String postTitle,
                                    String content, LocalDateTime createdAt) {

    public static RecentCommentResponse from(Comment comment) {
        return new RecentCommentResponse(
                comment.getId(),
                comment.getPost().getId(),
                comment.getPost().getTitle(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}