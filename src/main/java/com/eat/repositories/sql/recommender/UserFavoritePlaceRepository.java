package com.eat.repositories.sql.recommender;

import com.eat.models.b2b.Place;
import com.eat.models.b2c.AppUser;
import com.eat.models.recommender.UserFavoritePlace;
import com.eat.models.recommender.UserFavoritePlaceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavoritePlaceRepository extends JpaRepository<UserFavoritePlace, UserFavoritePlaceId> {

    @Query("select u from UserFavoritePlace u where u.user = :userParam")
    List<UserFavoritePlace> findByUser(@Param("userParam") AppUser user);

    @Query("select u from UserFavoritePlace u where u.place = :placeParam")
    List<UserFavoritePlace> findByPlace(@Param("placeParam") Place place);

}
