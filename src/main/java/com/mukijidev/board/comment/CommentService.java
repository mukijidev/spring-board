package com.mukijidev.board.comment;


import com.mukijidev.board.post.Post;
import com.mukijidev.board.post.PostNotFoundException;
import com.mukijidev.board.post.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public CommentResponse create(Long postId, CommentCreateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new PostNotFoundException(postId));

        Comment comment = commentRepository.save(new Comment(post, request.content()));
        return CommentResponse.from(comment);
    }

    public Page<CommentResponse> list(Long postId, Pageable pageable) {
        if(!postRepository.existsById(postId)) {
            throw new PostNotFoundException(postId);
        }

        return commentRepository.findByPostId(postId, pageable).map(CommentResponse::from);
    }

    @Transactional
    public void delete(Long postId, Long commentId) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                        .orElseThrow(()->new CommentNotFoundException(commentId));
        commentRepository.delete(comment);
    }


}
