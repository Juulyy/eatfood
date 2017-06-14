package com.eat.models.common.enums;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

@ApiModel(value = "for application users e.g. ADMIN, USER, CURATOR etc. ")
public enum RoleType {

    ROLE_SUPER_ADMIN("SUPER_ADMIN"),
    ROLE_BUSINESS_ADMIN("BUSINESS_ADMIN"),
    ROLE_BUSINESS_MANAGER("BUSINESS_MANAGER"),
    ROLE_BUSINESS_USER("BUSINESS_USER"),
    ROLE_APP_USER("APP_USER"),
    ROLE_APP_CURATOR("APP_CURATOR");

    @Getter
    private String name;

    RoleType(String name) {
        this.name = name;
    }

}