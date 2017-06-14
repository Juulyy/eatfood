package com.eat.models.b2c.enums;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public enum PrivacyType {

    EVERYONE(1, "EVERYONE"), FOLLOWERS_ONLY(2, "FOLLOWERS ONLY"),
    MUTUAL_FOLLOWING(3, "MUTUAL FOLLOWING"), NO_ONE(4, "NO ONE");

    private Integer id;

    private String type;

    PrivacyType(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

}
