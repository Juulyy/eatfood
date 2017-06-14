package com.eat.models.b2b.enums;

import lombok.Getter;

public enum BusinessPlanStatus {

    CANCELLED_BY_APP_USER("CANCELLED BY APP USER"),
    CANCELLED_BY_BUSINESS_USER("CANCELLED BY BUSINESS USER"),
    SUCCESSFUL("SUCCESSFUL"),
    UNSUCCESSFUL("UNSUCCESSFUL");

    @Getter
    private String status;

    BusinessPlanStatus(String status) {
        this.status = status;
    }

}