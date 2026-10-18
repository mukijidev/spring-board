package com.mukijidev.board.post;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public PostResponse create(PostCreateRequest request) {
        Post post = postRepository.save(new Post(request.title(), request.content()));
        return PostResponse.from(post);
    }

    public PostResponse get(Long id) {
        return PostResponse.from(findPost(id));
    }


    public Page<PostResponse> list(String keyword, Pageable pageable) {
        Page<Post> posts;

        if(keyword == null || keyword.isBlank()) {
            posts = postRepository.findAll(pageable);
        } else {
            posts = postRepository.findByTitleContaining(keyword, pageable);
        }

        return posts.map(PostResponse::from);
    }

    @Transactional
    public PostResponse update(Long id, PostUpdateRequest request) {
        Post post = findPost(id);
        post.update(request.title(), request.content());
        return PostResponse.from(post);
    }

    @Transactional
    public void delete(Long id) {
        postRepository.delete(findPost(id));
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow( () -> new PostNotFoundException(id));
    }

}
