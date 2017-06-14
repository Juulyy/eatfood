package com.eat.services.b2c;

import com.eat.controllers.exceptions.DataNotFoundException;
import com.eat.models.b2b.Offer;
import com.eat.models.b2b.Place;
import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.Plan;
import com.eat.models.b2c.enums.PrivacyType;
import com.eat.repositories.sql.b2c.PlanRepository;
import com.eat.services.b2b.OfferService;
import com.eat.services.b2b.PlaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PlanService {

    @Autowired
    private PlanRepository repository;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private AppUserService userService;

    @Autowired
    private OfferService offerService;

    public void addNotFollowingUser(Long organizerId, Long userId, Long planId) {
        AppUser organizer = userService.findById(organizerId);
        AppUser user = userService.findById(userId);
        Plan plan = findById(planId);
        if (organizer != null && user != null && plan != null
                && plan.getOrganizer().equals(organizer)
                && user.getSettings().getPlanInvitation().equals(PrivacyType.EVERYONE)) {
            plan.getParticipants().add(user);
            update(plan);
        }
    }

    public Plan createFromPlace(Plan plan, Long userId, Long placeId, List<Long> participantsIds) {
        Place place = placeService.findById(placeId);
        if (place != null) {
            Plan persistedPlan = create(plan, userId, participantsIds);
            persistedPlan.setPlace(place);
            save(persistedPlan);
            return persistedPlan;
        }
        return null;
    }

    public Plan createFromOffer(Plan plan, Long userId, Long offerId, List<Long> participantsIds) {
        Offer offer = offerService.findById(offerId);
        if (offer != null) {
            Plan persistedPlan = create(plan, userId, participantsIds);
            persistedPlan.setOffer(offer);
            save(persistedPlan);
            return persistedPlan;
        }
        return null;
    }

    public Plan create(Plan plan, Long userId, List<Long> participantsIds) {
        AppUser organizer = userService.findById(userId);
        if (organizer != null) {
            ArrayList<AppUser> participants = new ArrayList<>();
            participantsIds.forEach(id -> participants.add(userService.findById(id)));
            plan.setOrganizer(organizer);
            plan.setParticipants(participants);
            return plan;
        }
//        TODO add exception
        return null;
    }

    public void delegateOrganizer(Long userId, Long newOrgId, Long planId) {
        AppUser currentOrganizer = userService.findById(userId);
        AppUser newOrganizer = userService.findById(newOrgId);
        Plan plan = findById(planId);
        if (currentOrganizer != null && newOrganizer != null
                && plan != null && plan.getOrganizer().equals(currentOrganizer)) {
//            TODO return push notification
        }
    }

    public void setNewOrganizer(Long userId, Long newOrgId, Long planId, boolean status) {
        AppUser currentOrganizer = userService.findById(userId);
        AppUser newOrganizer = userService.findById(newOrgId);
        Plan plan = findById(planId);
        if (currentOrganizer != null && newOrganizer != null
                && plan != null && plan.getOrganizer().equals(currentOrganizer) && status) {
            plan.setOrganizer(newOrganizer);
            plan.getParticipants().add(currentOrganizer);
            save(plan);
//                TODO return push notification
        }
    }

    public Plan findById(Long id) {
        return repository.findOne(id);
    }

    public Plan save(Plan plan) {
        return repository.save(plan);
    }

    public Plan update(Plan plan) {
        return repository.save(plan);
    }

    public void delete(Plan plan) {
        repository.delete(plan);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public List<Plan> findAll() {
        return repository.findAll();
    }

    public List<Plan> findAllByPlace(Long placeId) {
        Place place = placeService.findById(placeId);
        if (place != null) {
            return repository.findAllByPlace(place);
        }
        return null;
    }

    public List<Plan> findAllByUser(Long userId) {
        AppUser user = userService.findById(userId);
        if (user != null) {
            return repository.findAllByUser(user);
        }
        throw new DataNotFoundException(userId);
    }

}