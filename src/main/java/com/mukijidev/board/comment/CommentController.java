package com.mukijidev.board.comment;

import com.mukijidev.board.common.PageResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {this.commentService = commentService;}


    @GetMapping("/posts/{postId}/comments")
    public PageResponse<CommentResponse> list(
            @PathVariable Long postId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {

        return PageResponse.from(commentService.list(postId, pageable));
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> create(
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest request) {
        System.out.println("[REST} create coomment");
        CommentResponse created = commentService.create(postId, request);
        return ResponseEntity.created(URI.create("/posts/" + postId +"/comments/" + created.id())).body(created);
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long postId, @PathVariable Long id) {
        commentService.delete(postId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comments/recent")
    public PageResponse<RecentCommentResponse> recent(
            @PageableDefault(size =5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {


            return PageResponse.from(commentService.recent(pageable));
        }

}
