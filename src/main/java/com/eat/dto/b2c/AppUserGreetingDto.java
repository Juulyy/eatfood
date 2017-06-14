package com.eat.dto.b2c;

import com.eat.models.b2c.localization.LocalizedGreeting;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;

import java.util.Locale;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

@Builder(builderMethodName = "of", buildMethodName = "create")
public final class AppUserGreetingDto {

    @NonNull
    @JsonProperty(value = "message")
    public final Map<Locale, LocalizedGreeting> greetingText;

    @JsonInclude(value = Include.NON_NULL)
    @JsonProperty(value = "name")
    public final String name;

}