package com.eat.controllers.b2c;

import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.AppUserSettings;
import com.eat.models.common.enums.CrudMethod;
import com.eat.services.b2c.AppUserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/user/settings")
public class AppUserSettingsController {

    @Autowired
    private AppUserSettingsService service;

    @GetMapping(value = "/find-all", consumes = "application/json", produces = "application/json")
    public List<AppUserSettings> findAll(){
        return service.findAll();
    }

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public AppUserSettings add(@RequestBody AppUserSettings settings) {
        return service.save(settings);
    }

    @GetMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public AppUserSettings getSettings(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public AppUserSettings updateSettings(@RequestBody AppUserSettings settings) {
        return service.update(settings);
    }

    @GetMapping(value = "/{id}/blocked/all", consumes = "application/json", produces = "application/json")
    public List<AppUser> getBlockedUsers(@PathVariable("id") Long id) {
        return service.findBlockedUsers(id);
    }

    @PostMapping(value = "/{id}/blocked/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity addBlockedUser(@PathVariable("id") Long id, @RequestParam("blockedUserId") Long blockedUserId) {
        service.manageBlockedUsers(id, blockedUserId, CrudMethod.CREATE);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/blocked/remove", consumes = "application/json", produces = "application/json")
    public ResponseEntity removeBlockedUser(@PathVariable("id") Long id, @RequestParam("blockedUserId") Long blockedUserId) {
        service.manageBlockedUsers(id, blockedUserId, CrudMethod.DELETE);
        return ResponseEntity.ok().build();
    }

}