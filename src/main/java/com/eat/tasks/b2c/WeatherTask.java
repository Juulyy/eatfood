package com.eat.tasks.b2c;

import com.eat.services.mongo.b2c.WeatherService;
import com.eat.utils.converters.ForecastConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import tk.plogitech.darksky.api.jackson.DarkSkyJacksonClient;
import tk.plogitech.darksky.forecast.ForecastException;
import tk.plogitech.darksky.forecast.ForecastRequest;
import tk.plogitech.darksky.forecast.model.Forecast;

//@Slf4j
//@Component
public class WeatherTask {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private DarkSkyJacksonClient darkSkyClient;

    @Autowired
    private ForecastRequest forecastRequest;

    @Autowired
    private ForecastConverter forecastConverter;

    @Scheduled(cron = "${tasks.weather.schedule}")
    public void parse() {
        Forecast forecast = null;
        try {
            forecast = darkSkyClient.forecast(forecastRequest);
        } catch (ForecastException e) {
//            log.error(e.getMessage());
            e.printStackTrace();
        }
        weatherService.save(forecastConverter.toWeather(forecast));
    }

}