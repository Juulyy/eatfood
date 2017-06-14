package com.eat.repositories.sql.b2c;

import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    @Query("select d from Device d where d.appUser.id = :idParam")
    Device findByUserId(@Param("idParam") Long userId);

    @Query("select d from Device d where d.appUser = :userParam")
    Device findByUser(@Param("userParam") AppUser appUser);

}
