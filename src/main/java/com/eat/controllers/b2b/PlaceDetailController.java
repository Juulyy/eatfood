package com.eat.controllers.b2b;

import com.eat.models.b2b.PlaceDetail;
import com.eat.services.b2b.PlaceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/place/detail")
public class PlaceDetailController {

    @Autowired
    private PlaceDetailService service;

    @PostMapping(value = "/add", produces = "application/json")
    public PlaceDetail add(@RequestBody PlaceDetail placeDetail) {
        return service.save(placeDetail);
    }

    @DeleteMapping(value = "/{id}")
    public HttpStatus delete(@PathVariable Long id) {
        service.delete(id);
        return HttpStatus.OK;
    }

    @PatchMapping(value = "/{id}", produces = "application/json")
    public PlaceDetail update(@RequestBody PlaceDetail placeDetail) {
        return service.update(placeDetail);
    }

    @GetMapping(value = "/", produces = "application/json")
    public List<PlaceDetail> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public PlaceDetail findById(@PathVariable Long id) {
        return service.findById(id);
    }

}