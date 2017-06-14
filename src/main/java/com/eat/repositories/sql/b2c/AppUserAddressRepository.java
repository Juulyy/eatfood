package com.eat.repositories.sql.b2c;

import com.eat.models.b2c.AppUserAddress;
import com.eat.models.b2c.AppUserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserAddressRepository extends JpaRepository<AppUserAddress, Long> {

    @Query(value = "select a from AppUserAddress a where a.appUserPreference = :prefParam")
    List<AppUserAddress> findAllByAppUser(@Param("prefParam") AppUserPreference userPreferences);

}
