package com.eat.dto.b2b;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of", buildMethodName = "create")
@SqlResultSetMapping(name="PlaceRecommendationsDtoMapping",
        classes=@ConstructorResult(targetClass=PlaceRecommendationsDto.class,
                columns={@ColumnResult(name="id"), @ColumnResult(name="amount")}))
@NamedNativeQuery(name ="PlaceRecommendationsDto.getPlacesRecommendationsAmount",
        query = "SELECT usr_col_places.place_fk as id, Count(usr_col_places.place_fk) as amount FROM\n" +
        "    app_user_collection AS usr_col,\n" +
        "    app_user_collections_places AS usr_col_places\n" +
        "WHERE\n" +
        "    usr_col.user_preference_fk IN (SELECT usr.user_id FROM\n" +
        "            role AS role, app_user AS usr WHERE usr.role_fk = role.role_id\n" +
        "                AND role.type = 'ROLE_APP_CURATOR')\n" +
        "        AND usr_col_places.place_fk in :ids\n" +
        "        AND usr_col.collection_id = usr_col_places.collection_fk\n" +
        "        AND usr_col.collection_type = 'RECOMMENDED_PLACES'\n" +
        "Group By usr_col_places.place_fk", resultSetMapping="PlaceRecommendationsDtoMapping")
public class PlaceRecommendationsDto {

    @NonNull
    @JsonProperty(value = "id")
    private Long id;

    @NonNull
    @JsonProperty(value = "amount")
    private Integer amount;
}
