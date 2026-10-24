package com.mukijidev.board.comment;


import com.mukijidev.board.post.Post;
import com.mukijidev.board.user.User;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

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
@ActiveProfiles("test")
class CommentRepositoryTest {

    @Autowired
    TestEntityManager em;

    @Autowired
    CommentRepository commentRepository;

    @Test
    void countsCommentsPerPost() {
        //list [post 1, post 2] ->[ comment count 1, comment count 2]
        User author = em.persist(new User("alice", "encoded"));

        Post post1 = em.persist(new Post("Post 1", "Content", author));
        Post post2 = em.persist(new Post("Post 2", "Content", author));
        Post post3 = em.persist(new Post("Post 3", "Content", author));
        em.persist(new Comment(post1, "a", author));
        em.persist(new Comment(post1, "b", author));
        em.persist(new Comment(post3, "c", author));

        List<CommentCountPerPost> counts =
                commentRepository.countByPostIds(List.of(post1.getId(), post2.getId()));

        assertThat(counts).extracting(CommentCountPerPost::postId, CommentCountPerPost::count)
                .containsExactly(tuple(post1.getId(), 2L));
    }

    @Test
    void findRecentFetchesPost() {
        //fetch join
        User author = em.persist(new User("alice", "encoded"));

        Post post = em.persist(new Post("Title", "Content", author));
        em.persist(new Comment(post, "Hello", author));
        em.flush();
        em.clear();

        Comment comment = commentRepository.findRecent(PageRequest.of(0, 5)).getContent().get(0);
        assertThat(Hibernate.isInitialized(comment.getPost())).isTrue();
        assertThat(comment.getPost().getTitle()).isEqualTo("Title");
    }

    @Test
    void findAllKeepsPostLazy() {
        // findAll -> proxy post
        User author = em.persist(new User("alice", "encoded"));

        Post post = em.persist(new Post("Title", "Content", author));
        em.persist(new Comment(post, "Hello", author));
        em.flush();
        em.clear();

        Comment comment = commentRepository.findAll().get(0);
        assertThat(Hibernate.isInitialized(comment.getPost())).isFalse();
    }

    @Test
    void deleteByPostIdKeepsOtherPosts() {
        User author = em.persist(new User("alice", "encoded"));
        Post post1 = em.persist(new Post("Post 1", "Content", author));
        Post post2 = em.persist(new Post("Post 2", "Content", author));

        em.persist(new Comment(post1, "a", author));
        em.persist(new Comment(post1, "b", author));
        em.persist(new Comment(post2, "c", author));
        em.flush();

        commentRepository.deleteByPostId(post1.getId());

        assertThat(commentRepository.countByPostId(post1.getId())).isZero();
        assertThat(commentRepository.countByPostId(post2.getId())).isEqualTo(1);
    }

}
