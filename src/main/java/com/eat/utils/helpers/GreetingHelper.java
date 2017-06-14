package com.eat.utils.helpers;

import com.eat.models.b2c.AppUserGreeting;
import com.eat.models.b2c.AppUserGreetingContextSpecific;
import com.eat.models.b2c.AppUserGreetingTimePeriod;
import com.eat.models.b2c.enums.AppUserGreetingType;
import com.eat.models.b2c.localization.LocalizedGreeting;
import jdk.nashorn.internal.objects.annotations.Getter;

import java.time.LocalTime;
import java.util.*;

public class GreetingHelper {

    @Getter
    public static List<AppUserGreeting> getContextGreetings() {

        List<AppUserGreeting> greetings = new ArrayList<>();

        Set<AppUserGreetingTimePeriod> times = new HashSet<>();
        times.add(AppUserGreetingTimePeriod.of()
                .fromTime(LocalTime.of(6, 0))
                .toTime(LocalTime.of(10, 59))
                .create());

        Map<Locale, LocalizedGreeting> context1 = new HashMap<>();
        context1.put(Locale.ENGLISH, LocalizedGreeting.of()
                .locale(Locale.ENGLISH)
                .greeting("Remember, a good brekfast fuels you up and gets your ready for the day.")
                .create());

        context1.put(Locale.ENGLISH, LocalizedGreeting.of()
                .locale(Locale.ENGLISH)
                .greeting("Every good morning starts with a cup of fresh coffee.")
                .create());

        AppUserGreetingContextSpecific specific1 = AppUserGreetingContextSpecific.of().create();

        AppUserGreeting appUserGreeting1 = AppUserGreeting.of()
                .name("Contextual #1")
                .type(AppUserGreetingType.CONTEXT_SPECIFIC)
                .greetingText(context1)
                .isDatePeriod(false)
                .dates(null)
                .isActive(true)
                .isTimePeriod(true)
                .specificOption(specific1)
                .isContextSpecific(true)
                .times(times)
                .create();

        appUserGreeting1.setSpecificOption(specific1);

        greetings.add(appUserGreeting1);

        Set<AppUserGreetingTimePeriod> times2 = new HashSet<>();
        times2.add(AppUserGreetingTimePeriod.of()
                .fromTime(LocalTime.of(11, 0))
                .toTime(LocalTime.of(13, 59))
                .create());

        Map<Locale, LocalizedGreeting> context2 = new HashMap<>();
        context2.put(Locale.ENGLISH, LocalizedGreeting.of()
                .locale(Locale.ENGLISH)
                .greeting("Hold on! Whatever you do, stop it. A good lunch is what you need right now.")
                .create());

        context2.put(Locale.ENGLISH, LocalizedGreeting.of()
                .locale(Locale.ENGLISH)
                .greeting("Did you forget why you're hungry? Well, it's lunch time.")
                .create());

        AppUserGreetingContextSpecific specific2 = AppUserGreetingContextSpecific.of().create();

        AppUserGreeting appUserGreeting2 = AppUserGreeting.of()
                .name("Contextual #2")
                .type(AppUserGreetingType.CONTEXT_SPECIFIC)
                .greetingText(context2)
                .isDatePeriod(false)
                .dates(null)
                .isActive(true)
                .isTimePeriod(true)
                .specificOption(specific2)
                .isContextSpecific(true)
                .times(times2)
                .create();

        appUserGreeting2.setSpecificOption(specific2);

        greetings.add(appUserGreeting2);

        Set<AppUserGreetingTimePeriod> times3 = new HashSet<>();
        times3.add(AppUserGreetingTimePeriod.of()
                .fromTime(LocalTime.of(14, 0))
                .toTime(LocalTime.of(17, 59))
                .create());

        Map<Locale, LocalizedGreeting> context3 = new HashMap<>();
        context3.put(Locale.ENGLISH, LocalizedGreeting.of()
                .locale(Locale.ENGLISH)
                .greeting("It's a snack time! Here's a selecetion of the best food around you.")
                .create());

        context3.put(Locale.ENGLISH, LocalizedGreeting.of()
                .locale(Locale.ENGLISH)
                .greeting("What time is it? You're right, it's time to eat.")
                .create());

        AppUserGreetingContextSpecific specific3 = AppUserGreetingContextSpecific.of().create();

        AppUserGreeting appUserGreeting3 = AppUserGreeting.of()
                .name("Contextual #3")
                .type(AppUserGreetingType.CONTEXT_SPECIFIC)
                .greetingText(context3)
                .isDatePeriod(false)
                .dates(null)
                .isActive(true)
                .isTimePeriod(true)
                .isContextSpecific(true)
                .specificOption(specific3)
                .times(times3)
                .create();

        appUserGreeting3.setSpecificOption(specific3);

        greetings.add(appUserGreeting3);

        Set<AppUserGreetingTimePeriod> times4 = new HashSet<>();
        times4.add(AppUserGreetingTimePeriod.of()
                .fromTime(LocalTime.of(18, 0))
                .toTime(LocalTime.of(2, 0))
                .create());

        Map<Locale, LocalizedGreeting> context4 = new HashMap<>();
        context4.put(Locale.ENGLISH, LocalizedGreeting.of()
                .locale(Locale.ENGLISH)
                .greeting("It's raining outside. Don't forget to take an umbrella, when heading for dinner.")
                .create());

        context4.put(Locale.ENGLISH, LocalizedGreeting.of()
                .locale(Locale.ENGLISH)
                .greeting("Cooking is overated. Try these amaing dishes in the neighborhood.")
                .create());

        context4.put(Locale.ENGLISH, LocalizedGreeting.of()
                .locale(Locale.ENGLISH)
                .greeting("People who love food are always the best people.")
                .create());

        AppUserGreetingContextSpecific specific4 = AppUserGreetingContextSpecific.of().create();

        AppUserGreeting appUserGreeting4 = AppUserGreeting.of()
                .name("Contextual #4")
                .type(AppUserGreetingType.CONTEXT_SPECIFIC)
                .greetingText(context4)
                .isDatePeriod(false)
                .dates(null)
                .isActive(true)
                .isTimePeriod(true)
                .isContextSpecific(true)
                .specificOption(specific4)
                .times(times4)
                .create();

        appUserGreeting4.setSpecificOption(specific4);

        greetings.add(appUserGreeting4);

        Set<AppUserGreetingTimePeriod> times5 = new HashSet<>();
        times5.add(AppUserGreetingTimePeriod.of()
                .fromTime(LocalTime.of(6, 0))
                .toTime(LocalTime.of(10, 59))
                .create());

        Map<Locale, LocalizedGreeting> context5 = new HashMap<>();
        context5.put(Locale.ENGLISH, LocalizedGreeting.of()
                .locale(Locale.ENGLISH)
                .greeting("Brrr, it's freezing outside. Get a hot beverage to warm up.")
                .create());

        AppUserGreetingContextSpecific specific5 = AppUserGreetingContextSpecific.of().create();
        specific5.setUseWeatherFilter(true);

        AppUserGreeting appUserGreeting5 = AppUserGreeting.of()
                .name("Contextual #4")
                .type(AppUserGreetingType.CONTEXT_SPECIFIC)
                .greetingText(context5)
                .isDatePeriod(false)
                .dates(null)
                .isActive(true)
                .isTimePeriod(true)
                .specificOption(specific5)
                .isContextSpecific(true)
                .times(times5)
                .create();

        appUserGreeting5.setSpecificOption(specific5);

        greetings.add(appUserGreeting5);

        return greetings;
    }

}