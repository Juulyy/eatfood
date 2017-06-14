package com.eat.repositories.sql.b2c;

import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.AppUserCollection;
import com.eat.models.b2c.enums.AppUserCollectionType;
import com.eat.models.b2c.AppUserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserCollectionRepository extends JpaRepository<AppUserCollection, Long> {

    @Query("select c from AppUserCollection c where c.appUserPreference.user = :user")
    List<AppUserCollection> findByAppUser(@Param("user") AppUser appUser);

    @Query("select c from AppUserCollection c where c.id = :idParam and c.appUserPreference = :prefParam")
    AppUserCollection findAppUserCollectionById(@Param("idParam") Long collectionId,
                                                @Param("prefParam") AppUserPreference preference);

    @Query("select c from AppUserCollection c where c.appUserPreference = :prefParam and c.collectionType = :typeParam")
    AppUserCollection findAppUserCollectionByType(@Param("prefParam") AppUserPreference userPreferences,
                                                  @Param("typeParam") AppUserCollectionType type);

    @Query("select c from AppUserCollection c where c.appUserPreference = :prefParam and c.collectionType = :typeParam")
    List<AppUserCollection> findAppUserCollectionsByType(@Param("prefParam") AppUserPreference userPreferences,
                                                         @Param("typeParam") AppUserCollectionType type);

}
