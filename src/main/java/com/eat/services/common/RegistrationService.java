package com.eat.services.common;

import com.eat.controllers.exceptions.DataNotFoundException;
import com.eat.models.b2b.BusinessUser;
import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.AppUserToken;
import com.eat.models.b2c.DefaultResourcesConstants;
import com.eat.models.b2c.Invite;
import com.eat.models.common.enums.RoleType;
import com.eat.models.common.enums.UserType;
import com.eat.services.b2b.BusinessUserService;
import com.eat.services.b2c.*;
import com.eat.utils.security.PasswordEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegistrationService implements PasswordEncryptor {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private BusinessUserService businessUserService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppUserTokenService tokenService;

    @Autowired
    private AppUserSettingsService settingsService;

    @Autowired
    private InviteService inviteService;

    @Autowired
    private AppUserPreferenceService preferenceService;

    public AppUser setBasicAppUserSettings(AppUser user) {
        user.setPassword(encryptPassword(user.getPassword(), (BCryptPasswordEncoder) passwordEncoder));
        user.setEnabled(false);
        if (user.getPhotoUrl() == null) {
            user.setPhotoUrl(DefaultResourcesConstants.DEFAULT_PHOTO_URL);
        }
        return user;
    }

    public AppUser createReferralEatUser(AppUser user, Long referrerUserId, String refId) {
        AppUser referrer = appUserService.findById(referrerUserId);
        if (referrer != null) {
            Invite invite = inviteService.findByUserAndRefId(referrer, refId);
            if (invite != null) {
                user.setReferrer(referrer);
                return createAppUser(user);
            }
//            TODO add referralNotFoundException
        }
        throw new DataNotFoundException(referrerUserId);
    }

    public AppUser createAppUser(AppUser user) {
        AppUser appUser = setBasicAppUserSettings(user);
        appUser.setRole(roleService.getRoleByRoleType(RoleType.ROLE_APP_USER));
        appUserService.save(appUser);
        preferenceService.createDefaultPreference(appUser);
        settingsService.createDefaultSettings(appUser);
        tokenService.createAndSendToken(appUser, AppUserToken.TokenType.REGISTRATION_TOKEN);
        return appUser;
    }

    public BusinessUser createBusinessUser(BusinessUser businessUser, RoleType roleType) {
        switch (roleType) {
            case ROLE_BUSINESS_ADMIN:
                businessUser.setRole(roleService.getRoleByRoleType(RoleType.ROLE_BUSINESS_ADMIN));
                break;
            case ROLE_BUSINESS_MANAGER:
                businessUser.setRole(roleService.getRoleByRoleType(RoleType.ROLE_BUSINESS_MANAGER));
                break;
            case ROLE_BUSINESS_USER:
                businessUser.setRole(roleService.getRoleByRoleType(RoleType.ROLE_BUSINESS_USER));
                break;
        }
        businessUserService.save(businessUser);
        return businessUser;
    }

    /*public BusinessUser createSuperAdmin() {
        AppUser administrator = setBasicAppUserSettings();
        administrator.setRole(roleService.getSuperAdminRole());
        appUserService.save(administrator);
//        tokensService.createAndSendToken(user, TokenType.REGISTRATION_TOKEN);
        return administrator;
    }*/

    public HttpStatus checkEmailAvailability(String email, UserType userType) {
        Object object = null;
        switch (userType) {
            case ADMIN:
//                object = adminService.findByEmail(email);
                break;
            case BUSINESS:
                object = businessUserService.findByEmail(email);
                break;
            case APP:
                object = appUserService.findByEmail(email);
                break;
        }
        if (object == null) {
            return HttpStatus.ACCEPTED;
        }
        return HttpStatus.CONFLICT;
    }

}