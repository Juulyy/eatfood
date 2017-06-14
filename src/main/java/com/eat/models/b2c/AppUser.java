package com.eat.models.b2c;

import com.eat.models.common.AbstractUser;
import com.eat.models.common.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id", "settings", "userPreferences"}, callSuper = false)
@ToString(exclude = {"id", "settings", "userPreferences"})
@Entity
@Table(name = "APP_USER")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "is the user of the application. User who finds places to eat in")
public class AppUser extends AbstractUser {

    private static final long serialVersionUID = -7171084937834629502L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", updatable = false)
    @ApiModelProperty(value = "ID in the Database", allowableValues = "Long", required = true, position = 1)
    private Long id;

//    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", columnDefinition = "VARCHAR(10)")
    @ApiModelProperty(value = "e.g. MALE, FEMALE")
    private Gender gender;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "REGISTRATION_DATE")
    private LocalDate registrationDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "LAST_LOGIN")
    private LocalDate lastLogin;

    @Column(name = "APPLE_ID")
    private String appleId;

    @Column(name = "FACEBOOK_ID")
    private String facebookId;

    @Column(name = "TWITTER_ID")
    private String twitterId;

    @Column(name = "INSTAGRAM_ID")
    private String instagramId;

    @Column(name = "PHOTO_URL")
    private String photoUrl;

    @Size(max = 140)
    @Column(name = "BIO")
    @ApiModelProperty(value = "is the biography of the user")
    private String bio;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "CURATOR_SOCIAL_LINKS", joinColumns = @JoinColumn(name = "USER_FK"),
            inverseJoinColumns = @JoinColumn(name = "LINK_FK"))
    private List<SocialLinks> links;

    @JsonManagedReference(value = "user_devices")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "appUser")
    private List<Device> devices;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "REFERRER_FK", referencedColumnName = "USER_ID")
    private AppUser referrer;

    @JsonManagedReference(value = "user_settings")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private AppUserSettings settings;

    @JsonManagedReference(value = "user_preferences")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private AppUserPreference userPreferences;

    @Getter
    public enum Gender {

        MALE(1, "MALE"), FEMALE(2, "FEMALE"), NO_MATTER(3, "NO MATTER");

        private Integer id;

        private String gender;

        Gender(Integer id, String gender) {
            this.id = id;
            this.gender = gender;
        }
    }

    @Builder(builderMethodName = "of", buildMethodName = "create")
    public AppUser(String firstName, String lastName, Gender gender, String login, String password,
                   String email, Role role, String appleId, String photoUrl, AppUserPreference userPreferences) {
        super(firstName, lastName, login, password, email, role, true);
        this.gender = gender;
        this.appleId = appleId;
        this.photoUrl = photoUrl;
        this.userPreferences = userPreferences;
        userPreferences.setUser(this);
    }

}
