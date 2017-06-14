package com.eat.services.b2c;

import com.eat.controllers.exceptions.DataNotFoundException;
import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.DefaultResourcesConstants;
import com.eat.models.b2c.Invite;
import com.eat.repositories.sql.b2c.InviteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class InviteService {

    @Autowired
    private InviteRepository repository;

    @Autowired
    private AppUserService userService;

    public String getRefUrl(Long userId) {
        AppUser appUser = userService.findById(userId);
        if (appUser != null) {
            Invite invite = createInvite(appUser);
            save(invite);
            return DefaultResourcesConstants.REGISTRATION_BASE_URL.concat(invite.getInviteUrl());
        }
        throw new DataNotFoundException(userId);
    }

    private Invite createInvite(AppUser appUser) {
        return Invite.of()
                .appUser(appUser)
                .refId(UUID.randomUUID().toString())
                .create();
    }

    public Invite findByUserAndRefId(AppUser appUser, String refId) {
        return repository.findByUserAndRefId(appUser, refId);
    }

    public List<Invite> findAllByUser(Long userId) {
        AppUser appUser = userService.findById(userId);
        if (appUser != null) {
            return repository.findAllByUser(appUser);
        }
        throw new DataNotFoundException(userId);
    }

    public Invite findById(Long id) {
        return repository.findOne(id);
    }

    public Invite save(Invite invite) {
        return repository.save(invite);
    }

    public void update(Invite invite) {
        repository.save(invite);
    }

    public void delete(Invite invite) {
        repository.delete(invite);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public List<Invite> findAll() {
        return repository.findAll();
    }

}