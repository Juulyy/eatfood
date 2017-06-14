package com.eat.configs.integration;

import fi.foyt.foursquare.api.FoursquareApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FoursquareConfiguration {

    private static final String CLIENT_ID = "DDUFTWU5OZKPPACDK0JDU5LU1XSNTB5P2VAWCFEJETRUBH10";
    private static final String CLIENT_SECRET = "4CPUGOANOH5H0I4UULHHHL4TI1A3JIMDEFMD4J2OZJKVOXFO";

    @Bean(name = "foursquareApi")
    public FoursquareApi foursquareApi() {
        FoursquareApi foursquareApi = new FoursquareApi(CLIENT_ID, CLIENT_SECRET, "http:/localhost:8080");
        foursquareApi.setUseCallback(false);
        return foursquareApi;
    }

}
