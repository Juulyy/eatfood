package com.eat.models.recommender;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "id")
@EqualsAndHashCode(of = {"type", "rate"})
@Entity
@Table(name = "RANKING_RATE")
public class RankingRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RANKING_RATE_ID", updatable = false)
    private Long id;

    @Column
    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "TYPE", columnDefinition = "VARCHAR(255)", nullable = false)
    private RankingType type;

    @Column
    private BigDecimal rate;

}