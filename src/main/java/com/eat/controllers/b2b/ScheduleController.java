package com.eat.controllers.b2b;

import com.eat.models.b2b.Schedule;
import com.eat.services.b2b.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(value = "/api/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService service;

    @GetMapping(value = "/{id}", produces = "application/json")
    public Schedule get(@PathVariable("id") Long id){
        return service.getById(id);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Schedule add(@RequestBody Schedule schedule){
        return service.save(schedule);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Schedule update(@RequestBody Schedule schedule){
        return service.save(schedule);
    }

    @GetMapping(value = "/find-all")
    public Collection<Schedule> getAll(){
        return service.getAll();
    }
}
