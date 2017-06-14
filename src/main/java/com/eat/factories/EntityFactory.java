package com.eat.factories;

import com.eat.utils.converters.LocalDateConverter;
import com.eat.utils.converters.LocalDateTimeConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class EntityFactory extends AbstractEntityFactory {

    @Override
    public <T> T createInstance(String object, Class<T> clazz) {
        T entity = null;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateConverter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
                .create();
        try {
            entity = gson.fromJson(object, clazz);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return entity;
    }

}