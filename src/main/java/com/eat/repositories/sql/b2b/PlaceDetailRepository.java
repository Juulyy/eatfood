package com.eat.repositories.sql.b2b;

import com.eat.models.b2b.PlaceDetail;
import com.eat.models.b2b.enums.PlaceDetailType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceDetailRepository extends BaseB2BRepository<PlaceDetail, Long> {

    @Query(value = "select p from PlaceDetail p where p.name = :nameParam")
    PlaceDetail findByName(@Param("nameParam") String name);

    @Query(value = "select p from PlaceDetail p where p.placeDetailType = :typeParam")
    List<PlaceDetail> findAllByType(@Param("typeParam") PlaceDetailType type);

}
