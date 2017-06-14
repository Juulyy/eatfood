package com.eat.models.recommender;

import com.eat.models.common.Image;
import com.eat.models.common.Tag;
import com.eat.models.common.enums.TagType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@ApiModel("Categories for main page off user application with some suggestions about places to visit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"id", "dayTimes", "weatherOptions"})
@EqualsAndHashCode(exclude = "id")
@Builder(builderMethodName = "of", buildMethodName = "create")
@Entity
@Table(name = "SUGGESTION_CATEGORY")
public class SuggestionCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID", updatable = false)
    private Long id;

    @Size(max = 150)
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @ApiModelProperty("List of time greeting validity period")
    @ElementCollection
    @CollectionTable(name = "SUGGESTION_CATEGORY_TIME_PERIOD", joinColumns = @JoinColumn(name = "SUGGESTION_CATEGORY_FK"))
    private Set<DayTimePeriod> dayTimes = new HashSet<>();

    @ApiModelProperty("Suggestion card has a validity period by particular dates")
    @Column(name = "USE_DATE_PERIOD")
    private boolean useDatePeriod;

    @ApiModelProperty("List of dates suggestion category validity period")
    @ElementCollection
    @CollectionTable(name = "SUGGESTION_CATEGORY_DATE_PERIOD", joinColumns = @JoinColumn(name = "SUGGESTION_CATEGORY_FK"))
    private Set<SuggestionCategoryDatePeriod> dates = new HashSet<>();

    @ApiModelProperty("Suggestion card has a validity by weather")
    @Column(name = "USE_WEATHER_PARAM")
    private boolean useWeatherParam;

    @ApiModelProperty("Weather filtering options of suggestion category")
    @ElementCollection
    @CollectionTable(name = "SUGGESTION_CATEGORY_WEATHER", joinColumns = @JoinColumn(name = "SUGGESTION_CATEGORY_FK"))
    private Set<WeatherOption> weatherOptions = new HashSet<>();

    @ApiModelProperty("Suggestion card has a filter by particular tags")
    @Column(name = "USE_TAG_FILTER")
    private boolean useTagFilter;

    @ApiModelProperty("Suggestion card photo")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "IMAGE", referencedColumnName = "IMAGE_ID")
    private Image image;

    @ApiModelProperty("List of tags for filter out places and users")
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SUGGESTION_CATEGORY_TAG", joinColumns = @JoinColumn(name = "CATEGORY_FK",
            referencedColumnName = "CATEGORY_ID",
            foreignKey = @ForeignKey(name = "SUGGESTION_CATEGORY_FK", value = ConstraintMode.CONSTRAINT)),
            inverseJoinColumns = @JoinColumn(name = "TAG_FK", referencedColumnName = "TAG_ID",
                    foreignKey = @ForeignKey(name = "TAG_FK", value = ConstraintMode.CONSTRAINT)),
            uniqueConstraints = {@UniqueConstraint(name = "category_tags_uk", columnNames = {"CATEGORY_FK", "TAG_FK"})})
    private Set<Tag> tags = new HashSet<>();

    @ApiModelProperty("List of dates suggestion category validity period")
    @ElementCollection
    @CollectionTable(name = "SUGGESTION_CATEGORY_TAG_RATE", joinColumns = @JoinColumn(name = "SUGGESTION_CATEGORY_FK"))
    private Set<SuggestionCategoryTagRate> tagRates = new HashSet<>();

    @ApiModelProperty("Limit for suggestion category's pool of places")
    @Column(name = "PLACE_POOL_SIZE")
    private Integer placePoolSize;

    @ApiModelProperty("Suggestion card has a validity by weather")
    @Column(name = "USE_WEATHER_FILTER")
    private Integer minCuratorRecommendations = 0;

    public String getTagNamesAsString() {
        return getTags().stream()
                .map(tag -> tag.getName().toLowerCase())
                .collect(Collectors.joining(" "));
    }

    public Double getTagRank(Tag tag) {
        return getTagRates().stream()
                .filter(value -> value.getTagType() != TagType.DIET)
                .filter(value -> value.getTagType() != TagType.ALLERGY)
                .filter(categoryTagRate -> categoryTagRate.getTagType() == tag.getType())
                .mapToDouble(SuggestionCategoryTagRate::getRate)
                .findAny()
                .orElse(0.1d);
    }

    public boolean timeInCategoryPeriod(LocalTime time) {
        return getDayTimes().stream()
                .anyMatch(greetingFromTimeLessForCurrentPredicate(time)
                        .and(greetingToTimeGreaterForCurrentPredicate(time)));
    }

    private Predicate<DayTimePeriod> greetingFromTimeLessForCurrentPredicate(LocalTime userTime) {
        return time -> ChronoUnit.NANOS.between(time.getFromTime(), userTime) >= 0;
    }

    /*@JsonIgnore
    public Predicate<SuggestionCategoryTagRate> rankableTagTypePredicate() {
        return tagRate -> tagRate.getTagType() != TagType.DIET || tagRate.getTagType() != TagType.ALLERGY;
    }*/

    private Predicate<DayTimePeriod> greetingToTimeGreaterForCurrentPredicate(LocalTime userTime) {
        return time -> ChronoUnit.NANOS.between(userTime, time.getToTime()) >= 0;
    }

    public DayTimePeriod getNearestDateTimePeriod(LocalTime time) {

        LocalDate now = LocalDate.now();
        DayOfWeek dayOfWeek = DayOfWeek.of(now.getDayOfWeek());

        SortedMap<Integer, DayTimePeriod> bestDiffs = new TreeMap<>();

        for (DayTimePeriod timePeriod : getDayTimes()) {
            if (timePeriod.getDayOfWeek() == dayOfWeek) {
                Integer diff = time.getNano() - timePeriod.getFromTime().getNano();

                if (diff > 0 && time.getNano() - timePeriod.getToTime().getNano() < 0) {
                    bestDiffs.put(diff, timePeriod);
                }
            }
        }

        return bestDiffs.get(bestDiffs.lastKey());

    }

    public LocalTime getNearestStartTime(LocalTime time) {

        DayTimePeriod nearestTimePeriod = getNearestDateTimePeriod(time);
        return getNearestDateTimePeriod(time) == null ? null : nearestTimePeriod.getFromTime();
    }

    public Set<Pair<LocalTime, LocalTime>> getDateTimePeriodsWithoutDays() {

        return getDayTimes().stream()
                .map((dayTimePeriod -> ImmutablePair.of(dayTimePeriod.getFromTime(), dayTimePeriod.getToTime())))
                .collect(Collectors.toSet());
    }

    public Optional<DayTimePeriod> getDateTimePeriodFor(LocalDateTime dateTime) {

        return this.getDayTimes().stream()
                .filter(dayTimePeriod -> dayTimePeriod.getDayOfWeek() == dateTime.getDayOfWeek())
                .findAny();
    }

}