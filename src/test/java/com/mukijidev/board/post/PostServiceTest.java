package com.mukijidev.board.post;


import com.mukijidev.board.comment.CommentRepository;
import com.mukijidev.board.common.NotAuthorException;
import com.mukijidev.board.user.User;
import com.mukijidev.board.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    PostService postService;

    //테스트용 사용자, id는 테스트에서만 직접 넣는다
    private static User user(Long id, String username) {
        User user = new User(username, "encoded");
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    @Test
    void getThrowsWhenNotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.get(99L))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessage("Post not found: 99");
    }

    @Test
    void getIncludesCommentCount() {
        when(postRepository.findById(1L))
                .thenReturn(Optional.of(new Post("Title", "Content", user(1L, "alice"))));

        when(commentRepository.countByPostId(1L)).thenReturn(2L);

        PostResponse response = postService.get(1L);

        assertThat(response.commentCount()).isEqualTo(2);
        assertThat(response.authorName()).isEqualTo("alice");
    }

    @Test
    void deleteRemovesCommentsFirst() {
        Post post = new Post("Title", "Content", user(1L, "alice"));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.delete(1L, 1L);

        InOrder order = inOrder(commentRepository, postRepository);
        order.verify(commentRepository).deleteByPostId(1L);
        order.verify(postRepository).delete(post);

    }

    @Test
    void deleteThrowsWhenNotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> postService.delete(99L, 1L))
                .isInstanceOf(PostNotFoundException.class);
        verify(commentRepository, never()).deleteByPostId(any());
    }

    @Test
    void deleteThrowsWhenNotAuthor() {
        Post post = new Post("Title", "Content", user(1L, "alice"));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        assertThatThrownBy(()->postService.delete(1L, 2L))
                .isInstanceOf(NotAuthorException.class);

        verify(commentRepository, never()).deleteByPostId(any());
        verify(postRepository, never()).delete(any());
    }

}