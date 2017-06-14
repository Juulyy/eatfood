package com.eat.models.common;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@Entity
@Table(name = "ROLE_PERMISSIONS", uniqueConstraints = @UniqueConstraint(columnNames = {"ROLE_FK", "ACCESS_OBJECT"}))
@ApiModel(value = "designed for permissions distribution within users of the application")
public class RolePermissions implements Serializable {

    private static final long serialVersionUID = 3135158444211002057L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false)
    @ApiModelProperty(value = "ID in the Database", allowableValues = "Long", required = true, position = 1)
    private Long id;

    @JsonBackReference(value = "role_permissions")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLE_FK", referencedColumnName = "ROLE_ID")
    private Role role;

    @Column(name = "ACCESS_OBJECT")
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "for application users allowed to appropriate views", allowableValues = "MENU, MENU_ITEMS, BUSINESS_USERS", position = 2)
    private AccessObject accessObject;

    @Column(name = "CAN_ADD")
    @ApiModelProperty(value = "the permission to the addition via the Role Permission", allowableValues = "Boolean", required = true, position = 3)
    private Boolean canAdd;

    @Column(name = "CAN_READ")
    @ApiModelProperty(value = "the permission to the reading via the Role Permission", allowableValues = "Boolean", required = true, position = 4)
    private Boolean canView;

    @Column(name = "CAN_EDIT")
    @ApiModelProperty(value = "the permission to the editing via the Role Permission", allowableValues = "Boolean", required = true, position = 5)
    private Boolean canEdit;

    @Column(name = "CAN_DELETE")
    @ApiModelProperty(value = "the permission to the deleting via the Role Permission", allowableValues = "Boolean", required = true, position = 6)
    private Boolean canDelete;

    @Column(name = "CAN_VIEW_REPORTS")
    @ApiModelProperty(value = "the permission to the viewing the reports via the Role Permission", allowableValues = "Boolean", required = true, position = 7)
    private Boolean canViewReports;

    public enum AccessObject {

        DEFAULT, OFFER, TEMPLATE_OFFER, MENU, MENU_ITEMS, BUSINESS_USERS;

        AccessObject() {
        }

    }

}
