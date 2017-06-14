package com.eat.logic.b2c;

import com.eat.models.b2c.AppUserGreetingContextSpecific;
import com.eat.models.mongo.Weather;
import com.eat.models.mongo.enums.WeatherIcon;
import com.eat.models.recommender.SuggestionCategory;
import com.eat.models.recommender.WeatherOption;
import com.eat.services.mongo.b2c.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Predicate;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class WeatherFilter {

    @Autowired
    private WeatherService weatherService;

    public boolean filterGreeting(AppUserGreetingContextSpecific specific, Double userTemp, WeatherIcon userWeatherIcon) {
        if (specific.isUseWeatherFilter()) {
            return isRelevantWeatherOptionDemo(specific.getWeatherOptions(), userTemp, userWeatherIcon);
        }
        return true;
    }

    public boolean filterCategories(SuggestionCategory category, Double userTemp, WeatherIcon userWeatherIcon) {
        if (category.isUseWeatherParam()) {
            return isRelevantWeatherOptionDemo(category.getWeatherOptions(), userTemp, userWeatherIcon);
        }
        return true;
    }

    private boolean isRelevantWeatherOptionDemo(Set<WeatherOption> options, Double userTemp, WeatherIcon userWeatherIcon) {
      if (userTemp == null || userWeatherIcon == null) {
          return true;
      }
        return options.stream()
                .anyMatch(weatherOption -> isCorrectTemp(weatherOption, userTemp) &&
                        isContainsWeatherIcon(weatherOption, userWeatherIcon));
    }

    private boolean isCorrectTemp(WeatherOption weatherOption, Double userTemp) {
        if (weatherOption.isUseTempFilter()) {
            return userTemp >= weatherOption.getFromTemperature() &&
                    userTemp <= weatherOption.getToTemperature();
        }
        return true;
    }

    private boolean isContainsWeatherIcon(WeatherOption weatherOption, WeatherIcon userWeatherIcon) {
        if (weatherOption.isUseWeatherIconsFilter()) {
            return weatherOption.getIcons().stream()
                    .anyMatch(weatherIcon -> weatherIcon.getIcon().equals(userWeatherIcon.getIcon()));
        }
        return true;
    }

    private boolean isRelevantWeatherOption(Set<WeatherOption> options) {
        Weather currentWeather = weatherService.findLatest();
        for (WeatherOption weatherOption : options) {
            if (weatherOption.isUseTempFilter()) {
                return currentWeather.getTemp() >= weatherOption.getFromTemperature() &&
                        currentWeather.getTemp() <= weatherOption.getToTemperature();
            }

            if (weatherOption.isUseWeatherIconsFilter()) {
                return weatherOption.getIcons().stream()
                        .anyMatch(weatherIcon -> weatherIcon.getIcon().equals(currentWeather.getIcon()));
            }
        }
        return false;
    }

    private Predicate<WeatherOption> equalsWeatherConditionPredicate(Weather currentWeather) {
        return weather -> weather.getIcons().stream()
                .anyMatch(weatherIcon -> weatherIcon.getIcon().equals(currentWeather.getIcon()));
    }

    private Predicate<WeatherOption> fromTempLessForNowPredicate(Weather currentWeather) {
        return weather -> currentWeather.getTemp() - weather.getFromTemperature() >= 0;
    }

    private Predicate<WeatherOption> toTempGreaterForNowPredicate(Weather currentWeather) {
        return weather -> weather.getToTemperature() - currentWeather.getTemp() >= 0;
    }

}