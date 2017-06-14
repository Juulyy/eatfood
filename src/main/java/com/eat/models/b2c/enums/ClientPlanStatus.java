package com.eat.models.b2c.enums;

import lombok.Getter;

@Getter
public enum ClientPlanStatus {

    PENDING(1, "PENDING"), CONFIRMED(2, "CONFIRMED"), CANCELED(3, "CANCELED"), PAST(4, "PAST");

    private Integer id;

    private String status;

    ClientPlanStatus(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

}
