package com.mukijidev.board.post;


import com.mukijidev.board.comment.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    PostService postService;

    @Test
    void getThrowWhenPostDoesNotExist() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.get(99L))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessage("Post not found: 99");
    }

    @Test
    void getReturnsPostWithCommentCount() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(new Post("Title", "Content")));
        when(commentRepository.countByPostId(1L)).thenReturn(2L);

        PostResponse response = postService.get(1L);

        assertThat(response.title()).isEqualTo("Title");
        assertThat(response.commentCount()).isEqualTo(2);
    }

    @Test
    void deleteRemovesCommentsBeforePost() {
        Post post = new Post("Title", "Content");
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.delete(1L);

        InOrder order = inOrder(commentRepository, postRepository);
        order.verify(commentRepository).deleteByPostId(1L);
        order.verify(postRepository).delete(post);
    }

    @Test
    void deleteDoesNothingWhenPostDoesNotExist() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.delete(99L))
                .isInstanceOf(PostNotFoundException.class);
        verify(commentRepository, never()).deleteByPostId(any());
    }


}