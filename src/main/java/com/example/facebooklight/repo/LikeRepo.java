package com.example.facebooklight.repo;

import com.example.facebooklight.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepo extends JpaRepository<Like, Long> {

    @Query(value = "select * from Like_Table WHERE post_Id = ?1",
            nativeQuery = true)
    List<Like> getAllLikesByPostId(long postId);

    @Query(value = "select * from Like_Table WHERE actor_Id=?1 AND post_Id = ?2",
            nativeQuery = true)
    Optional<Like> getLikeByLikerAndPostId(long likerId, long postId);

}
