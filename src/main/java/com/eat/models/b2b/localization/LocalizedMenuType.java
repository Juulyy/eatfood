/*
package com.eat.models.b2b.localization;

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
@Table(name = "LOCALIZED_MENU_TYPE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalizedMenuType {

    private static final long serialVersionUID = -2711117987709812727L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "LOCALE")
    private Locale locale;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "MEAL_TYPE_FK", referencedColumnName = "MEAL_TYPE_ID")
    private MealType mealType;

}
*/
