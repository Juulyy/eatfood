package com.eat.controllers.common;

import com.eat.factories.EntityFactory;
import com.eat.models.b2b.BusinessUser;
import com.eat.models.b2c.AppUser;
import com.eat.models.common.AuthoritiesConstants;
import com.eat.models.common.enums.RoleType;
import com.eat.models.common.enums.UserType;
import com.eat.services.b2c.AppUserService;
import com.eat.services.common.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/registration")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private EntityFactory entityFactory;

    @Autowired
    private AppUserService userService;

    @PostMapping(value = "/referral", consumes = "application/json", produces = "application/json")
    public AppUser referralRegistration(@RequestParam("userId") Long userId, @RequestParam("refId") String refId,
                                        @RequestBody String userRaw) {
        AppUser instance = entityFactory.createInstance(userRaw, AppUser.class);
        return registrationService.createReferralEatUser(instance, userId, refId);
    }

    @PostMapping(value = "/app-user", consumes = "application/json", produces = "application/json")
    public AppUser eatUserRegistration(@RequestBody String appUserRaw) {
        return registrationService.createAppUser(entityFactory.createInstance(appUserRaw, AppUser.class));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/business-admin", consumes = "application/json", produces = "application/json")
    public BusinessUser businessAdminRegistration(@RequestBody BusinessUser user) {
        return registrationService.createBusinessUser(user, RoleType.ROLE_BUSINESS_ADMIN);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/business-manager", consumes = "application/json", produces = "application/json")
    public BusinessUser businessManagerRegistration(@RequestBody BusinessUser user) {
        return registrationService.createBusinessUser(user, RoleType.ROLE_BUSINESS_MANAGER);
    }

    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.BUSINESS_MANAGER})
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUSINESS_MANAGER')")
    @PostMapping(value = "/business-user", consumes = "application/json", produces = "application/json")
    public BusinessUser businessUserRegistration(@RequestBody BusinessUser user) {
        return registrationService.createBusinessUser(user, RoleType.ROLE_BUSINESS_USER);
    }

    /*@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/admin", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> adminRegistration(@RequestBody AppUser appUser) {
        return registrationService.createSuperAdmin(appUser);
    }*/

    @GetMapping(value = "/check-email/admin", consumes = "application/json", produces = "application/json")
    public HttpStatus checkAdminEmailAvailability(@RequestParam("email") String email) {
        return registrationService.checkEmailAvailability(email, UserType.ADMIN);
    }

    @GetMapping(value = "/check-email/business", consumes = "application/json", produces = "application/json")
    public HttpStatus checkBusinessEmailAvailability(@RequestParam("email") String email) {
        return registrationService.checkEmailAvailability(email, UserType.BUSINESS);
    }

    @GetMapping(value = "/check-email/app", consumes = "application/json", produces = "application/json")
    public HttpStatus checkAppEmailAvailability(@RequestParam("email") String email) {
        return registrationService.checkEmailAvailability(email, UserType.APP);
    }

    @GetMapping(value = "/confirm")
    public HttpStatus processRegistrationConfirmation(@RequestParam String token) {
        userService.registrationConfirm(token);
        return HttpStatus.OK;
    }

}
