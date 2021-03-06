package com.eat.models.b2b.localization;

import com.eat.models.b2b.OfferAction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.*;

import javax.persistence.*;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@ToString(exclude = "id")
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "LOCALIZED_OFFER_ACTION")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalizedOfferAction implements Serializable{

    private static final long serialVersionUID = -2720917987705392727L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "LOCALE")
    private Locale locale;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "OFFER_ACTION_FK", referencedColumnName = "OFFER_ACTION_ID")
    private OfferAction offerAction;

}
