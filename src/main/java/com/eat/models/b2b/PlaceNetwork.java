package com.eat.models.b2b;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id", "places"})
@ToString(exclude = {"id", "places"})
@Entity
@Table(name = "PLACE_NETWORK")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "is the network/s where the place is included")
public class PlaceNetwork implements Serializable {

    private static final long serialVersionUID = -2311337082473494031L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLACE_NETWORK_ID")
    @ApiModelProperty(value = "the ID in the DB", position = 1)
    private Long id;

    @Column(name = "NAME")
    @ApiModelProperty(value = "the name of the place network", position = 2)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "PLACE_NETWORK_PLACES", joinColumns = @JoinColumn(name = "PLACE_NETWORK_FK"),
            inverseJoinColumns = @JoinColumn(name = "PLACE_FK"))
    private List<Place> places;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BUSINESS_USER_FK", referencedColumnName = "BUSINESS_USER_ID")
    private BusinessUser businessUser;

}
