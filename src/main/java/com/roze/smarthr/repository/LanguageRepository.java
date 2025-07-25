package com.roze.smarthr.repository;

import com.roze.smarthr.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findByCode(String code);

    List<Language> findByIsActiveTrue();

    Optional<Language> findByIsDefaultTrue();

    @Query("SELECT l FROM Language l WHERE l.isActive = true AND l.code = :code")
    Optional<Language> findActiveByCode(String code);

    @Query("SELECT l FROM Language l WHERE l.isDefault = true")
    List<Language> findAllDefaultLanguages();

    default Optional<Language> findDefaultLanguage() {
        List<Language> defaults = findAllDefaultLanguages();
        return defaults.isEmpty() ? Optional.empty() : Optional.of(defaults.get(0));
    }
}