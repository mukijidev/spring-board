package com.mukijidev.board.comment;


import com.mukijidev.board.common.NotAuthorException;
import com.mukijidev.board.post.Post;
import com.mukijidev.board.post.PostNotFoundException;
import com.mukijidev.board.post.PostRepository;
import com.mukijidev.board.user.User;
import com.mukijidev.board.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CommentResponse create(Long postId, CommentCreateRequest request, Long authorId) {
        User author = userRepository.getReferenceById(authorId);

        Post post = postRepository.findById(postId)
                .orElseThrow(()->new PostNotFoundException(postId));

        Comment comment = commentRepository.save(new Comment(post, request.content(), author));
        return CommentResponse.from(comment);
    }

    public Page<CommentResponse> list(Long postId, Pageable pageable) {
        if(!postRepository.existsById(postId)) {
            throw new PostNotFoundException(postId);
        }

        return commentRepository.findByPostId(postId, pageable).map(CommentResponse::from);
    }

    @Transactional
    public void delete(Long postId, Long commentId, Long loginId) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                        .orElseThrow(()->new CommentNotFoundException(commentId));

        if(!comment.getAuthor().getId().equals(loginId)) {
            throw new NotAuthorException();
        }

        commentRepository.delete(comment);
    }

    public Page<RecentCommentResponse> recent(Pageable pageable) {
        return commentRepository.findRecent(pageable).map(RecentCommentResponse::from);
    }
}
