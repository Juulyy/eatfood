package com.eat.models.elasticsearch;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ESUser extends ESAbstractObject {
    @JsonProperty("user_id")
    private long id;

    public ESUser(long id, List<String> tags) {
        super(id, tags);
    }
}
