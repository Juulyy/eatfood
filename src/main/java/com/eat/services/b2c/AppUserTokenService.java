package com.eat.services.b2c;

import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.AppUserToken;
import com.eat.models.b2c.AppUserToken.TokenType;
import com.eat.repositories.sql.b2c.AppUserTokenRepository;
import com.eat.services.common.MailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AppUserTokenService {

    @Autowired
    private AppUserTokenRepository tokensRepository;

    @Autowired
    private MailSenderService mailService;

    public void createAndSendToken(AppUser appUser, TokenType tokenType) {
        AppUserToken userToken = new AppUserToken();
        userToken.setAppUser(appUser);
        userToken.setTokenType(tokenType);
        userToken.setToken(UUID.randomUUID().toString());
        userToken.setStartDate(LocalDate.now());
        userToken.setExpiryDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        tokensRepository.save(userToken);
        mailService.sendMail(appUser, userToken);
    }

    public AppUserToken findByToken(String token) {
        return tokensRepository.findByToken(token);
    }

    public AppUserToken findByUser(AppUser appUser) {
        return tokensRepository.findByUser(appUser);
    }

    public List<AppUserToken> findAll() {
        return tokensRepository.findAll();
    }

    public AppUserToken findById(Long id) {
        return tokensRepository.findOne(id);
    }

    public AppUserToken save(AppUserToken appUserToken) {
        return tokensRepository.save(appUserToken);
    }

    public AppUserToken update(AppUserToken appUserToken) {
        return tokensRepository.save(appUserToken);
    }

    public void delete(Long id) {
        tokensRepository.delete(id);
    }

    public void delete(AppUserToken appUserToken) {
        tokensRepository.delete(appUserToken);
    }

}