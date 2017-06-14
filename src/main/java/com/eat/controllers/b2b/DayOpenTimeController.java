package com.eat.controllers.b2b;

import com.eat.models.b2b.DayOpenTime;
import com.eat.services.b2b.DayOpenTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(value = "/api/open-time")
public class DayOpenTimeController {

    @Autowired
    private DayOpenTimeService service;

    @PostMapping(value = "/add", produces = "application/json")
    public DayOpenTime add(@RequestBody DayOpenTime dayOpenTime){
        return service.save(dayOpenTime);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

    @PatchMapping(value = "/{id}")
    public DayOpenTime update(@RequestBody DayOpenTime dayOpenTime){
        return service.save(dayOpenTime);
    }

    @GetMapping(value = "/find-all")
    public Collection<DayOpenTime> getAll(){
        return service.getAll();
    }

}
