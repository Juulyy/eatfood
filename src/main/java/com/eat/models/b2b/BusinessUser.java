package com.eat.models.b2b;

import com.eat.models.common.AbstractUser;
import com.eat.models.common.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "place")
@ToString(callSuper = true, exclude = "place")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "BUSINESS_USER")
@ApiModel(value = "is the user who is responsible for creating offers or another sales from the place side")
public class BusinessUser extends AbstractUser {

    private static final long serialVersionUID = 1234567879L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BUSINESS_USER_ID", updatable = false)
    @JsonProperty("id")
    @ApiModelProperty(value = "the ID in the DB", position = 1)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "BUSINESS_USER_NETWORKS", joinColumns = @JoinColumn(name = "BUSINESS_USER_FK"),
            inverseJoinColumns = @JoinColumn(name = "PLACE_NETWORK_FK"))
    private List<PlaceNetwork> placeNetworks;

    @JsonBackReference(value = "business_user")
    @JoinColumn(name = "PLACE_FK")
    @ManyToOne(cascade = CascadeType.ALL)
    private Place place;

    @Builder(builderMethodName = "of", buildMethodName = "create")
    public BusinessUser(String firstName, String lastName, String login, String password, String email,
                        Role role, boolean isEnabled, List<PlaceNetwork> placeNetworks, Place place) {
        super(firstName, lastName, login, password, email, role, isEnabled);
        this.placeNetworks = placeNetworks;
        this.place = place;
    }

}
