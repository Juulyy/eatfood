package com.eat.utils.converters;

import com.eat.models.mongo.Weather;
import com.eat.models.mongo.enums.WeatherIcon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tk.plogitech.darksky.forecast.model.Forecast;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class ForecastConverter {

    public Weather toWeather(Forecast forecast) {
        return Weather.of()
                .summary(forecast.getCurrently().getSummary())
                .icon(parseToWeatherIcon(forecast.getCurrently().getIcon()))
                .precipIntensity(forecast.getCurrently().getPrecipIntensity())
                .precipType(forecast.getCurrently().getPrecipType())
                .temp(forecast.getCurrently().getTemperature())
                .apparentTemp(forecast.getCurrently().getApparentTemperature())
                .dewPoint(forecast.getCurrently().getDewPoint())
                .windSpeed(forecast.getCurrently().getWindSpeed())
                .pressure(forecast.getCurrently().getPressure())
                .cloudCover(forecast.getCurrently().getCloudCover())
                .timeZone(forecast.getTimezone())
                .location(new double[]{forecast.getLatitude(), forecast.getLongitude()})
                .systemDateTime(LocalDateTime.now())
                .create();
    }

    private WeatherIcon parseToWeatherIcon(String iconName) {
        Optional<WeatherIcon> icon = WeatherIcon.getIcons().stream()
                .filter(weatherIcon -> weatherIcon.getIcon().equals(iconName))
                .findAny();
        if (icon.isPresent()) {
            return icon.get();
        } else {
            log.error("Unknown icon type : " + iconName + "!");
            return WeatherIcon.UNKNOWN;
        }
    }

}