package com.eat.models.b2b.enums;

import lombok.Getter;

@Getter
public enum OfferTemplateVariables {

    PLACE("$place$"), MENU_ITEM("$menu_item$"),
    DATE("$date$"), DAY_OF_WEEK("$day_of_week$"), TIME("$time$"),
    PERCENT("$percent$");

    private String var;

    OfferTemplateVariables(String var) {
        this.var = var;
    }

}