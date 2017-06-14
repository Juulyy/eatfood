package com.eat.models.mongo;

import com.eat.models.mongo.enums.ContactReportType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Contact reports from business users")
@Document(collection = "contact_reports")
public class ContactReport implements Serializable {

    private static final long serialVersionUID = 970239373279297383L;

    @Id
    private String id;

    @Field(value = "date")
    private LocalDateTime date;

    @Field(value = "place_id")
    private String placeId;

    @Field(value = "user_id")
    private String userId;

    @Field(value = "first_name")
    private String firstName;

    @Field(value = "last_name")
    private String lastName;

    @Field(value = "role")
    private String role;

    @Field(value = "type")
    private ContactReportType type;

    @TextIndexed(weight = 3)
    @Field(value = "message")
    @Size(max = 255)
    private String message;
}