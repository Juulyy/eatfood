package com.eat.models.b2c;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "APP_USER_ADDRESS")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "favourite addresses of app user")
public class AppUserAddress implements Serializable {

    private static final long serialVersionUID = 4762161091031083567L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADDRESS_ID", updatable = false)
    private Long id;

    @JsonBackReference(value = "user_favorite_address")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_PREFERENCE_FK", referencedColumnName = "USER_PREFERENCE_ID")
    private AppUserPreference appUserPreference;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "LONGTITUDE")
    private Double longtitude;

}