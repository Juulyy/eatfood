package com.eat.models.b2c.enums;

import lombok.Getter;

@Getter
public enum ClientPlanCategory {

    CREATED(1, "CREATED"), UPCOMING(2, "UPCOMING"), PAST(3, "UPCOMING");

    private Integer id;

    private String category;

    ClientPlanCategory(Integer id, String category) {
        this.id = id;
        this.category = category;
    }

}
