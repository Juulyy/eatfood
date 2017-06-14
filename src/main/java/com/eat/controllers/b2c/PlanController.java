package com.eat.controllers.b2c;

import com.eat.models.b2c.Plan;
import com.eat.services.b2c.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/plan/")
public class PlanController {

    @Autowired
    private PlanService planService;

    @PostMapping(value = "/add/from-offer", consumes = "application/json", produces = "application/json")
    public Plan addFromOffer(@RequestBody Plan plan, @RequestParam("userId") Long userId,
                             @RequestParam("offerId") Long offerId,
                             @RequestParam("participantsIds") ArrayList<Long> participantsIds) {
        return planService.createFromOffer(plan, userId, offerId, participantsIds);
    }

    @PostMapping(value = "/add/from-place", consumes = "application/json", produces = "application/json")
    public Plan addFromPlace(@RequestBody Plan plan, @RequestParam("userId") Long userId,
                             @RequestParam("placeId") Long placeId,
                             @RequestParam("participantsIds") ArrayList<Long> participantsIds) {
        return planService.createFromPlace(plan, userId, placeId, participantsIds);
    }

    @GetMapping(value = "/user/{id}/find-all", consumes = "application/json", produces = "application/json")
    public List<Plan> findAllByUser(@PathVariable("id") Long userId) {
        return planService.findAllByUser(userId);
    }

    @GetMapping(value = "/place/{id}/find-all", consumes = "application/json", produces = "application/json")
    public List<Plan> findAllByPlace(@PathVariable("placeId") Long placeId) {
        return planService.findAllByPlace(placeId);
    }

    @GetMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public Plan findById(@PathVariable("id") Long id) {
        return planService.findById(id);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public Plan update(@RequestBody Plan plan) {
        return planService.update(plan);
    }

    @DeleteMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public void delete(@PathVariable Long id) {
        planService.delete(id);
    }

    @GetMapping(value = "/find-all", consumes = "application/json", produces = "application/json")
    public List<Plan> getAll() {
        return planService.findAll();
    }

}
