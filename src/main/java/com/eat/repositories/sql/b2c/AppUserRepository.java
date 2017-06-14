package com.eat.repositories.sql.b2c;

import com.eat.models.b2c.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    @Query("select u from AppUser u where u.login = :loginParam")
    AppUser findByLogin(@Param("loginParam") String login);

    @Query("select u from AppUser u where u.email = :emailParam")
    AppUser findByEmail(@Param("emailParam") String email);

    @Query(nativeQuery = true, value = "select * from APP_USER where user_id in " +
            "(select APP_USER_FOLLOWING.FOLLOWING_FK from APP_USER_FOLLOWERS inner join APP_USER_FOLLOWING on " +
            "APP_USER_FOLLOWERS.APP_USER_FK = :idParam and APP_USER_FOLLOWING.APP_USER_FK = :idParam " +
            "and APP_USER_FOLLOWERS.FOLLOWERS_FK = APP_USER_FOLLOWING.FOLLOWING_FK)")
    List<AppUser> findMutualFollowers(@Param("idParam") Long userId);

}
