package com.eat.models.mongo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Locale;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "FAQ's for app/business users")
@Document(collection = "faqs")
public class FAQ implements Serializable {

    private static final long serialVersionUID = 1363460691027196484L;

    @Id
    private String id;

    @Field(value = "locale")
    private Locale locale;

    @Field(value = "type")
    @Enumerated(EnumType.STRING)
    private FAQType type;

    @Field(value = "question")
    @TextIndexed(weight = 3)
    private String question;

    @Field(value = "answer")
    @TextIndexed(weight = 3)
    private String answer;

    @Getter
    public enum FAQType {

        B2B(1, "B2B"), B2C(2, "B2C");

        private Integer id;

        private String type;

        FAQType(Integer id, String type) {
            this.id = id;
            this.type = type;
        }

    }

}