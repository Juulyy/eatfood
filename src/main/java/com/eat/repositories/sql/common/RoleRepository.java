package com.eat.repositories.sql.common;

import com.eat.models.common.Role;
import com.eat.models.common.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select u from Role u where u.type = :toleTypeParam")
    Role findByRoleType(@Param("toleTypeParam") RoleType roleType);

    @Query("select u from Role u where u.name = :nameParam")
    Role findByName(@Param("nameParam") String name);

}
