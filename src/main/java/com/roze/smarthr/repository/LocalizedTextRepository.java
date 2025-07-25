package com.roze.smarthr.repository;

import com.roze.smarthr.entity.LocalizedText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LocalizedTextRepository extends JpaRepository<LocalizedText, Long> {
    Optional<LocalizedText> findByEntityTypeAndEntityIdAndFieldNameAndLanguageCode(
            String entityType, Long entityId, String fieldName, String languageCode);

    List<LocalizedText> findByEntityTypeAndEntityIdAndLanguageCode(
            String entityType, Long entityId, String languageCode);

    @Query("SELECT lt FROM LocalizedText lt WHERE lt.entityType = :entityType AND lt.entityId = :entityId")
    List<LocalizedText> findAllTranslationsForEntity(String entityType, Long entityId);
}