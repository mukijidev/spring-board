package com.mukijidev.board.post;


import com.mukijidev.board.user.User;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    void updateChangeTitleAndContent() {
        Post post = new Post("Old title", "Old content", new User("alice", "encoded"));

        post.update("New title", "New content");

        assertThat(post.getTitle()).isEqualTo("New title");
        assertThat(post.getContent()).isEqualTo("New content");
    }

    @Test
    void updateRefreshesUpdatedAt() {
        Post post = new Post("Title", "Content", new User("alice", "encoded"));
        post.update("New title", "New content");
        assertThat(post.getUpdatedAt()).isAfterOrEqualTo(post.getCreatedAt());

    }
}