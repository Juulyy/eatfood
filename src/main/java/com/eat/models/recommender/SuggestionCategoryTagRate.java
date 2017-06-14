package com.eat.models.recommender;

import com.eat.models.common.enums.TagType;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@ApiModel("Tag type rates for suggestion categories ranking by tag")
@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SuggestionCategoryTagRate {

//    TODO change to enumerated
    @Column(name = "TAG_TYPE")
    private TagType tagType;

    @Column(name = "RATE")
    private Double rate;

}
