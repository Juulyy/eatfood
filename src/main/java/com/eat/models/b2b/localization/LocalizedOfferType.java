package com.eat.models.b2b.localization;

import com.eat.models.b2b.OfferAction;
import com.eat.models.b2b.OfferType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@ToString(exclude = "id")
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "LOCALIZED_OFFER_TYPE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalizedOfferType {

    private static final long serialVersionUID = -2720917987705392727L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "LOCALE")
    private Locale locale;

    @ManyToOne
    @JoinColumn(name = "OFFER_TYPE_FK", referencedColumnName = "OFFER_TYPE_ID")
    private OfferType offerType;

}
