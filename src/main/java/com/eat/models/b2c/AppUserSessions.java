package com.eat.models.b2c;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "is the history of the user activity")
@Entity
@Table(name = "APP_USER_SESSIONS")
public class AppUserSessions implements Serializable {

    private static final long serialVersionUID = 1802751412916784526L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APP_USER_SESSION_ID", updatable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "APP_USER_FK")
    private AppUser user;

    @Column(name = "LOGIN_DATE")
    private LocalDate loginDate;

    @Column(name = "LOGOFF_DATE")
    private LocalDate logoffDate;

    @Column(name = "SESSION_TIME")
    private Integer sessionTime;
}
