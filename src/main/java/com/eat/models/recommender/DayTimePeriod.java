package com.eat.models.recommender;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.DayOfWeek;
import java.time.LocalTime;

@ApiModel("Day & time period options for suggestion categories, for context specific messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@EqualsAndHashCode
@Embeddable
public class DayTimePeriod {

    @Column(name = "DAY")
    private DayOfWeek dayOfWeek;

    @Column(name = "FROM_TIME")
    private LocalTime fromTime;

    @Column(name = "TO_TIME")
    private LocalTime toTime;

}