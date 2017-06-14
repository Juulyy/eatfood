package com.eat.models.b2b;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@Table(name = "CONTACT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@ApiModel("is the contact data where the place is situated")
public class Contact implements Serializable {

    private static final long serialVersionUID = -838779980410456942L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTACT_ID")
    @ApiModelProperty(value = "is the ID in the DataBase", allowableValues = "Long", required = true, position = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CONTACT_TYPE_FK", referencedColumnName = "CONTACT_TYPE_ID")
    private ContactType type;

    @Column(name = "CONTACT_DETAILS")
    @ApiModelProperty(value = "is description of the contact details, e.g. comments", allowableValues = "String", required = true, position = 2)
    private String contactDetails;

}