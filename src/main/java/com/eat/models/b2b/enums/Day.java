package com.eat.models.b2b.enums;

import lombok.Getter;

@Getter
public enum Day {

    MONDAY(1, "Monday"),
    TUESDAY(2, "Tuesday"),
    WEDNESDAY(3, "Wednesday"),
    THURSDAY(4, "Thursday"),
    FRIDAY(5, "Friday"),
    SATURDAY(6, "Saturday"),
    SUNDAY(7, "Sunday");

    private Integer id;

    private String name;

    Day(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}