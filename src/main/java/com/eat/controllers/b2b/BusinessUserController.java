package com.eat.controllers.b2b;

import com.eat.factories.EntityFactory;
import com.eat.models.b2b.BusinessUser;
import com.eat.services.b2b.BusinessUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(value = "/api/business-user")
public class BusinessUserController {

    @Autowired
    private BusinessUserService businessUserService;

    @Autowired
    private EntityFactory entityFactory;

    @GetMapping(value = "/{id}", produces = "application/json")
    public BusinessUser get(@PathVariable("id") Long id) {
        return businessUserService.findById(id);
    }

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public BusinessUser add(@RequestBody String rawBusiness) {
        return businessUserService.save(entityFactory.createInstance(rawBusiness, BusinessUser.class));
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        businessUserService.delete(id);
    }

    @PatchMapping(value = "/{id}")
    public BusinessUser update(@RequestBody String rawBusiness) {
        return businessUserService.update(entityFactory.createInstance(rawBusiness, BusinessUser.class));
    }

    @GetMapping(value = "/find-all", produces = "application/json")
    public Collection<BusinessUser> getAllBusiness() {
        return businessUserService.findAll();
    }

}
