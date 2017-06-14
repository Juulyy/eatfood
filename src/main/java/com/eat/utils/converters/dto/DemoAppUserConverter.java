package com.eat.utils.converters.dto;

import com.eat.dto.demo.DemoAppParamDto;
import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.AppUserPreference;
import com.eat.models.demo.DemoAppParam;
import com.eat.services.demo.DemoAppUserConsumer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class DemoAppUserConverter {

    public DemoAppParam toDemoAppParam(DemoAppParamDto dto) {
        AppUser appUser = AppUser.of()
                .userPreferences(AppUserPreference.of()
                        .tasteTags(dto.tags)
                        .collections(new ArrayList<>())
                        .create())
                .create();

        DemoAppUserConsumer.autoFieldsGeneratorAppUserConsumer.accept(appUser);

        return DemoAppParam.of()
                .time(dto.time)
                .temp(dto.temp)
                .icon(dto.icon)
                .latitude(dto.latitude)
                .longtitude(dto.longtitude)
                .appUser(appUser)
                .create();
    }

}