package com.eat.models.b2b;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "id")
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "CREDIT_CARD_TYPE")
public class CreditCardType implements Serializable {

    private static final long serialVersionUID = -6931795175187631959L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CREDIT_CARD_TYPE_ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

}
