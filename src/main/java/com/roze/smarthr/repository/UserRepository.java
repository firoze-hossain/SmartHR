package com.roze.smarthr.repository;

import com.roze.smarthr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    Boolean existsByUsernameIgnoreCase(String username);

    Boolean existsByEmailIgnoreCase(String email);
}
