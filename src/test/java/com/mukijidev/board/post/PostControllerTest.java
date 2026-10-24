package com.mukijidev.board.post;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mukijidev.board.common.NotAuthorException;
import com.mukijidev.board.common.SecurityConfig;
import com.mukijidev.board.user.LoginUser;
import com.mukijidev.board.user.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(PostController.class)
@Import(SecurityConfig.class)
class PostControllerTest {

    private static final String BODY = """
            {"title" : "Hello", "content": "First post"}
            """;


    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PostService postService;

    private static LoginUser alice() {
        User user = new User("alice", "encoded");
        ReflectionTestUtils.setField(user, "id", 1L);
        return new LoginUser(user);
    }

    //  로그인 + csrf 토큰이 있으면 201
    // with(user()) 요청을 사용자가 로그인한 상태로
    // with(csrf()) 올바른 csrf 토큰을 넣는다

    @Test
    void createReturns201() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        when(postService.create(any(), eq(1L)))
                .thenReturn(new PostResponse(1L, "Hello", "First post", now, now, 0, "alice"));

        mockMvc.perform(post("/posts").with(user(alice())).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/posts/1"))
                .andExpect(jsonPath("$.authorName").value("alice"));

    }

    //로그인 안하면 Security가 Controller 앞에서 막는다 -> 401
    @Test
    void createReturns401WhenNotLoggedIn() throws Exception {
        mockMvc.perform(post("/posts").with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isUnauthorized());

        verify(postService, never()).create(any(), any());
    }


    // 로그인했어도 csrf 토큰이 없으면 403
    @Test
    void createReturns403WithoutCsrf() throws Exception {
        mockMvc.perform(post("/posts").with(user(alice()))
                .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isForbidden());
    }

    @Test
    void createReturns400WhenBlank() throws Exception {
        mockMvc.perform(post("/posts").with(user(alice())).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "", "content": ""}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("content is required, title is required"));
    }

    // Service가 NotAuthorException을 던지면 403과 메시지 JSON
    @Test
    void updateReturns403WhenNotAuthor() throws Exception {
        when(postService.update(eq(5L), any(), eq(1L))).thenThrow(new NotAuthorException());

        mockMvc.perform(put("/posts/5").with(user(alice())).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("You are not the author."));
    }


    @Test
    void getReturns404WhenNotFound() throws Exception {
        when(postService.get(99L)).thenThrow(new PostNotFoundException(99L));

        mockMvc.perform(get("/posts/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Post not found: 99"));
    }

    @Test
    void getReturns400WhenIdIsNotNumber() throws Exception {
        mockMvc.perform(get("/posts/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid value for id"));
    }



}
