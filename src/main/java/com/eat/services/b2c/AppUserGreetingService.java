package com.eat.services.b2c;

import com.eat.controllers.exceptions.RemoveActionDeniedException;
import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.AppUserGreeting;
import com.eat.models.b2c.enums.AppUserGreetingType;
import com.eat.models.b2c.localization.LocalizedGreeting;
import com.eat.repositories.sql.b2c.AppUserGreetingRepository;
import com.eat.repositories.sql.b2c.AppUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Service
public class AppUserGreetingService {

    @Autowired
    private AppUserGreetingRepository repository;

    @Autowired
    private AppUserRepository userRepository;

    @PostConstruct
    private void initialize() {
        try {

            AppUserGreeting defaultGreeting = findSystemGreetingByType(AppUserGreetingType.GREETING_DEFAULT);
            if (defaultGreeting == null) {

                Map<Locale, LocalizedGreeting> defaultGreetingText = new HashMap<>();
                defaultGreetingText.put(Locale.ENGLISH, LocalizedGreeting.of()
                        .locale(Locale.ENGLISH)
                        .greeting("Good to see you, #user_name!")
                        .create());

                save(AppUserGreeting.of()
                        .name("Default greeting")
                        .isActive(true)
                        .type(AppUserGreetingType.GREETING_DEFAULT)
                        .greetingText(defaultGreetingText)
                        .create());

            }

            AppUserGreeting defaultContext = findSystemGreetingByType(AppUserGreetingType.CONTEXT_DEFAULT);
            if (defaultContext == null) {

                Map<Locale, LocalizedGreeting> defaultGreetingText = new HashMap<>();
                defaultGreetingText.put(Locale.ENGLISH, LocalizedGreeting.of()
                        .locale(Locale.ENGLISH)
                        .greeting("What time is it? You're right, it's time to eat.")
                        .create());

                save(AppUserGreeting.of()
                        .name("Default contextual")
                        .isActive(true)
                        .type(AppUserGreetingType.CONTEXT_DEFAULT)
                        .isContextSpecific(true)
                        .greetingText(defaultGreetingText)
                        .create());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public List<AppUserGreeting> findAll() {
        return repository.findAll();
    }

    public AppUserGreeting findById(Long id) {
        return repository.findOne(id);
    }

    public AppUserGreeting save(AppUserGreeting greeting) {
        updateRate.accept(greeting);
        return repository.save(greeting);
    }

    public void saveAll(List<AppUserGreeting> greetings) {
        repository.save(greetings);
    }

    private Consumer<AppUserGreeting> updateRate = greeting -> {
        greeting.setRate(0D);
        if (greeting.isDatePeriod()) {
            greeting.setRate(greeting.getRate() + 1);
        }

        if (greeting.isTimePeriod()) {
            greeting.setRate(greeting.getRate() + 1);
        }
    };

    public void delete(Long id) {
        AppUserGreeting greeting = findById(id);
        if (greeting.getType().equals(AppUserGreetingType.GREETING_DEFAULT) ||
                greeting.getType().equals(AppUserGreetingType.CONTEXT_DEFAULT)) {
            throw new RemoveActionDeniedException();
        }
        repository.delete(id);
    }

    public AppUserGreeting update(AppUserGreeting greeting) {
        updateRate.accept(greeting);
        return repository.save(greeting);
    }

    public AppUserGreeting findSystemGreetingByType(AppUserGreetingType type) {
        return repository.findSystemGreetingByType(type);
    }

    public List<AppUserGreeting> getGreetingByType(AppUserGreetingType greetingType) {
        return repository.findGreetingByType(greetingType);
    }

    public List<AppUserGreeting> getGreetingByTypeAndTime(AppUserGreetingType greetingType, LocalTime time) {
        return repository.findGreetingByTypeAndByTime(greetingType, time);
    }

    public List<AppUserGreeting> getGreetingByTypeAndTimeAndDate(AppUserGreetingType greetingType, LocalTime time, LocalDate date) {
        return repository.findGreetingByTypeAndTimeAndDate(greetingType, time, date);
    }

    public List<AppUserGreeting> getContextSpecificGreeting(Long userId, LocalTime time, LocalDate date) {
        AppUser user = userRepository.findOne(userId);
        return null;
        //return repository.findGreetingByTypeAndTimeAndDate(greetingType, time, date);
    }

}