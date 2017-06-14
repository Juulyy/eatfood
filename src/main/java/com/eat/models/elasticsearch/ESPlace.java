package com.eat.models.elasticsearch;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ESPlace extends ESAbstractObject {
    @JsonProperty("place_id")
    private long id;

    public ESPlace(long id, List<String> tags) {
        super(id, tags);
    }
}
