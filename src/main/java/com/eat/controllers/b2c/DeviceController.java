package com.eat.controllers.b2c;

import com.eat.factories.EntityFactory;
import com.eat.models.common.AuthoritiesConstants;
import com.eat.models.b2c.Device;
import com.eat.services.b2c.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "/api/user/device")
@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BUSINESS_MANAGER, AuthoritiesConstants.BUSINESS_USER})
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private EntityFactory entityFactory;

    @GetMapping(value = "/find-all", produces = "application/json")
    public Collection<Device> findAll() {
        return deviceService.findAll();
    }

    @PostMapping(value = "/add", produces = "application/json")
    public ResponseEntity add(@RequestParam("userId") Long userId, @RequestBody String deviceRaw) {
        deviceService.add(userId, entityFactory.createInstance(deviceRaw, Device.class));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "remove/{id}", produces = "application/json")
    public void remove(@PathVariable("id") Long deviceId) {
        deviceService.remove(deviceId);
    }

}
