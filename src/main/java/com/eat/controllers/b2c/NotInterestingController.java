package com.eat.controllers.b2c;

import com.eat.models.b2c.NotInteresting;
import com.eat.services.b2c.NotInterestingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/not-interesting")
public class NotInterestingController {

    @Autowired
    private NotInterestingService notInterestingService;

    @GetMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public NotInteresting get(@PathVariable("id") Long id) {
        return notInterestingService.findById(id);
    }

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public NotInteresting add(@RequestBody NotInteresting notInteresting) {
        return notInterestingService.save(notInteresting);
    }

    @DeleteMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public void delete(@PathVariable Long id) {
        notInterestingService.delete(id);
    }

    @GetMapping(value = "/find-all", consumes = "application/json", produces = "application/json")
    public List<NotInteresting> getAll() {
        return notInterestingService.findAll();
    }

}
