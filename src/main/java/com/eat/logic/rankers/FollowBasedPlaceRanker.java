package com.eat.logic.rankers;

import com.eat.logic.rankers.wrappers.Rankable;
import com.eat.models.b2b.Place;
import com.eat.models.b2c.AppUser;
import com.eat.models.b2c.AppUserCollection;
import com.eat.models.b2c.AppUserPreference;
import com.eat.models.recommender.RankingRate;
import com.eat.models.recommender.RankingType;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

public class FollowBasedPlaceRanker extends AbstractRankingCounter<Place> {

    public FollowBasedPlaceRanker(List<RankingRate> rankingRates) {
        super(rankingRates);
    }

    @Override
    public void countRank(Rankable<Place> rankable) {

       /* Set<AppUser> followings = new HashSet<>(rankable.getUser().getUserPreferences().getFollowing());
        for (AppUser following : followings) {
            rankable.increaseRank(getRateByFollowing(following, rankable));
        }

        Set<AppUser> followers = new HashSet<>(rankable.getUser().getUserPreferences().getFollowers());
        for (AppUser follower : followers) {
            rankable.increaseRank(getRateByFollowing(follower, rankable));
        }

        proceed(rankable);*/
    }

    private BigDecimal getRateByFollowing(AppUser user, Rankable<Place> rankable) {

        RankingType rankingType = user.isCurator()
                ? RankingType.RECOMMENDED_BY_FOLLOWING_CURATOR : RankingType.RECOMMENDED_BY_FOLLOWING;
        AppUserPreference userPreferences = user.getUserPreferences();

        return getRateByFavorites(userPreferences.getCollections(), rankable, rankingType);
    }

    private BigDecimal getRateByFollower(AppUser user, Rankable<Place> rankable) {

        RankingType rankingType = user.isCurator()
                ? RankingType.RECOMMENDED_BY_CURATOR : RankingType.RECOMMENDED_BY_USER;

        List<AppUserCollection> favorites = user.getUserPreferences().getCollections();

        return getRateByFavorites(favorites, rankable, rankingType);
    }

    private BigDecimal getRateByFavorites(List<AppUserCollection> userCollections, Rankable<Place> rankable, RankingType rankingType){

        for (AppUserCollection collection : userCollections) {
            HashSet<Place> places = new HashSet<>(collection.getPlaces());
            if(places.contains(rankable.getRankableObject())){
                return getRateByType(rankingType);
            }
        }

        return BigDecimal.ZERO;
    }

    private BigDecimal getRateByCollections(List<AppUserCollection> collections, Rankable<Place> rankable, FollowType followType){

        /*for (AppUserCollection collection : collections) {
            if(collection.getCollectionType() == AppUserCollection.CollectionType.RECOMMENDED_PLACES){
                getRateByType(followType == FOLLOWING ?  )
            }
        }*/

        return BigDecimal.ZERO;
    }

    public enum FollowType{
        FOLLOWING, FOLLOWER
    }
}
