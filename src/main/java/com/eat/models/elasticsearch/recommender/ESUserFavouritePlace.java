package com.eat.models.elasticsearch.recommender;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ESUserFavouritePlace {

    @JsonProperty("user")
    private Map<String, String> userId;

    @JsonProperty("item")
    private Map<String, String> placeId;

    @JsonProperty("value")
    private float value;

    public ESUserFavouritePlace(long userId, long placeId, float value) {
        this.userId = new HashMap<String, String>() {{
            put("id", String.valueOf(userId));
        }};
        this.placeId = new HashMap<String, String>() {{
            put("id", String.valueOf(placeId));
        }};
        this.value = value;
    }

}
