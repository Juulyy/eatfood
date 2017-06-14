package com.eat.models.b2b.enums;

public enum BusinessPlanCategory {

    PENDING("PENDING"), CONFIRMED("CONFIRMED"), CANCELED("CANCELED"), PAST("PAST");

    private String category;

    BusinessPlanCategory(String category) {
        this.category = category;
    }

}