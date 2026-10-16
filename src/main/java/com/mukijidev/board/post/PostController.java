package com.mukijidev.board.post;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostResponse> list() {
        return postService.list();
    }

    @GetMapping("/{id}")
    public PostResponse get(@PathVariable Long id) {
        return postService.get(id);
    }

    @PostMapping
    public ResponseEntity<PostResponse> create(@Valid @RequestBody PostCreateRequest request) {
        PostResponse created = postService.create(request);
        return ResponseEntity.created(URI.create("/posts" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public PostResponse update(@Valid @PathVariable Long id, @RequestBody PostUpdateRequest request) {
        return postService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
