package com.eat.models.b2c;

import com.eat.models.b2c.enums.AppUserGreetingType;
import com.eat.models.b2c.localization.LocalizedGreeting;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@ApiModel("General and context specific user's greetings")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "APP_USER_GREETING")
public class AppUserGreeting implements Serializable, Comparable<AppUserGreeting> {

    private static final long serialVersionUID = -1590548515232455712L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GREETING_ID", updatable = false)
    private Long id;

    @ApiModelProperty("Name of greeting database record")
    @Column(name = "NAME", columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

    @ApiModelProperty("Status of greeting activation")
    @Column(name = "IS_ACTIVE", nullable = false)
    private boolean isActive;

    @ApiModelProperty("Greeting type")
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "GREETING_TYPE", columnDefinition = "VARCHAR(20)")
    private AppUserGreetingType type;

    @ApiModelProperty("Greeting message template")
    @ElementCollection
    @CollectionTable(name = "LOCALIZED_GREETING")
    @MapKeyJoinColumn(name = "LOCALE")
    private Map<Locale, LocalizedGreeting> greetingText;

    @ApiModelProperty("Greeting has a validity period by time of day")
    @Column(name = "USE_TIME_PERIOD")
    private boolean isTimePeriod;

    @ApiModelProperty("List of time greeting validity period")
    @ElementCollection
    @CollectionTable(name = "GREETING_TIME_PERIOD", joinColumns = @JoinColumn(name = "GREETING_FK"))
    private Set<AppUserGreetingTimePeriod> times;

    @ApiModelProperty("Greeting has a validity period by particular dates")
    @Column(name = "USE_DATE_PERIOD")
    private boolean isDatePeriod;

    @ApiModelProperty("List of dates greeting validity period")
    @ElementCollection
    @CollectionTable(name = "GREETING_DATE_PERIOD", joinColumns = @JoinColumn(name = "GREETING_FK"))
    @Column(name = "DATE")
    private Set<AppUserGreetingDatePeriod> dates;

    @ApiModelProperty("True only for specific greetings")
    @Column(name = "IS_CONTEXT_SPECIFIC")
    private boolean isContextSpecific;

    @ApiModelProperty("Additional validity options for context specific type of greetings")
    @JsonManagedReference("greetings_specific_options")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "appUserGreeting")
    private AppUserGreetingContextSpecific specificOption;

    @JsonIgnore
    @Column(name = "RATE")
    private Double rate;

    @Builder(builderMethodName = "of", buildMethodName = "create")
    public AppUserGreeting(String name, boolean isActive, AppUserGreetingType type, Map<Locale,
            LocalizedGreeting> greetingText, boolean isTimePeriod, Set<AppUserGreetingTimePeriod> times,
                           boolean isDatePeriod, Set<AppUserGreetingDatePeriod> dates, boolean isContextSpecific,
                           AppUserGreetingContextSpecific specificOption, Double rate) {
        this.name = name;
        this.isActive = isActive;
        this.type = type;
        this.greetingText = greetingText;
        this.isTimePeriod = isTimePeriod;
        this.times = times;
        this.isDatePeriod = isDatePeriod;
        this.dates = dates;
        this.isContextSpecific = isContextSpecific;
        if (specificOption != null) {
            this.specificOption = specificOption;
            specificOption.setAppUserGreeting(this);
        }
        this.rate = rate;
    }

    @Override
    public int compareTo(AppUserGreeting o) {
        return Double.compare(o.getRate() == null ? 0 : o.getRate(),
                this.getRate() == null ? 0 : this.getRate());
    }

}