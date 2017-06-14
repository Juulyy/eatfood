package com.eat.controllers.mongo.b2b;

import com.eat.models.mongo.ClaimRequest;
import com.eat.models.mongo.enums.ClaimRequestStatus;
import com.eat.services.mongo.b2b.ClaimRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/claim-request")
public class ClaimRequestController {

    @Autowired
    private ClaimRequestService requestService;

    @GetMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ClaimRequest findById(@PathVariable("id") String id) {
        return requestService.findById(id);
    }

    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ClaimRequest add(@RequestBody ClaimRequest claimRequest) {
        return requestService.save(claimRequest);
    }

    @PutMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ClaimRequest update(@RequestBody ClaimRequest claimRequest) {
        return requestService.update(claimRequest);
    }

    @DeleteMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public void delete(@PathVariable String id) {
        requestService.delete(id);
    }

    @GetMapping(value = "/find-all", consumes = "application/json", produces = "application/json")
    public List<ClaimRequest> findAll() {
        return requestService.findAll();
    }

    @GetMapping(value = "/find-all-by-status", consumes = "application/json", produces = "application/json")
    public List<ClaimRequest> findAllByStatus(@Param(value = "status") ClaimRequestStatus status) {
        return requestService.findAllByStatus(status);
    }

    @GetMapping(value = "/find-all-by-user", consumes = "application/json", produces = "application/json")
    public List<ClaimRequest> findAllByUser(@Param(value = "user") String userId) {
        return requestService.findAllByUserId(userId);
    }

    @GetMapping(value = "/find-all-by-place", consumes = "application/json", produces = "application/json")
    public List<ClaimRequest> findAllByPlace(@Param(value = "place") String placeId) {
        return requestService.findAllByPlaceId(placeId);
    }

    @GetMapping(value = "/find-all-by-text", consumes = "application/json", produces = "application/json")
    public List<ClaimRequest> findAllByText(@Param(value = "text") String text) {
        return requestService.findAllByContainingText(text);
    }

}