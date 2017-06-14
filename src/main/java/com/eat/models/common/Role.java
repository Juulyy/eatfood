package com.eat.models.common;

import com.eat.models.common.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@ToString(exclude = "permissions")
@EqualsAndHashCode(of = {"name", "type"})
@Entity
@Table(name = "ROLE")
@ApiModel(value = "intended for users of whole application, e.g. for managers, app users, business users")
public class Role implements Serializable {

    private static final long serialVersionUID = -7383317484452637847L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID", updatable = false)
    @ApiModelProperty(value = "ID in the Database", allowableValues = "Long", required = true, position = 1)
    private Long id;

    @Column(name = "NAME", columnDefinition = "VARCHAR(255)", nullable = false)
    @ApiModelProperty(value = "roles title", allowableValues = "ADMINISTRATOR, MANAGER, USER", position = 2)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", columnDefinition = "VARCHAR(255)", nullable = false)
    private RoleType type;

    @JsonManagedReference(value = "role_permissions")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "role")
    private List<RolePermissions> permissions;

}
