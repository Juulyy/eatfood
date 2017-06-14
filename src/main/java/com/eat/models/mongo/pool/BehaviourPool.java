package com.eat.models.mongo.pool;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder(builderMethodName = "of", buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "behaviour_pool_stats")
public class BehaviourPool implements Serializable {

    private static final long serialVersionUID = -8165729687413418085L;

    @JsonProperty(value = "id")
    @Id
    private String id;

    @NotNull
    @JsonProperty(value = "sql_user_id")
    @Indexed(name = "sql_user_id", unique = true)
    private Long sqlUserId;

    @NotNull
    @JsonProperty(value = "pool")
    @Field(value = "pool")
    private Set<TagContext> pool;

}