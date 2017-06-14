package com.eat.services.b2c;

import com.eat.controllers.exceptions.DataNotFoundException;
import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.Device;
import com.eat.repositories.sql.b2c.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository repository;

    @Autowired
    private AppUserService appUserService;

    public void add(Long userId, Device device) {
        AppUser appUser = appUserService.findById(userId);
        if (appUser != null) {
            device.setAppUser(appUser);
            save(device);
        }
        throw new DataNotFoundException();
    }

    public void remove(Long deviceId) {
        repository.delete(deviceId);
    }

    public Device findByUser(AppUser appUser) {
        return repository.findByUser(appUser);
    }

    public Device findByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    public List<Device> findAll() {
        return repository.findAll();
    }

    public Device findById(Long id) {
        return repository.findOne(id);
    }

    public Device save(Device device) {
        return repository.save(device);
    }

    public Device update(Device device) {
        return repository.save(device);
    }

    public void delete(Device device) {
        repository.delete(device);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

}
