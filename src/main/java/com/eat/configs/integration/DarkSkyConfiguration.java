package com.eat.configs.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.plogitech.darksky.api.jackson.DarkSkyJacksonClient;
import tk.plogitech.darksky.forecast.*;

@Configuration
public class DarkSkyConfiguration {

    private final String API_KEY = "541f63153dc05b420d51344ea085a944";

    private final Double BERLIN_CENTER_LONGTITUDE = 13.4105300D;
    private final Double BERLIN_CENTER_LATITUDE = 52.5243700D;

    @Bean
    public DarkSkyJacksonClient darkSkyJacksonClient() {
        return new DarkSkyJacksonClient();
    }

    @Bean
    public ForecastRequest forecastRequest() {
        return new ForecastRequestBuilder()
                .key(new APIKey(API_KEY))
                .location(new GeoCoordinates(
                        new Longitude(BERLIN_CENTER_LONGTITUDE), new Latitude(BERLIN_CENTER_LATITUDE)))
                .language(ForecastRequestBuilder.Language.en)
                .units(ForecastRequestBuilder.Units.si)
                .build();
    }

}