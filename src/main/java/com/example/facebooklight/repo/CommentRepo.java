package com.example.facebooklight.repo;

import com.example.facebooklight.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    @Query(value = "select * from Comment_Table WHERE post_Id = ?1 ORDER BY create_Date desc",
            nativeQuery = true)
    List<Comment> getAllCommentsByPostId(long postId);
}
