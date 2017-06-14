package com.eat.repositories.sql.b2c;

import com.eat.models.b2c.AppUserToken;
import com.eat.models.b2c.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public interface AppUserTokenRepository extends JpaRepository<AppUserToken, Long> {

    @Query("select u from AppUserToken u where u.token = :tokenParam")
    AppUserToken findByToken(@Param("tokenParam") String token);

    @Query("select u from AppUserToken u where u.appUser = :userParam")
    AppUserToken findByUser(@Param("userParam") AppUser appUser);

}
