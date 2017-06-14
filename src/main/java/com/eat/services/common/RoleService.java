package com.eat.services.common;

import com.eat.controllers.exceptions.DataNotFoundException;
import com.eat.models.b2c.AppUser;
import com.eat.models.common.Role;
import com.eat.models.common.RolePermissions;
import com.eat.models.common.enums.RoleType;
import com.eat.repositories.sql.common.RoleRepository;
import com.eat.services.b2c.AppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RoleService {

    @Autowired
    private RolePermissionsService permissionsService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AppUserService userService;

    public Role findById(Long id) {
        return roleRepository.findOne(id);
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public Role update(Role role) {
        return roleRepository.save(role);
    }

    public void delete(Role role) {
        roleRepository.delete(role);
    }

    public void delete(Long id) {
        roleRepository.delete(id);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Role findByRoleType(RoleType roleType) {
        Role role = roleRepository.findByRoleType(roleType);
        if (role == null) {
            return createEmptyRoleByRoleType(roleType);
        }
        return role;
    }

    public Role createEmptyRoleByRoleType(RoleType roleType) {
        return save(Role.of()
                .name(roleType.getName())
                .type(roleType)
                .create());
    }

    public Role getRoleByRoleType(RoleType roleType) {
        Role role = null;
        List<RolePermissions> list = new ArrayList<>();
        switch (roleType) {
            case ROLE_SUPER_ADMIN:
                role = findByRoleType(RoleType.ROLE_SUPER_ADMIN);
                break;
            case ROLE_BUSINESS_ADMIN:
                role = findByRoleType(RoleType.ROLE_BUSINESS_ADMIN);
                break;
            case ROLE_BUSINESS_MANAGER:
                role = findByRoleType(RoleType.ROLE_BUSINESS_MANAGER);
                break;
            case ROLE_BUSINESS_USER:
                role = findByRoleType(RoleType.ROLE_BUSINESS_USER);
                break;
            case ROLE_APP_CURATOR:
                role = findByRoleType(RoleType.ROLE_APP_USER);
                break;
            case ROLE_APP_USER:
                role = findByRoleType(RoleType.ROLE_APP_CURATOR);
                break;
        }
        list.add(permissionsService.createRolePermissions(role));
        role.setPermissions(list);
        return role;
    }

    public void setDefaultRole(Long userId) {
        AppUser user = userService.findById(userId);
        if (user != null) {
            user.setRole(getRoleByRoleType(RoleType.ROLE_APP_USER));
            userService.update(user);
        }
        throw new DataNotFoundException();
    }

    public void setCuratorRole(Long userId) {
        AppUser user = userService.findById(userId);
        if (user != null) {
            user.setRole(getRoleByRoleType(RoleType.ROLE_APP_CURATOR));
        }
        throw new DataNotFoundException();
    }

}