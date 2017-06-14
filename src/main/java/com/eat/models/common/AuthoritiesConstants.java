package com.eat.models.common;

import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor
public class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_SUPER_ADMIN";

    public static final String USER = "ROLE_APP_USER";

    public static final String BUSINESS_USER = "ROLE_BUSINESS_ADMIN";

    public static final String BUSINESS_MANAGER = "ROLE_BUSINESS_MANAGER";

    public static final String CURATOR = "ROLE_APP_CURATOR";

}
