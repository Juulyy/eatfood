package com.eat.repositories.sql.b2b;

import com.eat.models.b2b.ContactType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactTypeRepository extends ExtendedB2BRepository<ContactType, Long> {

    @Query("SELECT cp FROM ContactType cp WHERE cp.contactAspect = (:contactAspect)")
    List<ContactType> findByContactAspect(@Param("contactAspect") ContactType.ContactAspect contactAspect);

    @Query("select c from ContactType c where c.name = :nameParam")
    ContactType findByName(@Param("nameParam") String name);

}
