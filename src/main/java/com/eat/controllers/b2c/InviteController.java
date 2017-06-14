package com.eat.controllers.b2c;

import com.eat.models.b2c.Invite;
import com.eat.services.b2c.InviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/invite/")
public class InviteController {

    @Autowired
    private InviteService referralService;

    @PostMapping(value = "user/{id}/new", consumes = "application/json", produces = "application/json")
    public String inviteUser(@PathVariable("id") Long userId) {
        return referralService.getRefUrl(userId);
    }

    @GetMapping(value = "user/{id}/find-all", consumes = "application/json", produces = "application/json")
    public List<Invite> findAllUserInvites(@PathVariable("id") Long userId) {
        return referralService.findAllByUser(userId);
    }

    @GetMapping(value = "/find-all", consumes = "application/json", produces = "application/json")
    public List<Invite> findAll() {
        return referralService.findAll();
    }

}