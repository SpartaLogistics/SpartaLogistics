package com.sparta.logistics.client.user.repository;

import com.sparta.logistics.client.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndIsDeletedFalse(String username);
    Optional<User> findByIdAndIsDeletedFalse(long id);
}
