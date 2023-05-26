package com.example.facebooklight.repo;

import com.example.facebooklight.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    @Query(value = "select * from User_Table WHERE email = ?1 and password = ?2",
            nativeQuery = true)
    Optional<User> signIn(String email, String password);

    @Query(value = "select * from User_Table WHERE email = ?1",
            nativeQuery = true)
    Optional<User> getUserByEmail(String email);



}
