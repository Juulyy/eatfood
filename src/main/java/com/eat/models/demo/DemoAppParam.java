package com.eat.models.demo;

import com.eat.models.b2c.AppUser;
import com.eat.models.mongo.enums.WeatherIcon;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@Entity
@Table(name = "DEMO_APP_PARAM")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DemoAppParam implements Serializable {

    private static final long serialVersionUID = -4690437556860314822L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEMO_ID", updatable = false)
    private Long id;

    @NotNull
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @Column(name = "TIME")
    private LocalTime time;

    @Column(name = "TEMP")
    private Double temp;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "WEATHER_ICON")
    private WeatherIcon icon;

    @Column(name = "LONGTITUDE")
    private Double longtitude;

    @Column(name = "LATITUDE")
    private Double latitude;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "APP_USER_FK", referencedColumnName = "USER_ID")
    private AppUser appUser;

    public LocalDateTime getDateTime() {
        LocalDateTime now = LocalDateTime.now();
        return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfWeek().getValue(),
                time.getHour(), time.getMinute(), time.getSecond());
    }

    public LocalDateTime getDateTimeWithShift(int shift) {
        return getDateTime().plus(shift, ChronoUnit.HOURS);

    }

}