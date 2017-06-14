package com.eat.models.b2c;

import io.swagger.annotations.ApiModel;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@Entity
@Table(name = "SOCIAL_LINKS")
@ApiModel(value = "is intended to save the social links of the users/places")
public class SocialLinks implements Serializable {

    private static final long serialVersionUID = -2146272548708656440L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LINK_ID", updatable = false)
    private Long id;

    @NonNull
    @Column(name = "LINK_NAME", nullable = false)
    private String linkName;

    @NonNull
    @Column(name = "LINK_URL", nullable = false)
    private String url;

}
