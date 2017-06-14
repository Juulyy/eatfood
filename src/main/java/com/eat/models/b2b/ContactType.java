package com.eat.models.b2b;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "CONTACT_TYPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@ApiModel(value = "is included type of contact information: EMAIL, PHONE, ADDRESS, URL, ZIP-CODE")
public class ContactType implements Serializable {

    private static final long serialVersionUID = -4441495761755305004L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTACT_TYPE_ID")
    @ApiModelProperty(value = "is the ID in the DataBase", allowableValues = "Long", required = true, position = 1)
    private Long id;

    @Column(name = "NAME", nullable = false)
    @ApiModelProperty(value = "the name of the contact type, e.g. email, home phone, mobile phone etc.",
            allowableValues = "Long", required = true, position = 2)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "CONTACT_ASPECT", nullable = false)
    @ApiModelProperty(value = "the more general enumeration of the contact type: EMAIL, PHONE, ADDRESS, URL, ZIP-CODE",
            required = true, position = 3)
    private ContactAspect contactAspect;

    @Getter
    public enum ContactAspect {

        EMAIL(1, "e-mail"), PHONE(2, "phone"), ADDRESS(3, "address"), URL(4, "url"), ZIP_CODE(5, "zip-code");

        private Integer id;

        private String name;

        ContactAspect(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

    }
}


