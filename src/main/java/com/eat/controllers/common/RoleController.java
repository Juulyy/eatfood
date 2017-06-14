package com.eat.controllers.common;

import com.eat.services.common.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PutMapping(value = "/{id}/set-curator", consumes = "application/json", produces = "application/json")
    public ResponseEntity setCuratorRole(@PathVariable("id") Long userId) {
        roleService.setCuratorRole(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}/set-default", consumes = "application/json", produces = "application/json")
    public ResponseEntity setDefaultRole(@PathVariable("id") Long userId) {
        roleService.setDefaultRole(userId);
        return ResponseEntity.ok().build();
    }

}