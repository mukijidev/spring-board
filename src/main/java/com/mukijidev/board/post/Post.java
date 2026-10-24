package com.mukijidev.board.post;

import com.mukijidev.board.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 5000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    private User author;

    protected Post() {
    }

    public Post(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.author = author;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return  id;}
    public String getTitle() {return title;}
    public String getContent() {return content;}
    public LocalDateTime getCreatedAt() { return createdAt;}
    public LocalDateTime getUpdatedAt() {return updatedAt;}
    public User getAuthor() {return author;}
}
