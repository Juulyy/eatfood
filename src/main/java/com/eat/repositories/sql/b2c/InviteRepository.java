package com.eat.repositories.sql.b2c;

import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Long> {

    @Query(value = "select i from Invite i where i.appUser = :userParam")
    List<Invite> findAllByUser(@Param("userParam") AppUser appUser);

    @Query(value = "select i from Invite i where i.appUser = :userParam and i.refId = :refParam")
    Invite findByUserAndRefId(@Param("userParam") AppUser appUser, @Param("refParam") String refId);

}
