package com.eat.services.b2c;

import com.eat.controllers.exceptions.DataNotFoundException;
import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.AppUserToken;
import com.eat.models.b2c.AppUserToken.TokenType;
import com.eat.models.b2c.DefaultResourcesConstants;
import com.eat.models.common.Tag;
import com.eat.models.common.enums.CrudMethod;
import com.eat.repositories.sql.b2c.AppUserRepository;
import com.eat.repositories.sql.common.TagRepository;
import com.eat.services.common.MailSenderService;
import com.eat.services.common.RoleService;
import com.eat.utils.security.PasswordEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class AppUserService implements PasswordEncryptor {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppUserTokenService tokensService;
    @Autowired
    private MailSenderService mailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private RoleService roleService;


    public void manageCuratorBadge(AppUser user, CrudMethod crudMethod) {
//        TODO add impl of setting/removing curator's badge
        switch (crudMethod) {
            case CREATE:
//                user.setCuratorBadge();
                break;
            case DELETE:
//                user.removeCuratorBadge();
                break;
        }
    }

    public void manageUserPhoto(Long userId, String photoUrl, CrudMethod crudMethod) {
        AppUser user = findById(userId);
        if (user != null) {
            switch (crudMethod) {
                case CREATE:
                    user.setPhotoUrl(photoUrl);
                    break;
                case DELETE:
                    user.setPhotoUrl(DefaultResourcesConstants.DEFAULT_PHOTO_URL);
                    break;
            }
            update(user);
        }
        throw new DataNotFoundException();
    }

    public void resetPassword(Long userId) {
        AppUser appUser = findById(userId);
        if (appUser != null) {
            AppUserToken appUserToken = tokensService.findByUser(appUser);
            if (appUserToken == null) {
                tokensService.createAndSendToken(appUser, TokenType.RESET_PASSWORD_TOKEN);
            } else {
                appUserToken.setStartDate(LocalDate.now());
                appUserToken.setExpiryDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
                appUserToken.setTokenType(TokenType.RESET_PASSWORD_TOKEN);
                tokensService.save(appUserToken);
                mailService.sendMail(appUser, appUserToken);
            }
        }
        throw new DataNotFoundException();
    }

    public void registrationConfirm(String token) {
        AppUserToken appUserToken = tokensService.findByToken(token);
        if (appUserToken != null && isValidTokenDates(appUserToken)) {
            AppUser appUser = appUserToken.getAppUser();
            appUser.setEnabled(true);
            save(appUser);
//            TODO add email info about successfully confirmation
        }
//        TODO throw exception
    }

    private boolean isValidTokenDates(AppUserToken appUserToken) {
        return (LocalDate.now().equals(appUserToken.getStartDate()) || LocalDate.now().equals(appUserToken.getExpiryDate()));
    }

    public void changePassword(Long userId, String token, String pass) {
        AppUser appUser = findById(userId);
        AppUserToken appUserToken = tokensService.findByUser(appUser);
        if (appUser != null && appUserToken != null && isValidTokenDates(appUserToken)) {
            appUser.setPassword(passwordEncoder.encode(pass));
            update(appUser);
        }
        throw new DataNotFoundException();
    }

    public AppUser getUserByLoginOrEmail(String authParam) {
        AppUser appUser;
        if (authParam.contains("@")) {
            appUser = appUserRepository.findByEmail(authParam);
        } else {
            appUser = appUserRepository.findByLogin(authParam);
        }
        return appUser;
    }

    public void processTokenResending(Map<String, String> emailData) {
        AppUser appUser = appUserRepository.findByEmail(emailData.get("email"));
        AppUserToken appUserToken = tokensService.findByUser(appUser);
        if (appUserToken != null) {
            appUserToken.setToken(getNewTokenCode());
            appUserToken.setTokenType(TokenType.REGISTRATION_TOKEN);
            appUserToken.setStartDate(LocalDate.now());
            appUserToken.setExpiryDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
            mailService.sendMail(appUser, appUserToken);
        } else {
            tokensService.createAndSendToken(appUser, TokenType.REGISTRATION_TOKEN);
        }
    }

    private String getNewTokenCode() {
        return UUID.randomUUID().toString();
    }

    public boolean isEmailExists(String email) {
        AppUser appUser = appUserRepository.findByEmail(email);
        return appUser == null;
    }

    public boolean isLoginExists(String login) {
        AppUser appUser = appUserRepository.findByLogin(login);
        return appUser == null;
    }

    public boolean isValidPassword(String password, AppUser appUser) {
        if (isMatches(password, appUser.getPassword(), (BCryptPasswordEncoder) passwordEncoder)) {
            return true;
        }
        return false;
    }

    public List<AppUser> getMutualFollowers(Long userId) {
        return appUserRepository.findMutualFollowers(userId);
    }

    public void update(AppUser appUser) {
        appUserRepository.save(appUser);
    }

    public AppUser save(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    public boolean isExists(AppUser appUser) {
        return appUserRepository.exists(appUser.getId());
    }

    public AppUser findById(Long id) {
        return appUserRepository.findOne(id);
    }

    public List<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    public void delete(Long id) {
        appUserRepository.delete(id);
    }

    public AppUser findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public void updateAll(List<AppUser> users) {
        users.forEach(this::update);
    }

    public List<AppUser> saveAll(List<AppUser> users) {
        return appUserRepository.save(users);
    }

    private Set<Tag> convertToPersisted(Set<Tag> tasteTags) {
        Set<Tag> persistedUserTags = new HashSet<>();

        for (Tag tasteTag : tasteTags) {
            List<Tag> persistedTag = tagRepository.findByName(tasteTag.getName());

            if (persistedTag == null) {
                persistedTag = Arrays.asList(tagRepository.save(tasteTag));
            }
            persistedUserTags.addAll(persistedTag);
        }
        return persistedUserTags;
    }

}