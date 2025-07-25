package com.roze.smarthr.repository;

import com.roze.smarthr.entity.User;
import com.roze.smarthr.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    Optional<UserPreference> findByUser(User user);

    Optional<UserPreference> findByUserId(Long userId);
}