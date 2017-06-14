package com.eat.models.recommender;

import lombok.Getter;


public enum RankingType {

    RECOMMENDED_BY_FOLLOWING_CURATOR(1L,"Recommended by following curator", DataType.FOLLOW, 10L),
    RECOMMENDED_BY_CURATOR(2L, "Recommended by curator", DataType.FOLLOW, 9L),
    RECOMMENDED_BY_FOLLOWING(3L, "Recommended by following user", DataType.FOLLOW, 8L),
    RECOMMENDED_BY_USER(4L, "Recommended by user", DataType.FOLLOW, 7L),
    RECOMMENDED_BY_SIMILAR_USER(5L, "Recommended by similar user", DataType.FOLLOW, 6L),

    ADDED_BY_FOLLOWING_CURATOR(6L, "Added to collection by following curator", DataType.COLLECTION, 10L),
    ADDED_BY_CURATOR(7L, "Added to collection by by curator", DataType.COLLECTION, 9L),
    ADDED_BY_FOLLOWING(8L, "Added to collection by following", DataType.COLLECTION, 8L),
    ADDED_BY_USER(9L, "Added to collection by user", DataType.COLLECTION, 7L),
    ADDED_BY_SIMILAR_USER(10L, "Added to collection by similar user", DataType.COLLECTION, 6L),

    MENU_ITEM(11L, "Menu item", DataType.TAG, 10L),
    CUISINE(12L, "Cuisine", DataType.TAG, 9L),
    PLACE_TYPE(13L, "Place type", DataType.TAG, 8L),
    FEATURE(14L, "Feature", DataType.TAG, 7L),
    ATMOSPHERE(15L, "Atmosphere", DataType.TAG, 6L),
    INTERIOR_DESIGN(16L, "Interior design", DataType.TAG, 5L),
    MUSIC(17L, "Music", DataType.TAG, 4L);

    @Getter
    private Long id;
    @Getter
    private String name;
    @Getter
    private Long defaultRate;

    private DataType dataType;

    RankingType(Long id, String name, DataType dataType, Long defaultRate) {
        this.id = id;
        this.name = name;
        this.dataType = dataType;
        this.defaultRate = defaultRate;
    }

    public boolean isTagType(){
        return this.dataType == DataType.TAG;
    }

    public boolean isFollowType(){
        return this.dataType == DataType.FOLLOW;
    }

    public boolean isCollectionType(){
        return this.dataType == DataType.COLLECTION;
    }

    private enum DataType {
        TAG, FOLLOW, COLLECTION
    }

}
