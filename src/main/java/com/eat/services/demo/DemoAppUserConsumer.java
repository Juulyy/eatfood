package com.eat.services.demo;

import com.eat.models.b2c.AppUser;

import java.util.function.Consumer;

@FunctionalInterface
public interface DemoAppUserConsumer extends Consumer<AppUser> {

    Consumer<AppUser> autoFieldsGeneratorAppUserConsumer = appUser -> {
        appUser.setFirstName("Alan");
        appUser.setLastName("Test");
        appUser.setGender(AppUser.Gender.MALE);
        appUser.setLogin("alanTest");
        appUser.setEmail("alan-test@paige.com");
        appUser.setPassword("dfW7ks43Rf*");
        appUser.getUserPreferences().setUser(appUser);
    };

    Consumer<AppUser> autoFieldsGeneratorCuratorConsumer = appUser -> {
        appUser.setFirstName("Alan Curator");
        appUser.setLastName("Test");
        appUser.setGender(AppUser.Gender.MALE);
        appUser.setLogin("alanCurator");
        appUser.setEmail("alan-curator@paige.com");
        appUser.setPassword("dfW7ks43Rf*");
        appUser.getUserPreferences().setUser(appUser);
    };

}