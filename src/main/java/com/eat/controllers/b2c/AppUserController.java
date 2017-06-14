package com.eat.controllers.b2c;

import com.eat.models.b2c.AppUser;
import com.eat.models.common.AuthoritiesConstants;
import com.eat.models.common.enums.CrudMethod;
import com.eat.services.b2c.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping(value = "/{id}/reset-password", consumes = "application/json", produces = "application/json")
    public ResponseEntity resetPassword(@PathVariable("id") Long userId) {
        appUserService.resetPassword(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}/change-password", consumes = "application/json", produces = "application/json")
    public ResponseEntity changePassword(@PathVariable("id") Long userId, @RequestParam String token,
                                         @RequestParam String pass) {
        appUserService.changePassword(userId, token, pass);
        return ResponseEntity.ok().build();
    }

//    TODO rebuild impl
    /*@PostMapping(value = "/authentication", consumes = "application/json", produces = "application/json")
    public ResponseEntity authentication(@RequestBody String authData) {
        return appUserService.processAuthentication(parseJsonToMap(authData));
    }*/

//    TODO rebuild impl
    /*@PostMapping(value = "/{id}/resend-token", consumes = "application/json", produces = "application/json")
    public ResponseEntity resendToken(@RequestBody String emailData) {
        return appUserService.processTokenResending(parseJsonToMap(emailData));
    }*/

    @GetMapping(value = "/find-all", consumes = "application/json", produces = "application/json")
    public List<AppUser> getAll() {
        return appUserService.findAll();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public AppUser findById(@PathVariable("id") Long id) {
        return appUserService.findById(id);
    }

    @DeleteMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Secured(AuthoritiesConstants.ADMIN)
    public void delete(@PathVariable Long id) {
        appUserService.delete(id);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public void update(@RequestBody AppUser appUser) {
        appUserService.save(appUser);
    }

    @PutMapping(value = "/{id}/photo/new", consumes = "application/json", produces = "application/json")
    public ResponseEntity addPhoto(@PathVariable("id") Long userId, @RequestParam("photoUrl") String photoUrl) {
        appUserService.manageUserPhoto(userId, photoUrl, CrudMethod.CREATE);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}/photo/remove", consumes = "application/json", produces = "application/json")
    public ResponseEntity removePhoto(@PathVariable("id") Long userId) {
        appUserService.manageUserPhoto(userId, null, CrudMethod.DELETE);
        return ResponseEntity.ok().build();
    }

}