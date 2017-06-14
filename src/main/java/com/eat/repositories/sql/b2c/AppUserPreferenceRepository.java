package com.eat.repositories.sql.b2c;

import com.eat.models.b2c.AppUserPreference;
import com.eat.models.common.Tag;
import com.eat.models.common.enums.TagType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserPreferenceRepository extends JpaRepository<AppUserPreference, Long> {

    @Query(value = "SELECT t.tag_id, t.name, t.type FROM user_taste_tags AS usr_tstg, tags AS t WHERE\n" +
            "\t\tusr_tstg.user_preference_fk = :userId AND usr_tstg.tag_fk = t.tag_id AND t.type in ('ALLERGY', 'DIET')", nativeQuery = true)
    Object[] findUserPreferencesAllergieAndDietTags(@Param("userId") Long userId);

}
