package com.mukijidev.board.post;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mukijidev.board.comment.CommentCountPerPost;
import com.mukijidev.board.comment.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public PostResponse create(PostCreateRequest request) {
        Post post = postRepository.save(new Post(request.title(), request.content()));
        return PostResponse.from(post, 0);
    }

    public PostResponse get(Long id) {
        Post post = findPost(id);
        long commentCount = commentRepository.countByPostId(id);
        return PostResponse.from(post, commentCount);
    }


    public Page<PostResponse> list(String keyword, Pageable pageable) {
        Page<Post> posts;

        if(keyword == null || keyword.isBlank()) {
            posts = postRepository.findAll(pageable);
        } else {
            posts = postRepository.findByTitleContaining(keyword, pageable);
        }

        //return posts.map(post ->PostResponse.from(post));
        Map<Long, Long> commentCounts = countComments(posts.getContent());
        return posts.map(post->PostResponse.from(post, commentCounts.getOrDefault(post.getId(), 0L)));
    }

    private Map<Long, Long> countComments(List<Post> posts) {
        if(posts.isEmpty()) {
            return Map.of();
        }

        List<Long> postIds = posts.stream().map(Post::getId).toList();
        return commentRepository.countByPostIds(postIds).stream()
                .collect(Collectors.toMap(CommentCountPerPost::postId, CommentCountPerPost::count));

    }

    @Transactional
    public PostResponse update(Long id, PostUpdateRequest request) {
        Post post = findPost(id);
        long commentCount = commentRepository.countByPostId(id);
        post.update(request.title(), request.content());
        return PostResponse.from(post, commentCount);
    }

    @Transactional
    public void delete(Long id) {
        Post post = findPost(id);
        commentRepository.deleteByPostId(id);
        postRepository.delete(post);
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow( () -> new PostNotFoundException(id));
    }

}
