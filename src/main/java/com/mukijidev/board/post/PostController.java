package com.mukijidev.board.post;


import com.mukijidev.board.common.PageResponse;
import com.mukijidev.board.user.LoginUser;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

   @GetMapping
   public PageResponse<PostResponse> list(
           @RequestParam(required = false) String keyword,
           @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable) {
        return PageResponse.from(postService.list(keyword, pageable));
   }

    @GetMapping("/{id}")
    public PostResponse get(@PathVariable Long id) {
        return postService.get(id);
    }

    @PostMapping
    public ResponseEntity<PostResponse> create(@Valid @RequestBody PostCreateRequest request,
                                               @AuthenticationPrincipal LoginUser loginUser) {

        PostResponse created = postService.create(request, loginUser.getId());
        return ResponseEntity.created(URI.create("/posts/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public PostResponse update( @PathVariable Long id,@Valid @RequestBody PostUpdateRequest request,
                                @AuthenticationPrincipal LoginUser loginUser) {
        return postService.update(id, request, loginUser.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal LoginUser loginUser){
        postService.delete(id, loginUser.getId());
        return ResponseEntity.noContent().build();
    }
}
