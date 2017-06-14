package com.eat.logic.b2c;

import com.eat.models.b2c.*;
import com.eat.models.b2c.enums.AppUserGreetingType;
import com.eat.models.common.Tag;
import com.eat.models.mongo.enums.WeatherIcon;
import com.eat.services.b2c.AppUserGreetingService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.eat.models.b2c.enums.AppUserGreetingType.*;

@Slf4j
@Component
public class AppUserGreetingPool {

    @Autowired
    private AppUserGreetingService greetingService;

    /*@Autowired
    private BehaviourPoolService poolService;

    @Autowired
    private AppUserService userService;*/

    @Autowired
    private WeatherFilter weatherFilter;

    @Getter
    private List<AppUserGreeting> greetingsPool;

    @Getter
    private List<AppUserGreeting> specificContextPool;

    public void updatePoll() {
        initialize();
    }

    @PostConstruct
    private void initialize() {
        try {
            this.greetingsPool = new ArrayList<>();
            this.specificContextPool = new ArrayList<>();

            List<AppUserGreeting> persistedGreetings = greetingService.findAll();

            if (!persistedGreetings.isEmpty()) {
                persistedGreetings.forEach(greeting -> {
                    if (greeting.isContextSpecific()) {
                        this.getSpecificContextPool().add(greeting);
                    } else {
                        this.getGreetingsPool().add(greeting);
                    }
                });

                Collections.sort(this.greetingsPool);
                Collections.sort(this.specificContextPool);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /*public AppUserGreeting getGreeting(Long userId, LocalTime time) {
        AppUser appUser = userService.findById(userId);
        AppUserGreeting greeting = findGreeting(appUser, time);
        return setUserName(greeting, appUser.getFirstName());
    }*/

    public AppUserGreeting getGreeting(AppUser appUser, LocalTime time) {
        return findGreeting(appUser, time);
    }

    private AppUserGreeting findGreeting(AppUser appUser, LocalTime time) {
        AppUserGreeting result;

        /*if (isBirthday(appUser.getBirthDate())) {
            return getGreetingByType(BIRTHDAY);
        }*/

        /*if (isReturn(appUser.getLastLogin())) {
            long betweenDays = ChronoUnit.DAYS.between(LocalDate.now(), appUser.getLastLogin());
            if (betweenDays <= 7) {
                return getGreetingByType(RETURN_AFTER_7);
            } else if (betweenDays <= 14) {
                return getGreetingByType(RETURN_AFTER_14);
            } else {
                return getGreetingByType(RETURN_AFTER_30);
            }
        }*/

        result = findInGreetingPool(getGreetingsPool().stream()
                .filter(greeting -> greeting.getType().equals(SPECIFIC))
                .collect(Collectors.toList()), time);

        if (result != null) {
            return result;
        }

        result = findInGreetingPool(getGreetingsPool().stream()
                .filter(greeting -> greeting.getType().equals(GENERAL))
                .collect(Collectors.toList()), time);

        if (result != null) {
            return result;
        }

        return getGreetingByType(this.getGreetingsPool(), GREETING_DEFAULT);
    }

    public AppUserGreeting getContextSpecificForDemo(AppUser appUser, LocalTime userTime, Double temp, WeatherIcon icon) {
        /*AppUserGreeting result;
        List<AppUserGreetingContextSpecific> specWithOutTags;

        Set<Tag> userTags = appUser.getUserPreferences().getTasteTags();*/

        List<AppUserGreeting> ctxSpecMessages = findActiveAndDateTimeRelevantGreetingsForDemo(getSpecificContextPool(), userTime);

        List<AppUserGreetingContextSpecific> relevantSpecifics = ctxSpecMessages.stream()
                .map(AppUserGreeting::getSpecificOption)
                .filter(specific -> isRelevantWeatherForDemo(specific, temp, icon))
                .collect(Collectors.toList());

        /*if (!relevantSpecifics.isEmpty()) {
            specWithOutTags = relevantSpecifics.stream()
                    .filter(ctxSpec -> ctxSpec.getTags().isEmpty())
                    .collect(Collectors.toList());

            if (!specWithOutTags.isEmpty()) {
                relevantSpecifics.removeAll(specWithOutTags);

                if (!relevantSpecifics.isEmpty()) {
                    result = findTopContextSpecific(userTags, relevantSpecifics);
                    if (result != null) {
                        return result;
                    }
                } else {
                    return specWithOutTags.get(RandomUtils.nextInt(0, specWithOutTags.size())).getAppUserGreeting();
                }
            }
        }*/

        if (!relevantSpecifics.isEmpty()) {
            if (temp == null || icon == null) {
                relevantSpecifics = relevantSpecifics.stream()
                        .filter(specific -> !specific.isUseWeatherFilter())
                        .collect(Collectors.toList());
                if (!relevantSpecifics.isEmpty()) {
                    return relevantSpecifics.get(RandomUtils.nextInt(0, relevantSpecifics.size())).getAppUserGreeting();
                }
            } else {
                List<AppUserGreetingContextSpecific> greetingsWithWeather = relevantSpecifics.stream()
                        .filter(AppUserGreetingContextSpecific::isUseWeatherFilter)
                        .collect(Collectors.toList());
                if (!greetingsWithWeather.isEmpty()) {
                    return greetingsWithWeather.get(RandomUtils.nextInt(0, greetingsWithWeather.size())).getAppUserGreeting();
                } else {
                    return relevantSpecifics.get(RandomUtils.nextInt(0, relevantSpecifics.size())).getAppUserGreeting();
                }
            }
        }
        return getGreetingByType(this.getSpecificContextPool(), AppUserGreetingType.CONTEXT_DEFAULT);
    }

    /*public AppUserGreeting getContextSpecific(Long userId) {
        AppUserGreeting result;
        List<AppUserGreetingContextSpecific> specWithOutTags;

        Set<Tag> userTags = poolService.findTopUserTags(userId);

        List<AppUserGreeting> ctxSpecMessages = findActiveAndDateTimeRelevantGreetings(getSpecificContextPool());

        List<AppUserGreetingContextSpecific> relevantSpecifics = ctxSpecMessages.stream()
                .map(AppUserGreeting::getSpecificOption)
                .filter(ctxSpec -> isRelevantDayAndTime(ctxSpec.getPeriods()))
                .filter(this::isRelevantWeather)
                .collect(Collectors.toList());

        if (!relevantSpecifics.isEmpty()) {
            specWithOutTags = relevantSpecifics.stream()
                    .filter(ctxSpec -> ctxSpec.getTags().isEmpty())
                    .collect(Collectors.toList());

            if (!specWithOutTags.isEmpty()) {
                relevantSpecifics.removeAll(specWithOutTags);

                if (!relevantSpecifics.isEmpty()) {
                    result = findTopContextSpecific(userTags, relevantSpecifics);
                    if (result != null) {
                        return result;
                    }
                } else {
                    return specWithOutTags.get(RandomUtils.nextInt(0, specWithOutTags.size() - 1)).getAppUserGreeting();
                }
            }
        }

        return getGreetingByType(AppUserGreetingType.CONTEXT_DEFAULT);
    }*/

    private AppUserGreeting findTopContextSpecific(Set<Tag> userTags, List<AppUserGreetingContextSpecific> specifics) {
        List<AppUserGreetingContextSpecific> result = new ArrayList<>();

        specifics.forEach(ctxSpec -> ctxSpec.getTags().forEach(tag -> {
            if (userTags.contains(tag)) {
                ctxSpec.setRate(ctxSpec.getRate() + 1);
            }
        }));

        if (!result.isEmpty()) {
            Collections.sort(result);
            return result.get(0).getAppUserGreeting();
        }

        return null;
    }

    private boolean isRelevantWeatherForDemo(AppUserGreetingContextSpecific specific, Double userTemp, WeatherIcon userIcon) {
        return weatherFilter.filterGreeting(specific, userTemp, userIcon);
    }

    /*private boolean isRelevantWeather(AppUserGreetingContextSpecific specific) {
        return weatherFilter.filterGreeting(specific);
    }*/

    /*private boolean isRelevantDayAndTime(Set<DayTimePeriod> periods) {
        return periods.stream()
                .anyMatch(ctxSpecDayOfWeekEqualsForNow
                        .and(ctxSpecFromTimeLessForNow)
                        .and(ctxSpecToTimeGreaterForNow));
    }

    private Predicate<DayTimePeriod> ctxSpecDayOfWeekEqualsForNow = period ->
            LocalDate.now().getDayOfWeek().equals(period.getDayOfWeek());

    private Predicate<DayTimePeriod> ctxSpecFromTimeLessForNow = period ->
            ChronoUnit.HOURS.between(LocalTime.now(), period.getFromTime()) >= 0;

    private Predicate<DayTimePeriod> ctxSpecToTimeGreaterForNow = period ->
            ChronoUnit.HOURS.between(period.getToTime(), LocalTime.now()) >= 0;*/

    private List<AppUserGreeting> findActiveAndDateTimeRelevantGreetingsForDemo(List<AppUserGreeting> pool, LocalTime time) {
        return pool.stream()
                .filter(AppUserGreeting::isActive)
                .filter(greeting -> isCorrectTime(greeting.getTimes(), time))
                .collect(Collectors.toList());
    }

    private boolean isCorrectTime(Set<AppUserGreetingTimePeriod> timePeriods, LocalTime userTime) {

/*        for(AppUserGreetingTimePeriod period : timePeriods) {
            if (greetingFromTimeLessForCurrentPredicate(userTime)
                    .and(greetingToTimeGreaterForCurrentPredicate(userTime))
                    .test(period)) {
                return true;
            }
        }

        return false;*/

        return timePeriods.stream().anyMatch(greetingFromTimeLessForCurrentPredicate(userTime)
                .and(greetingToTimeGreaterForCurrentPredicate(userTime)));
    }

    private Predicate<AppUserGreetingTimePeriod> greetingFromTimeLessForCurrentPredicate(LocalTime userTime) {
        return time -> ChronoUnit.NANOS.between(time.getFromTime(), userTime) >= 0;
    }

    private Predicate<AppUserGreetingTimePeriod> greetingToTimeGreaterForCurrentPredicate(LocalTime userTime) {
        return time -> ChronoUnit.NANOS.between(userTime, time.getToTime()) >= 0;
    }

    private List<AppUserGreeting> findActiveAndDateTimeRelevantGreetings(List<AppUserGreeting> pool) {
        return pool.stream()
                .filter(AppUserGreeting::isActive)
                .filter(greeting -> isRelevantDate(greeting.getDates()))
                .filter(greeting -> isRelevantTime(greeting.getTimes()))
                .collect(Collectors.toList());
    }

    private AppUserGreeting findInGreetingPool(List<AppUserGreeting> pool, LocalTime time) {
        List<AppUserGreeting> greetings = findActiveAndDateTimeRelevantGreetingsForDemo(pool, time);

        if (!greetings.isEmpty()) {
            return greetings.get(RandomUtils.nextInt(0, greetings.size()));
        }

        return null;
    }

    private boolean isRelevantTime(Set<AppUserGreetingTimePeriod> times) {
        return times.stream().anyMatch(time ->
                greetingFromTimeLessForCurrent.and(greetingToTimeGreaterForCurrent).test(time));
    }

    private Predicate<AppUserGreetingTimePeriod> greetingFromTimeLessForCurrent = time ->
            ChronoUnit.HOURS.between(LocalTime.now(), time.getFromTime()) >= 0;

    private Predicate<AppUserGreetingTimePeriod> greetingToTimeGreaterForCurrent = time ->
            ChronoUnit.HOURS.between(time.getFromTime(), LocalTime.now()) >= 0;

    private boolean isRelevantDate(Set<AppUserGreetingDatePeriod> periods) {
        return periods.stream().anyMatch(period ->
                greetingFromDateLessForCurrent.and(greetingToDateGreaterForCurrent).test(period));
    }

    private Predicate<AppUserGreetingDatePeriod> greetingFromDateLessForCurrent = date ->
            ChronoUnit.DAYS.between(LocalDate.now(), date.getFromDate()) >= 0;

    private Predicate<AppUserGreetingDatePeriod> greetingToDateGreaterForCurrent = date ->
            ChronoUnit.DAYS.between(date.getToDate(), LocalDate.now()) >= 0;

    private boolean isBirthday(LocalDate birthdayDate) {
        return LocalDate.now().equals(birthdayDate);
    }

    private boolean isReturn(LocalDate lastLoginDate) {
        return ChronoUnit.DAYS.between(LocalDate.now(), lastLoginDate) >= 7;
    }

    private AppUserGreeting getGreetingByType(List<AppUserGreeting> persistedGreetings, AppUserGreetingType greetingType) {
        List<AppUserGreeting> greetings = persistedGreetings.stream()
                .filter(greeting -> greeting.getType().equals(greetingType))
                .collect(Collectors.toList());

        if (!greetings.isEmpty()) {
            if (greetings.size() == 1) {
                return greetings.get(0);
            }
            return greetings.get(RandomUtils.nextInt(0, greetings.size()));
        }

        return null;
    }

}