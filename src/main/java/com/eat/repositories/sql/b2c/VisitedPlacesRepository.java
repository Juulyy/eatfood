package com.eat.repositories.sql.b2c;

import com.eat.models.b2b.Place;
import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.VisitedPlaces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitedPlacesRepository extends JpaRepository<VisitedPlaces, Long> {

    @Query("select v from VisitedPlaces v where v.appUserPreference.user = :userParam")
    List<VisitedPlaces> findAllByUser(@Param("userParam") AppUser appUser);

    @Query("select v from VisitedPlaces v where v.appUserPreference.user = :userParam and v.place = :placeParam")
    List<VisitedPlaces> findAllByUserAndPlace(@Param("userParam") AppUser appUser, @Param("placeParam") Place place);

}
