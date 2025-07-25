package com.roze.smarthr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user_preferences")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 10)
    private String preferredLanguageCode;

    @Column(length = 50)
    private String timezone;

    @Column(length = 20)
    private String dateFormat; // e.g., "dd-MM-yyyy"

    @Column(length = 20)
    private String numberFormat; // e.g., "#,###.##"
}