package com.mukijidev.board.post;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PostService postService;

    @Test
    void createReturns201() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        when(postService.create(any())).thenReturn(new PostResponse(1L, "Hello",
                "First post", now, now, 0));

        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "Hello", "content": "First post"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/posts/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Hello"));
    }

    @Test
    void createReturns400WhenBlank() throws Exception {
        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "", "content": ""}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("content is required, title is required"));

        verify(postService, never()).create(any());
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
