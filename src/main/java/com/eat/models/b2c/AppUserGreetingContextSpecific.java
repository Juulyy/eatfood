package com.eat.models.b2c;

import com.eat.models.common.Tag;
import com.eat.models.recommender.DayTimePeriod;
import com.eat.models.recommender.WeatherOption;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@ApiModel("settings for context specific type of user's greeting")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
@ToString(exclude = {"id", "appUserGreeting"})
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "APP_USER_GREETING_SPECIFIC_OPTION")
public class AppUserGreetingContextSpecific implements Serializable, Comparable<AppUserGreetingContextSpecific> {

    private static final long serialVersionUID = 9152711912986723720L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTEXT_SPECIFIC_ID")
    private Long id;

    @JsonBackReference("greetings_specific_options")
    @OneToOne(fetch = FetchType.EAGER)
    private AppUserGreeting appUserGreeting;

    @ApiModelProperty("Suggestion card has a validity by weather")
    @Column(name = "USE_WEATHER_PARAM")
    private boolean useWeatherFilter;

    @ApiModelProperty("Weather validity options for greetings")
    @ElementCollection
    @CollectionTable(name = "CONTEXT_SPECIFIC_WEATHER_OPTIONS", joinColumns = @JoinColumn(name = "SPECIFIC_OPTION_FK"))
    private Set<WeatherOption> weatherOptions;

    @ApiModelProperty("Suggestion card has a validity by weather")
    @Column(name = "USE_WEATHER_FILTER")
    private boolean usePeriodsFilter;

    @ApiModelProperty("List of days greeting validity period")
    @ElementCollection
    @CollectionTable(name = "CONTEXT_SPECIFIC_TIME_PERIODS", joinColumns = @JoinColumn(name = "SPECIFIC_OPTION_FK"))
    private Set<DayTimePeriod> periods;

    @ApiModelProperty("Tags filter validity options for greetings")
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CONTEXT_SPECIFIC_TAGS", joinColumns = @JoinColumn(name = "CONTEXT_SPECIFIC_FK"),
            inverseJoinColumns = @JoinColumn(name = "TAG_FK"), uniqueConstraints =
    @UniqueConstraint(columnNames = {"CONTEXT_SPECIFIC_FK", "TAG_FK"}, name = "context_tag_uk"))
    private Set<Tag> tags;

    @JsonIgnore
    @Transient
    private Double rate;

    @Builder(builderMethodName = "of", buildMethodName = "create")
    public AppUserGreetingContextSpecific(AppUserGreeting appUserGreeting,
                                          Set<WeatherOption> weatherOptions, Set<DayTimePeriod> periods,
                                          Set<Tag> tags) {
        this.appUserGreeting = appUserGreeting;
        this.weatherOptions = weatherOptions;
        this.periods = periods;
        this.tags = tags;
    }

    @Override
    public int compareTo(AppUserGreetingContextSpecific o) {
        return Double.compare(o.getRate() == null ? 0 : o.getRate(),
                this.getRate() == null ? 0 : this.getRate());
    }

}