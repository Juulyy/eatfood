package com.eat.models.b2c;

import com.eat.models.b2b.enums.BusinessPlanCategory;
import com.eat.models.b2b.enums.BusinessPlanStatus;
import com.eat.models.b2b.Place;
import com.eat.models.b2b.Offer;
import com.eat.models.b2c.enums.ClientPlanCategory;
import com.eat.models.b2c.enums.ClientPlanStatus;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@Entity
@Table(name = "PLAN")
@ApiModel(value = "user's tool for creating events with friends, booking places etc")
public class Plan implements Serializable {

    private static final long serialVersionUID = -3825991072959046626L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLAN_ID", updatable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLACE_FK", referencedColumnName = "PLACE_ID")
    private Place place;

    @Column(name = "PLAN_DATE", columnDefinition = "datetime")
    private LocalDateTime planDate;

    @Column(name = "DESCRIPTION")
    @Size(max = 140)
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "OFFER_FK", referencedColumnName = "OFFER_ID")
    private Offer offer;

    @Column(name = "PRIVACY_TYPE", columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private PrivacyType privacyType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORGANIZER_FK", referencedColumnName = "USER_ID")
    private AppUser organizer;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "PLAN_PARTICIPANTS", joinColumns = @JoinColumn(name = "PLAN_FK"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    private List<AppUser> participants;

    @Column(name = "CLIENT_CATEGORY", columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private ClientPlanCategory clientPlanCategory;

    @Column(name = "CLIENT_STATUS", columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private ClientPlanStatus clientPlanStatus;

    @Column(name = "BUSINESS_CATEGORY", columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private BusinessPlanCategory businessPlanCategory;

    @Column(name = "BUSINESS_STATUS", columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private BusinessPlanStatus businessPlanStatus;

    @Getter
    public enum PrivacyType {

        PUBLIC(1, "PUBLIC"), PRIVATE(2, "PRIVATE");

        private Integer id;

        private String privacyType;

        PrivacyType(Integer id, String privacyType) {
            this.id = id;
            this.privacyType = privacyType;
        }

    }

}