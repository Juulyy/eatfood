package com.eat.controllers.b2b;

import com.eat.models.b2b.OfferType;
import com.eat.services.b2b.OfferTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(value = "/api/offer-type")
public class OfferTypeController {

    @Autowired
    private OfferTypeService service;

    @GetMapping(value = "/{id}", produces = "application/json")
    public OfferType get(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @PostMapping(value = "/add", produces = "application/json")
    public OfferType add(@RequestBody OfferType offerType) {
        return service.save(offerType);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PatchMapping(value = "/{id}")
    public OfferType update(@RequestBody OfferType offerType) {
        return service.save(offerType);
    }

    @GetMapping(value = "/find-all")
    public Collection<OfferType> getAll() {
        return service.getAll();
    }


}
