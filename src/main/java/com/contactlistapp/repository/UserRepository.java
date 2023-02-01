package com.contactlistapp.repository;

import com.contactlistapp.domain.User;
import com.contactlistapp.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    @Query("SELECT new com.contactlistapp.dto.response.UserResponse(user)  FROM User user where  lower(user.email) like %?1% ")
    Page<UserResponse> getFilteredUsersWithQ(String q, Pageable pageable);

    @Query("SELECT new com.contactlistapp.dto.response.UserResponse(u) FROM User u")
    Page<UserResponse> getAllUsersByPage(Pageable pageable);

}
