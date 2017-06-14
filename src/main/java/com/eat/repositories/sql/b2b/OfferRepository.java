package com.eat.repositories.sql.b2b;

import com.eat.models.b2b.Offer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OfferRepository extends BaseB2BRepository<Offer, Long> {

    @Query(value = "select o from Offer o where o.expirationDate > :currentDateParam")
    List<Offer> findAllCurrentOffers(@Param(value = "currentDateParam") LocalDateTime currentDate);

}