package com.eat.models.b2b;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "is intended to determine the working days/hours of the certain place")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@ToString(exclude = {"id", "place"})
@EqualsAndHashCode(exclude = {"id", "place"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "place"})
@Entity
@Table(name = "SCHEDULE")
public class Schedule implements Serializable {

    private static final long serialVersionUID = -5045155404114104332L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_ID", updatable = false)
    private Long id;

    @JsonIgnore
    @JsonBackReference(value = "place_schedule")
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PLACE_FK")
    private Place place;

    @Column(name = "NAME")
    private String name;

    @Singular
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "SCHEDULE_OPEN_TIMES", joinColumns = @JoinColumn(name = "SCHEDULE_FK"),
            inverseJoinColumns = @JoinColumn(name = "DAY_OPEN_TIME_FK"))
    private List<DayOpenTime> openTimes;

}