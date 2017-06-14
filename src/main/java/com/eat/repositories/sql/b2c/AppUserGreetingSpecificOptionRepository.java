package com.eat.repositories.sql.b2c;

import com.eat.models.b2c.AppUserGreetingContextSpecific;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserGreetingSpecificOptionRepository extends JpaRepository<AppUserGreetingContextSpecific, Long> {

}