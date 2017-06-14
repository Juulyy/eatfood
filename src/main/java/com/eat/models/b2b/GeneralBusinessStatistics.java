package com.eat.models.b2b;

import lombok.*;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@Entity
@Table(name = "GENERAL_BUSINESS_STATISTICS")
public class GeneralBusinessStatistics implements Serializable {

    private static final long serialVersionUID = -763190651369634722L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUSINESS_USER_FK", referencedColumnName = "BUSINESS_USER_ID", unique = true)
    private BusinessUser businessUser;

    @Column(name = "FOLLOWERS")
    private Integer followers;

    @Column(name = "VIEWS")
    private Integer views;

    @Column(name = "RECOMMENDATION")
    private Integer recommendation;

}
