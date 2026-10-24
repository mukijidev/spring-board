package com.mukijidev.board.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = "author")
    Page<Comment> findByPostId(Long postId, Pageable pageable);

    //optional -> orElseThrow  for 404 error
    Optional<Comment> findByIdAndPostId(Long id, Long postId);


    @Modifying
    @Query("delete from Comment c where c.post.id = :postId")
    void deleteByPostId(@Param("postId") Long postId);

    long countByPostId(Long postId);

    @Query("select new com.mukijidev.board.comment.CommentCountPerPost(c.post.id, count(c))"
            + "from Comment c where c.post.id in :postIds group by c.post.id")
    List<CommentCountPerPost> countByPostIds(@Param("postIds") List<Long> postIds);


    @Query(value = "select c from Comment c join fetch c.post join fetch c.author",
            countQuery = "select count(c) from Comment c")
    Page<Comment> findRecent(Pageable pageable);
}