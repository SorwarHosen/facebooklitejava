package com.example.facebooklight.repo;

import com.example.facebooklight.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {

    @Query(value = "select * from Post_Table WHERE actor_Id = ?1 ORDER BY create_Date desc",
            nativeQuery = true)
    List<Post> getAllPostByUserId(long userId);
}
