package com.eat.models.b2c;

import com.eat.models.b2c.enums.PrivacyType;
import com.eat.utils.converters.TransitTypeListConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@Entity
@Table(name = "APP_USER_SETTINGS")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppUserSettings implements Serializable {

    private static final long serialVersionUID = 1709268161526347756L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SETTINGS_ID", updatable = false)
    private Long id;

    @JsonBackReference(value = "user_settings")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "APP_USER_FK")
    private AppUser user;

    /*Notifications begin*/
    /**
     * "Recently visited places" option
     *
     * @param rvp - switch on/off "Recently Visited Place ..." messages in user's activity
     */
    @Column(name = "RVP_VISIBILITY")
    private boolean rvp;

    /**
     * "Recommendations" option
     *
     * @param recommendations - switch on/off all recommendations for user
     */
    @Column(name = "RECOMMENDATIONS")
    private boolean recommendations;
    /*Notifications end*/

    /*Social start*/
    /**
     * "Someone started following you" option
     *
     * @param smvnFollowing - switch on/off "... started following you" messages in user's activity
     */
    @Column(name = "SMVN_FOLLOWING")
    private boolean smvnFollowing;

    /**
     * "Friend joins EAT!" option
     *
     * @param friendJoin - switch on/off "... friend joins Eat!" messages in user's activity
     */
    @Column(name = "FRIEND_JOIN")
    private boolean friendJoin;

    /**
     * "Someone invited you to a plan" option
     *
     * @param smvnPlanInvited - switch on/off "... invited you to a plan" messages in user's activity
     */
    @Column(name = "SMVN_PLAN_INVITED")
    private boolean smvnPlanInvited;

    /**
     * " Someone recommends a place/food" option
     *
     * @param smvnRecommend - switch on/off "... recommends a place/food" messages in user's activity
     */
    @Column(name = "SMVN_RECOMMEND")
    private boolean smvnRecommend;
    /*Social end*/

    /*Map start*/
    /**
     * "Radius" option
     *
     * @param radius - the radius (kilometers) of search places and recommendations
     */
    @Column(name = "RADIUS")
    private int radius;

    /**
     * "Preferred transit types" option
     *
     * @param transitTypes - list preferred transit types. Default (walk)
     *
     */
    @Basic
    @Convert(converter = TransitTypeListConverter.class)
    @Column(name = "TRANSIT_TYPES")
    private List<TransitType> transitTypes;
    /*Map end*/

    /*Privacy start*/
    /**
     * "Receive plans invitations from:" option
     *
     * @param planInvitation - plans invitation privacy
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "PLAN_INVITATION")
    private PrivacyType planInvitation;

    /**
     * "Show my recommendations to:" option
     *
     * @param recommendationView - recommendation visibility privacy
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "RECOMMENDATION_VIEW")
    private PrivacyType recommendationView;

    /**
     * "Show my collections to:" option
     *
     * @param collectionView - collection visibility privacy
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "COLLECTION_VIEW")
    private PrivacyType collectionView;

    /**
     * "Show my subscriptions to:" option
     *
     * @param subscriptionsView - subscriptions visibility privacy
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "SUBSCRIPTION_VIEW")
    private PrivacyType subscriptionsView;

    /**
     * "Show your history of visited places to:" option
     *
     * @param placeHistoryView - history of visited places visibility privacy
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "PlACE_HISTORY_VIEW")
    private PrivacyType placeHistoryView;

    /**
     * "SHOW THAT I HAVE BEEN HERE:" option
     *
     * @param placeProofView - switch on/off "SHOW THAT I HAVE BEEN HERE:" messages in user's activity
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "PLACE_PROOF_VIEW")
    private PrivacyType placeProofView;

    /**
     * "Show my new following to:" option
     *
     * @param followingView - following visibility privacy
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "FOLLOWING_VIEW")
    private PrivacyType followingView;
    /*Privacy end*/

    /**
     * "Blocked users" field
     *
     * @param blockedUsers - list of blocked users
     */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "BLOCKED_USERS", joinColumns = @JoinColumn(name = "SETTINGS_FK"),
            inverseJoinColumns = @JoinColumn(name = "BLOCKED_USER_FK"))
    private List<AppUser> blockedUsers;

    /**
     * "Language" field
     *
     * @param language - user's interface language
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "LANGUAGE")
    private Language language;

    @Getter
    public enum TransitType {

        DRIVE(1, "DRIVE"), BIKE(2, "BIKE"), WALK(3, "WALK"), PUBLIC(4, "PUBLIC");

        private Integer id;

        private String type;

        TransitType(Integer id, String type) {
            this.id = id;
            this.type = type;
        }

    }

    @Getter
    public enum Language {

        ENGLISH(1, "ENGLISH"), GERMAN(2, "GERMAN");

        private Integer id;

        private String type;

        Language(Integer id, String type) {
            this.id = id;
            this.type = type;
        }

    }

}
