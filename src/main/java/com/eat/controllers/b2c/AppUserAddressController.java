package com.eat.controllers.b2c;

import com.eat.models.b2c.AppUserAddress;
import com.eat.services.b2c.AppUserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/address")
public class AppUserAddressController {

    @Autowired
    private AppUserAddressService addressService;

    @PostMapping(value = "user/{id}/add", consumes = "application/json", produces = "application/json")
    public AppUserAddress addAddress(@PathVariable("id") Long userId, @RequestBody AppUserAddress address) {
        return addressService.add(userId, address);
    }

    @DeleteMapping(value = "user/{id}/remove", consumes = "application/json", produces = "application/json")
    public HttpStatus removeAddress(@PathVariable("id") Long userId, @RequestParam("addressId") Long addressId) {
        addressService.remove(userId, addressId);
        return HttpStatus.OK;
    }

    @GetMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public AppUserAddress findById(@PathVariable("id") Long placeId) {
        return addressService.findById(placeId);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public HttpStatus update(@RequestBody AppUserAddress address) {
        addressService.update(address);
        return HttpStatus.OK;
    }

    @GetMapping(value = "user/{id}/find-all", consumes = "application/json", produces = "application/json")
    public List<AppUserAddress> findAllUserAppUserAddress(@PathVariable("id") Long userId) {
        return addressService.findAllByAppUser(userId);
    }

    @GetMapping(value = "/find-all", consumes = "application/json", produces = "application/json")
    public List<AppUserAddress> findAll() {
        return addressService.findAll();
    }

}
