package com.eat.models.b2c;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@Entity
@Table(name = "INVITE")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "user invitation in application")
public class Invite implements Serializable {

    private static final long serialVersionUID = -4690437556860314822L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INVITE_ID", updatable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "APP_USER_FK", referencedColumnName = "USER_ID")
    private AppUser appUser;

    @Column(name = "REFERRAL_ID")
    private String refId;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private ReferralType type;

    @Getter
    public enum ReferralType {

        PLAN(1, "PLAN"), COLLECTION(2, "COLLECTION"), OFFER(3, "OFFER");

        private Integer id;;

        private String type;

        ReferralType(Integer id, String type) {
            this.id = id;
            this.type = type;
        }

    }

    public String getInviteUrl() {
        return "?userId=" + appUser.getId() + "&refId=" + refId;
    }

}
