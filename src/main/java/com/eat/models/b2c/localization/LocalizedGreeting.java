package com.eat.models.b2c.localization;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Locale;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@Embeddable
public class LocalizedGreeting {

    @Column(name = "LOCALE")
    private Locale locale;

    @Column(name = "GREETING")
    private String greeting;

}
