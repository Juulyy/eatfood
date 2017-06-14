package com.eat.models.b2c;

import com.eat.models.b2b.Place;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@Entity
@Table(name = "APP_USER_VISITED_PLACES")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "places that user visited")
public class VisitedPlaces implements Serializable {

    private static final long serialVersionUID = -7323176381047077367L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VISITED_PLACE_ID", updatable = false)
    private Long id;

    @JsonBackReference(value = "user_place_history")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_PREFERENCE_FK", referencedColumnName = "USER_PREFERENCE_ID")
    private AppUserPreference appUserPreference;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLACE_FK", referencedColumnName = "PLACE_ID")
    private Place place;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "DATE")
    private LocalDateTime date;

    @Column(name = "DAY", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

}
