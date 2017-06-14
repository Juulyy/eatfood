package com.eat.repositories.sql.b2c;

import com.eat.models.b2b.Place;
import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    @Query(value = "select p from Plan p where p.place = :placeParam")
    List<Plan> findAllByPlace(@Param("placeParam") Place place);

    @Query(nativeQuery = true, value = "select * from PLAN where PLAN.PLAN_ID in " +
            "(select PLAN_PARTICIPANTS.PLAN_FK from PLAN_PARTICIPANTS where PLAN_PARTICIPANTS.USER_ID = :userParam)")
    List<Plan> findAllByUser(@Param("userParam") AppUser user);

}