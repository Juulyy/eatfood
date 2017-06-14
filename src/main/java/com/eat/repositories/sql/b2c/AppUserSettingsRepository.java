package com.eat.repositories.sql.b2c;

import com.eat.models.b2c.AppUserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserSettingsRepository extends JpaRepository<AppUserSettings, Long> {

}
