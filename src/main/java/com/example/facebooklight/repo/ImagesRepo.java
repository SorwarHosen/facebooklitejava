package com.example.facebooklight.repo;

import com.example.facebooklight.model.Photos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImagesRepo extends JpaRepository<Photos, Long> {

    @Query(value = "select photo_Url from Images_Table WHERE entity_Id = ?1",
            nativeQuery = true)
    Optional<String> getPhotoByEntity(Long entityId);
}
