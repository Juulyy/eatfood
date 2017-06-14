package com.eat.models.mongo.pool;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Comparator;

@Builder(builderMethodName = "of", buildMethodName = "create")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagContext implements Serializable, Comparator<TagContext>, Comparable<TagContext> {

    private static final long serialVersionUID = 217778379143748838L;

    @NonNull
    private String tagName;

    @NonNull
    private Double weight;

    @NonNull
    private PoolContextType type;

    @Override
    public int compare(TagContext o1, TagContext o2) {
        return Double.compare(o1.getWeight(), o2.getWeight());
    }

    @Override
    public int compareTo(TagContext o) {
        return Double.compare(o.getWeight() == null ? 0 : o.getWeight(),
                this.getWeight() == null ? 0 : this.getWeight());
    }

}