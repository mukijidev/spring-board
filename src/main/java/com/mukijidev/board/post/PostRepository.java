package com.mukijidev.board.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    //findByTitle
    //findByTitleContaining
    //findByTitleStartingWith
    //findByTitleAndContent
    Page<Post> findByTitleContaining(String keyword, Pageable pageable);
}