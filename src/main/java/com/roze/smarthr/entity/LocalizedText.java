package com.roze.smarthr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "localized_texts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocalizedText {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String entityType; // e.g., "JobPost", "Department"

    @Column(nullable = false)
    private Long entityId; // ID of the original record

    @Column(nullable = false, length = 50)
    private String fieldName; // Field to translate

    @Column(nullable = false, length = 10)
    private String languageCode; // e.g., "en", "bn"

    @Column(columnDefinition = "TEXT")
    private String translatedText;
}