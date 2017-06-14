package com.eat.models.mongo;

import com.eat.models.mongo.enums.ClaimRequestStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel("Request from user for claiming some place as his own business")
@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "claim_requests")
public class ClaimRequest implements Serializable {

    private static final long serialVersionUID = -8136602735126572521L;

    @Id
    private String id;

    @Field(value = "date")
    private LocalDateTime date;

    @Field(value = "status")
    @Enumerated(EnumType.STRING)
    private ClaimRequestStatus status;

    @Field(value = "place_id")
    private String placeId;

    @Field(value = "user_id")
    private String userId;

    @Size(max = 25)
    @Field(value = "first_name")
    private String firstName;

    @Size(max = 25)
    @Field(value = "last_name")
    private String lastName;

    @Size(max = 46)
    @Email
    @Field(value = "eamil")
    private String email;

    @Size(max = 15)
    @Field(value = "phone")
    private String phone;

    @TextIndexed
    @Size(max = 80)
    @Field(value = "place_name")
    private String placeName;

    @TextIndexed
    @Size(max = 120)
    @Field(value = "place_address")
    private String placeAddress;

}