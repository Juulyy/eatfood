package com.eat.models.b2c;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "APP_USER_TOKENS")
public class AppUserToken implements Serializable {

    private static final long serialVersionUID = -7576005033598880682L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false)
    private Long id;

    @OneToOne(targetEntity = AppUser.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER", referencedColumnName = "USER_ID")
    private AppUser appUser;

    @Column(name = "TOKEN_TYPE", columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "EXPIRY_DATE")
    private LocalDate expiryDate;

    @Getter
    public enum TokenType {

        REGISTRATION_TOKEN(1, "REGISTRATION"),
        RESET_PASSWORD_TOKEN(2, "RESET_PASSWORD");

        private Integer id;

        private String tokenType;

        TokenType(Integer id, String tokenType) {
            this.id = id;
            this.tokenType = tokenType;
        }

    }

}
