package com.eat.models.b2c;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalTime;

@ApiModel("Time period options for user greetings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@Embeddable
public class AppUserGreetingTimePeriod {

    @Column(name = "FROM_TIME")
    private LocalTime fromTime;

    @Column(name = "TO_TIME")
    private LocalTime toTime;

}