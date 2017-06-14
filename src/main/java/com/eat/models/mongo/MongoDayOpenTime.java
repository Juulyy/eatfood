package com.eat.models.mongo;

import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;


@Data
@Builder(builderMethodName = "of", buildMethodName = "create")
public class MongoDayOpenTime {

    private DayOfWeek day;

    private Integer openFrom;

    private Integer openTo;

}
