package com.eat.models.mongo.enums;

import lombok.Getter;

import java.util.EnumSet;

public enum WeatherIcon {

    /**
     * may be defined in the future: hail, thunderstorm, tornado
     */
    CLEAR_DAY("clear-day"), CLEAR_NIGHT("clear-night"), RAIN("rain"), SNOW("snow"), SLEET("sleet"), WIND("wind"),
    FOG("fog"), CLOUDY("cloudy"), PARTLY_CLOUDY_DAY("partly-cloudy-day"), PARTLY_CLOUDY_NIGHT("partly-cloudy-night"),
    UNKNOWN("unknown");

    @Getter
    private String icon;

    WeatherIcon(String icon) {
        this.icon = icon;
    }

    @Getter
    private static final EnumSet<WeatherIcon> icons = EnumSet.allOf(WeatherIcon.class);

}