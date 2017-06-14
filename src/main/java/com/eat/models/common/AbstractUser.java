package com.eat.models.common;

import com.eat.models.common.enums.RoleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"firstName", "lastName", "login", "email"})
@ToString(of = {"firstName", "lastName", "login", "email"})
@MappedSuperclass
@ApiModel(value = "is the abstract user who is the ancestor of the descendants, e.g. BusinessUser, AppUser")
public abstract class AbstractUser implements Serializable {

    private static final long serialVersionUID = 8657926332173851328L;

//    @NonNull
//    @NotEmpty
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

//    @NonNull
//    @NotEmpty
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

//    @NonNull
//    @NotEmpty
    @Size(min = 3, max = 20, message = "Login length must between 3 and 20 characters.")
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Login must contain no special symbols!")
    @Column(name = "LOGIN", nullable = false)
    private String login;

//    @NonNull
//    @NotEmpty
    @Size(min = 8, message = "Password length must between 8.")
    @Pattern.List({
            @Pattern(regexp = "(?=.*[0-9]).+", message = "Password must contain one digit."),
            @Pattern(regexp = "(?=.*[a-z]).+", message = "Password must contain one lowercase letter."),
            @Pattern(regexp = "(?=.*[A-Z]).+", message = "Password must contain one uppercase letter."),
            @Pattern(regexp = "(?=\\S+$).+", message = "Password must contain no whitespace.")
    })
    @Column(name = "PASSWORD", nullable = false)
    private String password;

//    @NonNull
//    @NotEmpty
    @Email
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ROLE_FK", referencedColumnName = "ROLE_ID")
    private Role role;

    @Column(name = "IS_ENABLED")
    @ApiModelProperty(value = "allowing to be in currently usage", allowableValues = "Boolean", required = true)
    private boolean isEnabled;

    public boolean isCurator() {
        return role.getType() == RoleType.ROLE_APP_CURATOR;
    }

}