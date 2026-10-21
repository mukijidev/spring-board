package com.mukijidev.board.comment;


import com.mukijidev.board.post.Post;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    TestEntityManager em;

    @Autowired
    CommentRepository commentRepository;

    @Test
    void countsCommentsPerPost() {
        //list [post 1, post 2] ->[ comment count 1, comment count 2]

        Post post1 = em.persist(new Post("Post 1", "Content"));
        Post post2 = em.persist(new Post("Post 2", "Content"));
        Post post3 = em.persist(new Post("Post 3", "Content"));

        em.persist(new Comment(post1, "a"));
        em.persist(new Comment(post1, "b"));
        em.persist(new Comment(post3, "c"));

        List<CommentCountPerPost> counts =
                commentRepository.countByPostIds(List.of(post1.getId(), post2.getId()));

        assertThat(counts).extracting(CommentCountPerPost::postId, CommentCountPerPost::count)
                .containsExactly(tuple(post1.getId(), 2L));
    }

    @Test
    void findRecentFetchesPost() {
        //fetch join
        Post post = em.persist(new Post("Title", "Content"));
        em.persist(new Comment(post, "Hello"));
        em.flush();
        em.clear();

        Comment comment = commentRepository.findRecent(PageRequest.of(0, 5)).getContent().get(0);
        assertThat(Hibernate.isInitialized(comment.getPost())).isTrue();
        assertThat(comment.getPost().getTitle()).isEqualTo("Title");
    }

    @Test
    void findAllKeepsPostLazy() {
        // findAll -> proxy post
        Post post = em.persist(new Post("Title", "Content"));
        em.persist(new Comment(post, "Hello"));
        em.flush();
        em.clear();

        Comment comment = commentRepository.findAll().get(0);
        assertThat(Hibernate.isInitialized(comment.getPost())).isFalse();
    }

    @Test
    void deleteByPostIdKeepsOtherPosts() {
        Post post1 = em.persist(new Post("Post 1", "Content"));
        Post post2 = em.persist(new Post("Post 2", "Content"));

        em.persist(new Comment(post1, "a"));
        em.persist(new Comment(post1, "b"));
        em.persist(new Comment(post2, "c"));
        em.flush();

        commentRepository.deleteByPostId(post1.getId());

        assertThat(commentRepository.countByPostId(post1.getId())).isZero();
        assertThat(commentRepository.countByPostId(post2.getId())).isEqualTo(1);
    }

}
