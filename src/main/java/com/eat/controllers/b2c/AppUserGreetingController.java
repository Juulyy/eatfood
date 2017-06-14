package com.eat.controllers.b2c;

import com.eat.logic.b2c.AppUserGreetingPool;
import com.eat.models.b2c.AppUserGreeting;
import com.eat.services.b2c.AppUserGreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-greeting")
public class AppUserGreetingController {

    @Autowired
    private AppUserGreetingService service;

    @Autowired
    private AppUserGreetingPool greetingPool;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AppUserGreeting> getAllGreetings() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AppUserGreeting getGreeting(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    /*@GetMapping(value = "/message", produces = MediaType.APPLICATION_JSON_VALUE)
    public AppUserGreeting getGreetingMessage(@RequestParam("userId") Long userId) {
        return greetingPool.getGreeting(userId);
    }*/

    /*@GetMapping(value = "/context", produces = MediaType.APPLICATION_JSON_VALUE)
    public AppUserGreeting getContextSpecific(@RequestParam("userId") Long userId) {
        return greetingPool.getContextSpecific(userId);
    }*/

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public AppUserGreeting addGreeting(@RequestBody AppUserGreeting greeting) {
        return service.save(greeting);
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AppUserGreeting updateGreeting(@RequestBody AppUserGreeting greeting) {
        return service.update(greeting);
    }

    @DeleteMapping(value = "/{id}")
    public HttpStatus deleteGreetings(@PathVariable("id") Long id) {
        service.delete(id);
        return HttpStatus.OK;
    }

    /*@GetMapping(value = "/get-greeting/by-type", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AppUserGreeting> getGreetingsByType(@RequestParam AppUserGreetingType greetingType) {
        return service.getGreetingByType(greetingType);
    }

    @GetMapping(value = "/get-greeting/by-type-date", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AppUserGreeting> getGreetingsByTypeAndTime(@RequestParam AppUserGreetingType greetingType,
                                                           @RequestParam Long timeZone) {

        LocalTime time = LocalTime.now().plusHours(timeZone);

        return service.getGreetingByTypeAndTime(greetingType, time);
    }


    @GetMapping(value = "/get-greeting/by-type-date-time", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AppUserGreeting> getGreetingByTypeAndTimeAndDate(@RequestParam AppUserGreetingType greetingType,
                                                                 @RequestParam Long timeZone) {

        LocalTime time = LocalTime.now().plusHours(timeZone);
        LocalDate date = LocalDate.now();

        return service.getGreetingByTypeAndTimeAndDate(greetingType, time, date);
    }

    @GetMapping(value = "/get-context-specific-greeting/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AppUserGreeting> getContextSpecificGreeting(@RequestParam Long userId,
                                                            @RequestParam Long timeZone) {

        LocalTime time = LocalTime.now().plusHours(timeZone);
        LocalDate date = LocalDate.now();

        return service.getGreetingByTypeAndTimeAndDate(AppUserGreetingType.SPECIFIC, time, date);
    }*/

}