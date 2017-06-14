package com.eat.models.mongo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.validator.constraints.Email;
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
@ApiModel(value = "Problem reports from app/business users")
@Document(collection = "problem_reports")
public class ProblemReport implements Serializable {

    private static final long serialVersionUID = -3377105519392247812L;

    @Id
    private String id;

    @Field(value = "time")
    private LocalDateTime time;

    @Field(value = "first_name")
    private String firstName;

    @Field(value = "last_name")
    private String lastName;

    @Email
    @Field(value = "email")
    private String email;

    @Field(value = "description")
    @Size(max = 140)
    private String description;

}
