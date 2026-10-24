package com.mukijidev.board.comment;


import com.mukijidev.board.post.Post;
import com.mukijidev.board.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private LocalDateTime createdAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    private User author;

    protected Comment() {

    }

    public Comment(Post post, String content, User author){
        this.post = post;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.author = author;
    }

    public Long getId() {return id;}
    public String getContent() {return content;}
    public Post getPost() { return post;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public User getAuthor() {return author;}
}
