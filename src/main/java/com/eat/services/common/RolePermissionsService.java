package com.eat.services.common;

import com.eat.models.common.Role;
import com.eat.models.common.RolePermissions;
import com.eat.repositories.sql.b2c.RolePermissionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionsService {

    @Autowired
    private RolePermissionsRepository permissionsRepository;

    public RolePermissions findByObject(RolePermissions permissions) {
        return permissionsRepository.findByObject(permissions.getRole(), permissions.getAccessObject(),
                permissions.getCanAdd(), permissions.getCanView(), permissions.getCanEdit(),
                permissions.getCanDelete(), permissions.getCanViewReports());
    }

    public RolePermissions createRolePermissions(Role role) {
        RolePermissions permissions = setPermissionByRole(role);
        RolePermissions persistentPermissions = findByObject(permissions);
        if (persistentPermissions == null) {
            return save(permissions);
        }
        return persistentPermissions;
    }

    public RolePermissions setPermissionByRole(Role role) {
        RolePermissions permissions = RolePermissions.of()
                .role(role)
                .create();
        switch (role.getType()) {
            case ROLE_SUPER_ADMIN:
                permissions.setAccessObject(RolePermissions.AccessObject.DEFAULT);
                permissions.setCanAdd(true);
                permissions.setCanView(true);
                permissions.setCanEdit(true);
                permissions.setCanDelete(true);
                permissions.setCanViewReports(true);
                return permissions;
            case ROLE_BUSINESS_ADMIN:
                permissions.setAccessObject(RolePermissions.AccessObject.DEFAULT);
                permissions.setCanAdd(true);
                permissions.setCanView(true);
                permissions.setCanEdit(true);
                permissions.setCanDelete(true);
                permissions.setCanViewReports(true);
                return permissions;
            case ROLE_BUSINESS_MANAGER:
                permissions.setAccessObject(RolePermissions.AccessObject.DEFAULT);
                permissions.setCanAdd(true);
                permissions.setCanView(true);
                permissions.setCanEdit(true);
                permissions.setCanDelete(true);
                permissions.setCanViewReports(true);
                return permissions;
            case ROLE_BUSINESS_USER:
                permissions.setAccessObject(RolePermissions.AccessObject.DEFAULT);
                permissions.setCanAdd(false);
                permissions.setCanView(true);
                permissions.setCanEdit(false);
                permissions.setCanDelete(false);
                permissions.setCanViewReports(true);
                return permissions;
            case ROLE_APP_CURATOR:
                permissions.setAccessObject(RolePermissions.AccessObject.DEFAULT);
                permissions.setCanAdd(false);
                permissions.setCanView(true);
                permissions.setCanEdit(false);
                permissions.setCanDelete(false);
                permissions.setCanViewReports(false);
                return permissions;
            case ROLE_APP_USER:
                permissions.setAccessObject(RolePermissions.AccessObject.DEFAULT);
                permissions.setCanAdd(false);
                permissions.setCanView(true);
                permissions.setCanEdit(false);
                permissions.setCanDelete(false);
                permissions.setCanViewReports(false);
                return permissions;
        }
        return permissions;
    }

    public RolePermissions getPermissionsByRole(Role role) {
        return permissionsRepository.findByRole(role);
    }

    private RolePermissions getPermissionsByRoleAndAccessObject(Role role, RolePermissions.AccessObject object) {
        return permissionsRepository.findByRoleAndAccessObject(role, object);
    }

    public List<RolePermissions> getAll() {
        return permissionsRepository.findAll();
    }

    public void updatePermissionsByParams(RolePermissions permissions) {
        permissionsRepository.update(permissions.getAccessObject(), permissions.getRole(),
                permissions.getCanAdd(), permissions.getCanDelete(), permissions.getCanView(),
                permissions.getCanViewReports(), permissions.getId());
    }

    private RolePermissions update(RolePermissions permissions) {
        return permissionsRepository.save(permissions);
    }

    private RolePermissions save(RolePermissions permissions) {
        return permissionsRepository.save(permissions);
    }

}