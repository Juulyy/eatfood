package com.eat.models.b2c.enums;

import lombok.Getter;

@Getter
public enum AppUserCollectionType {

    RECOMMENDED_PLACES(1, "RECOMMENDED_PLACES"),
    RECOMMENDED_OFFERS(2, "RECOMMENDED_OFFERS"),
    RECOMMENDED_MENU_ITEMS(3, "RECOMMENDED_MENU_ITEMS"),
    RECOMMENDED_FEATURES(4, "RECOMMENDED_FEATURES"),
    FOLLOWED_PLACES(3, "FOLLOWED_PLACES"),
    FOLLOWED_COLLECTIONS(5, "FOLLOWED_PLACES"),
    CUSTOM(6, "CUSTOM");

    private Integer id;

    private String type;

    AppUserCollectionType(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

}
