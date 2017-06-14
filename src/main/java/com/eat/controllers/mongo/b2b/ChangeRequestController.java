package com.eat.controllers.mongo.b2b;

import com.eat.models.mongo.PlaceChangeRequest;
import com.eat.models.mongo.enums.ChangeRequestStatus;
import com.eat.models.mongo.enums.ChangeRequestType;
import com.eat.services.mongo.b2b.PlaceChangeRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/change-request")
public class ChangeRequestController {

    @Autowired
    private PlaceChangeRequestService requestService;

    @GetMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public PlaceChangeRequest findById(@PathVariable("id") String id) {
        return requestService.findById(id);
    }

    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public PlaceChangeRequest add(@RequestBody PlaceChangeRequest request) {
        return requestService.save(request);
    }

    @PutMapping(value = "/", consumes = "application/json", produces = "application/json")
    public PlaceChangeRequest update(@RequestBody PlaceChangeRequest request) {
        return requestService.update(request);
    }

    @DeleteMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public void delete(@PathVariable String id) {
        requestService.delete(id);
    }

    @GetMapping(value = "/find-all", consumes = "application/json", produces = "application/json")
    public List<PlaceChangeRequest> findAll() {
        return requestService.findAll();
    }

    @GetMapping(value = "/find-all-current", consumes = "application/json", produces = "application/json")
    public List<PlaceChangeRequest> findAllByStatus() {
        return requestService.findByStatusIn(ChangeRequestStatus.PENDING, ChangeRequestStatus.IN_PROGRESS);
    }

    @GetMapping(value = "/find-all-by-place", consumes = "application/json", produces = "application/json")
    public List<PlaceChangeRequest> findAllByPlace(@RequestParam(value = "placeId") Long placeId) {
        return requestService.findAllByPlaceId(placeId);
    }

    @PutMapping(value = "/status/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<HttpStatus> updateStatus(@RequestParam(value = "id") String id,
                                                   @RequestParam(value = "status") ChangeRequestStatus status,
                                                   @RequestParam(value = "type", required = false)
                                                           ChangeRequestType type) {
        PlaceChangeRequest request = requestService.findById(id);
//        TODO review this impl
        switch (status) {
            case APPROVED:
                if (type != null) {
                    switch (type) {
                        case PLACE_PAGE:
                            requestService.approvePlacePageChanges(request);
                            break;
                        case MENUS:
                            requestService.approvePlaceMenusChanges(request);
                            break;
                        case OFFERS:
                            requestService.approvePlaceOfferssChanges(request);
                            break;
                        case PHOTO_SHOOT:
                            request.setStatus(status);
                            break;
                    }
                }
                break;
            default:
                request.setStatus(status);
                requestService.update(request);
                break;
        }
        return ResponseEntity.ok().build();
    }

}