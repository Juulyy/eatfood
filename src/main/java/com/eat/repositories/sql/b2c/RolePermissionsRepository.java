package com.eat.repositories.sql.b2c;

import com.eat.models.common.Role;
import com.eat.models.common.RolePermissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RolePermissionsRepository extends JpaRepository<RolePermissions, Long> {

    @Query("select r from RolePermissions r where r.role = :roleParam")
    RolePermissions findByRole(@Param("roleParam") Role role);

    @Query("select r from RolePermissions r where r.role = :roleParam and r.accessObject = :objectParam")
    RolePermissions findByRoleAndAccessObject(@Param("roleParam") Role role,
                                              @Param("objectParam") RolePermissions.AccessObject object);

    @Modifying
    @Transactional
    @Query("update RolePermissions r set r.accessObject = :objectParam, r.role = :roleParam, " +
            "r.canAdd = :addParam, r.canDelete = :deleteParam, r.canView = :viewParam, " +
            "r.canViewReports = :viewReportsParam where r.id = :idParam")
    void update(@Param("objectParam") RolePermissions.AccessObject object, @Param("roleParam") Role role,
                @Param("addParam") boolean add, @Param("deleteParam") boolean delete,
                @Param("viewParam") boolean view, @Param("viewReportsParam") boolean viewReports,
                @Param("idParam") Long id);

    @Query(value = "select r from RolePermissions r where r.role = :roleParam and r.accessObject = :accessObjParam " +
            "and r.canAdd = :canAddParam and r.canView = :canViewParam and r.canEdit = :canEditParam " +
            "and r.canDelete = :canDeleteParam and r.canViewReports = :canViewRepParam")
    RolePermissions findByObject(@Param("roleParam") Role role,
                                 @Param("accessObjParam") RolePermissions.AccessObject accessObject,
                                 @Param("canAddParam") Boolean canAdd, @Param("canViewParam") Boolean canView,
                                 @Param("canEditParam") Boolean canEdit, @Param("canDeleteParam") Boolean canDelete,
                                 @Param("canViewRepParam") Boolean canViewReports);

}