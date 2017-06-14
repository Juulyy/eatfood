package com.eat.repositories.sql.b2b;

import com.eat.models.b2b.BusinessUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessUserRepository extends BaseB2BRepository<BusinessUser, Long> {

    @Query("select u from BusinessUser u where u.email = :emailParam")
    BusinessUser findByEmail(@Param("emailParam") String email);

}
