package com.eat.utils.converters.dto;

import com.eat.dto.b2c.AppUserGreetingDto;
import com.eat.models.b2c.AppUserGreeting;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class AppUserGreetingConverter {

    public AppUserGreetingDto toAppUserGreetingDto(AppUserGreeting greeting, String name) {
        return AppUserGreetingDto.of()
                .greetingText(greeting.getGreetingText())
                .name(name)
                .create();
    }

}