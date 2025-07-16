
package com.roze.smarthr.repository;

import com.roze.smarthr.entity.GlobalSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GlobalSettingRepository extends JpaRepository<GlobalSetting, Long> {
    Optional<GlobalSetting> findByKey(String key);

    @Query("SELECT gs FROM GlobalSetting gs WHERE gs.key LIKE :prefix%")
    List<GlobalSetting> findByKeyPrefix(@Param("prefix") String prefix);

    boolean existsByKey(String key);
}