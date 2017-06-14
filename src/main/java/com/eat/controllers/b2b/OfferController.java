package com.eat.controllers.b2b;

import com.eat.models.b2b.Offer;
import com.eat.models.common.AuthoritiesConstants;
import com.eat.services.b2b.OfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(value = "/api/offer")
public class OfferController {

    @Autowired
    private OfferService service;

    /**
     * GET secured method to get all offers
     *
     * @return http://localhost:8090/api/offer/find-all
     */
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BUSINESS_MANAGER, AuthoritiesConstants.BUSINESS_USER})
    @GetMapping(value = "/find-all")
    public Collection<Offer> getAll() {
        return service.getAll();
    }


    /**
     * POST secured method to add new offer for Establishment
     *
     * @param userId - Business user ID
     * @param placeId  - Establishment ID
     * @param offer  - Offer
     * @return http://localhost:8090/api/offer/add-for-estb
     */
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BUSINESS_MANAGER, AuthoritiesConstants.BUSINESS_USER})
    @PostMapping(value = "/add-for-place", produces = "application/json")
    public Offer addOfferForEstablishment(@RequestParam("userId") Long userId,
                                                           @RequestParam("placeId") Long placeId,
                                                           @RequestBody Offer offer) {
        return service.addOffer(userId, placeId, offer);
    }

    /**
     * DELETE secured method to delete offer
     *
     * @param userId - Business user ID
     * @param placeId  - Establishment ID
     * @return http://localhost:8090/api/offer/delete-from-place
     */
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BUSINESS_MANAGER, AuthoritiesConstants.BUSINESS_USER})
    @DeleteMapping(value = "/delete-from-place/{id}", produces = "application/json")
    public void deleteOfferFromEstablishment(@RequestParam("userId") Long userId,
                                                               @RequestParam("placeId") Long placeId,
                                                               @PathVariable("id") Long offerId) {
        service.deleteOffer(userId, placeId, offerId);
    }

    /**
     * PATCH secured method to update all offer's fields
     *
     * @param userId - Business user ID
     * @param placeId  - Establishment ID
     * @param offer  - Offer
     * @return http://localhost:8090/api/offer/update-for-place/2
     */
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BUSINESS_MANAGER, AuthoritiesConstants.BUSINESS_USER})
    @PatchMapping(value = "/update-for-place/{id}", produces = "application/json")
    public Offer updateOfferFromEstablishment(@RequestParam("userId") Long userId,
                                                               @RequestParam("placeId") Long placeId,
                                                               @RequestBody Offer offer) {
        return service.updateOffer(userId, placeId, offer);
    }

}
