package com.eat.models.mongo.pool;

import lombok.Getter;

@Getter
public enum PoolContextRate {

    /**
     * REG_PREF by (#Cuisine, #Feature, #Atmosphere, #Music, #Interior) x0.024
     * REG_PREF by #Menu_Item(0.024) -> #Menu_Category(0.024/2) -> #Menu_Group(0.024/4)
     * REG_PREF by #Menu_Category(0.024/2) -> #Menu_Group(0.024/2)
     * REG_PREF by #Menu_Group(0.024)
     */
    REG_PREF(0.024),

    /**
     * REC_MENU_ITEM by #Menu_Item(0.024) -> #Menu_Category(0.024/2) -> #Menu_Group(0.024/4)
     */
    REC_MENU_ITEM(0.024),

    /**
     * REC_FEATURE by #Feature(0.024)
     */
    REC_FEATURE(0.024),

    /**
     * REC_PLACE by (#Place_Type, #Cuisine, #Feature, #Atmosphere, #Music, #Interior) x0.012
     */
    REC_PLACE(0.012),

    /**
     * REC_PLACE by #Menu_Category(0.008) -> #Menu_Group(0.008/2)
     */
    REC_PLACE_MENU_ITEM(0.008),

    /**
     * VISITED_PLACE_BY_PLAN by (#Cuisine, #Feature, #Atmosphere, #Music, #Interior) x0.006
     */
    VISITED_PLACE_BY_PLAN(0.006),

    /**
     * VISITED_PLACE_BY_PLAN_MENU_CATEGORY by #Menu_Category(0.004) -> #Menu_Group(0.004/2)
     */
    VISITED_PLACE_BY_PLAN_MENU_CATEGORY(0.004),

    /**
     * VISITED_PLACE_AUTOCHECK by (#Cuisine, #Feature, #Atmosphere, #Music, #Interior) x0.003
     */
    VISITED_PLACE_AUTOCHECK(0.003),

    /**
     * VISITED_PLACE_AUTOCHECK_MENU_CATEGORY by #Menu_Category(0.002) -> #Menu_Group(0.002/2)
     */
    VISITED_PLACE_AUTOCHECK_MENU_CATEGORY(0.002),

    /**
     * FOLLOWED_PLACE (#Cuisine, #Feature, #Atmosphere, #Music, #Interior) x0.0015
     */
    FOLLOWED_PLACE(0.0015),

    /**
     * FOLLOWED_PLACE_MENU_ITEM by #Menu_Category(0.00075) -> #Menu_Group(0.00075/2)
     */
    FOLLOWED_PLACE_MENU_ITEM(0.00075);

    private Double weight;

    PoolContextRate(Double weight) {
        this.weight = weight;
    }

}