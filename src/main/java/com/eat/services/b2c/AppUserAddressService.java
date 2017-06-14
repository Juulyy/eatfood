package com.eat.services.b2c;

import com.eat.controllers.exceptions.DataNotFoundException;
import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.AppUserAddress;
import com.eat.repositories.sql.b2c.AppUserAddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AppUserAddressService {

    @Autowired
    private AppUserAddressRepository repository;

    @Autowired
    private AppUserService userService;

    public AppUserAddress add(Long userId, AppUserAddress address) {
        AppUser user = userService.findById(userId);
        if (user != null && address != null) {
            address.setAppUserPreference(user.getUserPreferences());
            save(address);
        }
        throw new DataNotFoundException();
    }

    public void remove(Long userId, Long addressId) {
        AppUser user = userService.findById(userId);
        AppUserAddress address = findById(addressId);
        if (user != null && address != null) {
            user.getUserPreferences().getFavoriteAddresses().remove(address);
            delete(address);
        }
        throw new DataNotFoundException();
    }

    public AppUserAddress findById(Long id) {
        return repository.findOne(id);
    }

    public AppUserAddress save(AppUserAddress address) {
        return repository.save(address);
    }

    public AppUserAddress update(AppUserAddress address) {
        return repository.save(address);
    }

    public void delete(AppUserAddress address) {
        repository.delete(address);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public List<AppUserAddress> findAll() {
        return repository.findAll();
    }

    public List<AppUserAddress> findAllByAppUser(Long userId) {
        AppUser user = userService.findById(userId);
        if (user != null) {
            return repository.findAllByAppUser(user.getUserPreferences());
        }
        log.error("There no user by id: " + user + "!");
        throw new DataNotFoundException();
    }

}