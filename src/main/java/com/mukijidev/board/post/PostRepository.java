package com.mukijidev.board.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    //findByTitle
    //findByTitleContaining
    //findByTitleStartingWith
    //findByTitleAndContent
    @Override
    @EntityGraph(attributePaths = "author") // author를 같이가져와서 N+1 방지
    Page<Post> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "author")
    Page<Post> findByTitleContaining(String keyword, Pageable pageable);
}