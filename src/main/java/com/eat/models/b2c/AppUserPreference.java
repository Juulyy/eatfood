package com.eat.models.b2c;

import com.eat.models.common.Tag;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@EqualsAndHashCode(of = {"user"}, callSuper = false)
@ToString(of = {"user"})
@JsonIgnoreProperties(ignoreUnknown = true, value = {"followers", "following"})
@ApiModel(value = "is the user of the application. User who finds places to eat in")
@Entity
@Table(name = "APP_USER_PREFERENCES")
public class AppUserPreference implements Serializable {

    private static final long serialVersionUID = 8882751412486783926L;

    @Id
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "user"))
    @Column(name = "USER_PREFERENCE_ID", unique = true, nullable = false)
    private Long id;

    @JsonBackReference(value = "user_preferences")
    @OneToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private AppUser user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "APP_USER_FOLLOWERS", joinColumns = @JoinColumn(name = "USER_PREFERENCE_FK"),
            inverseJoinColumns = @JoinColumn(name = "FOLLOWERS_FK"))
    private Set<AppUser> followers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "APP_USER_FOLLOWING", joinColumns = @JoinColumn(name = "USER_PREFERENCE_FK"),
            inverseJoinColumns = @JoinColumn(name = "FOLLOWING_FK"))
    private Set<AppUser> following;

    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_TASTE_TAGS", joinColumns = @JoinColumn(name = "USER_PREFERENCE_FK"),
            inverseJoinColumns = @JoinColumn(name = "TAG_FK"), uniqueConstraints =
    @UniqueConstraint(columnNames = {"USER_PREFERENCE_FK", "TAG_FK"}, name = "user_tag_uk"))
    private Set<Tag> tasteTags;

    @Singular
    @JsonManagedReference(value = "user_collection")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "appUserPreference")
    private List<AppUserCollection> collections;

    @JsonManagedReference(value = "user_not_interesting")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "appUserPreference")
    private Set<NotInteresting> notInteresting;

    @JsonManagedReference(value = "user_place_history")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "appUserPreference")
    private Set<VisitedPlaces> visitedPlaces;

    @JsonManagedReference(value = "user_favorite_address")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "appUserPreference")
    private Set<AppUserAddress> favoriteAddresses;

}
