package com.eat.models.common.enums;

import lombok.Getter;

@Getter
public enum TagType {

    MENU_ITEM_GROUP("MENU_ITEM_GROUP"), MENU_ITEM_CATEGORY("MENU_ITEM_CATEGORY"), MENU_ITEM("MENU_ITEM"),
    ALLERGY("ALLERGY"), DIET("DIET"),
    CUISINE("CUISINE"),
    PLACE_DETAIL("PLACE DETAIL"),
    ATMOSPHERE("ATMOSPHERE"), INTERIOR("INTERIOR"), FEATURE("FEATURE"), MUSIC("MUSIC"), PLACE_TYPE("PLACE_TYPE"),
    OPTION("OPTION"), CUSTOM("CUSTOM");

    private String type;

    TagType(String privacyType) {
        this.type = privacyType;
    }

}