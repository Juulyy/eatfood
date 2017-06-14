package com.eat.models.b2b;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@ToString(exclude = "id")
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "DAY_OPEN_TIME")
@ApiModel(value = "is the place's working  hours and days")
public class DayOpenTime implements Serializable {

    private static final long serialVersionUID = 8357216346440756977L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false)
    @ApiModelProperty(value = "is the ID in the DataBase", allowableValues = "Long", required = true, position = 1)
    private Long id;

    @Column(name = "DAY_NAME", nullable = false)
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "is the day of the week", position = 2)
    private DayOfWeek day;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @Column(name = "OPEN_FROM", columnDefinition = "time")
    private LocalTime from;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @Column(name = "OPEN_TO", columnDefinition = "time")
    private LocalTime to;

}