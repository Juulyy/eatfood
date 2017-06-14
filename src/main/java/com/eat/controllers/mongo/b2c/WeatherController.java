package com.eat.controllers.mongo.b2c;

import com.eat.models.mongo.Weather;
import com.eat.models.mongo.enums.WeatherIcon;
import com.eat.services.mongo.b2c.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(value = "/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping(value = "/berlin", produces = "application/json")
    public Weather findLatestWeather() {
        return weatherService.findLatest();
    }

    @GetMapping(value = "/icons", produces = "application/json")
    public Set<WeatherIcon> findIcons() {
        return WeatherIcon.getIcons();
    }

}