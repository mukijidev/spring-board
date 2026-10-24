package com.mukijidev.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.mukijidev.board.comment.CommentCreateRequest;
import com.mukijidev.board.comment.CommentService;
import com.mukijidev.board.post.PostCreateRequest;
import com.mukijidev.board.post.PostResponse;
import com.mukijidev.board.post.PostService;
import com.mukijidev.board.user.User;
import com.mukijidev.board.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

@SpringBootTest
@Testcontainers // @Container가 붙은 컨테이너를 띄우고 끝나면 지움
class MySqlContainerTest {

    // @ServiceConnection: Spring의 db url, account를 이 컨테이너 것으로
    @Container
    @ServiceConnection
    static MySQLContainer mysql = new MySQLContainer("mysql:8.4");

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserRepository userRepository;

    @Test
    void postListShowsCommentCountOnMySql() {
        Long authorId = userRepository.save(new User("alice", "encoded")).getId();
        PostResponse post = postService.create(new PostCreateRequest("Hello", "First post"), authorId);
        commentService.create(post.id(), new CommentCreateRequest("Nice"), authorId);
        commentService.create(post.id(), new CommentCreateRequest("Thanks"), authorId);

        Page<PostResponse> posts = postService.list(null, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")));

        assertThat(posts.getContent())
                .extracting(PostResponse::title, PostResponse::commentCount)
                .contains(tuple("Hello", 2L));
    }
}