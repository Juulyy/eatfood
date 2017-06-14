package com.eat.models.b2b.enums;

import lombok.Getter;

@Getter
public enum PlaceDetailType {

    CUISINE("CUISINE"), PLACE_TYPE("PLACE_TYPE"), OPTION("OPTION"),
    ATMOSPHERE("ATMOSPHERE"), INTERIOR("INTERIOR"), FEATURE("FEATURE"), MUSIC("MUSIC"),
    CUSTOM("CUSTOM");

    private String type;

    PlaceDetailType(String type) {
        this.type = type;
    }

}