package com.eat.models.b2c;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.codehaus.jackson.annotate.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "DEVICE")
@ApiModel(value = "user's gadgets used to login into application")
public class Device implements Serializable {

    private static final long serialVersionUID = 6699508184373641768L;

    @Id
    @Column(name = "DEVICE_ID", unique = true, columnDefinition = "VARCHAR(64)")
    private String deviceId;

    @JsonBackReference(value = "user_devices")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_FK", referencedColumnName = "USER_ID")
    private AppUser appUser;

}
