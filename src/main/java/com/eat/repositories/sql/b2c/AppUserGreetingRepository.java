package com.eat.repositories.sql.b2c;

import com.eat.models.b2c.AppUserGreeting;
import com.eat.models.b2c.enums.AppUserGreetingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppUserGreetingRepository extends JpaRepository<AppUserGreeting, Long> {

    List<AppUserGreeting> findGreetingByType(@Param("greetingType") AppUserGreetingType greetingType);

    AppUserGreeting findSystemGreetingByType(AppUserGreetingType greetingType);

    @Query("select g from AppUserGreeting g join g.times times where g.type = :greetingType and times.fromTime <= :time and times.toTime >= :time")
    List<AppUserGreeting> findGreetingByTypeAndByTime(@Param("greetingType") AppUserGreetingType greetingType, @Param("time") LocalTime time);

    @Query("select g from AppUserGreeting g join g.times times join g.dates dates where g.type = :greetingType " +
            "and times.fromTime <= :time and times.toTime >= :time and dates.fromDate <= :date and dates.toDate >= :date")
    List<AppUserGreeting> findGreetingByTypeAndTimeAndDate(@Param("greetingType") AppUserGreetingType greetingType,
                                                           @Param("time") LocalTime time,
                                                           @Param("date") LocalDate date);


    /*@Query("select g from AppUserGreeting union ")
    List<AppUserGreeting> findContextSpecificGreeting(@Param("greetingType")AppUserGreetingType greetingType,
                                                           @Param("time")LocalTime time,
                                                           @Param("date")LocalDate date);*/

}