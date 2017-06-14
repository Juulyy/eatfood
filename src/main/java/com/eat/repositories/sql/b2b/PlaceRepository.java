package com.eat.repositories.sql.b2b;

import com.eat.models.b2b.Place;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends BaseB2BRepository<Place, Long> {

    @Query("select p from Place p where p.name = :nameParam and p.latitude = :latitudeParam and p.longtitude = :longtitudeParam")
    Place findByNameAndLatitudeAndLongtitude(@Param("nameParam") String name, @Param("latitudeParam") Double latitude, @Param("longtitudeParam") Double longtitude);

//    List<Place> findByBusinessUserId(Long id);

    @Query(value = "SELECT * FROM (SELECT *, ((ACOS( "+
            "SIN(:latitude * PI() / 180) * SIN(p.LATITUDE * PI() / 180)" +
                    "COS(:latitude * PI() / 180) * COS(p.LATITUDE * PI() / 180) * " +
                            "COS((:longtitude - p.LONGTITUDE) * PI() / 180)) * 180 / PI())" +
            " * 60 * 1.1515) * 1.609344 AS distance" +
            "FROM place as p) as pdist WHERE distance <= :radius ORDER BY distance", nativeQuery = true)
    List<Place> getPlacesInRadius(@Param("latitude") Double latitude, @Param("longtitude") Double longtitude, @Param("radius") Integer radius);


    @Query("select e from Place e where isActive = true")
    List<Place> findByIsActiveTrue();

    @Query("select e from Place e where tags in :tags")
    List<Place> findByTags();

    @Query("select e from Place e where schedule is null")
    List<Place> findPlacesWithoutSchedule();

    @Query("select e from Place e where id in :ids")
    List<Place> findPlacesByIdIn(@Param("ids") List<Long> ids);

    @Query(value = "SELECT Count(*) FROM\n" +
            "    app_user_collection AS usr_col,\n" +
            "    app_user_collections_places AS usr_col_places\n" +
            "WHERE\n" +
            "    usr_col.user_preference_fk IN (SELECT usr.user_id FROM\n" +
            "            role AS role, app_user AS usr WHERE usr.role_fk = role.role_id\n" +
            "                AND role.type = 'ROLE_APP_CURATOR')\n" +
            "        AND usr_col_places.place_fk = :placeId\n" +
            "        AND usr_col.collection_id = usr_col_places.collection_fk\n" +
            "        AND usr_col.collection_type = 'RECOMMENDED_PLACES'", nativeQuery = true)
    Integer getPlaceRecommendationsCount(@Param("placeId") Long placeId);

    @Query(value = "SELECT usr_col_places.place_fk as id, Count(*) as amount FROM\n" +
            "    app_user_collection AS usr_col,\n" +
            "    app_user_collections_places AS usr_col_places\n" +
            "WHERE\n" +
            "    usr_col.user_preference_fk IN (SELECT usr.user_id FROM\n" +
            "            role AS role, app_user AS usr WHERE usr.role_fk = role.role_id\n" +
            "                AND role.type = 'ROLE_APP_CURATOR')\n" +
            "        AND usr_col_places.place_fk in :ids\n" +
            "        AND usr_col.collection_id = usr_col_places.collection_fk\n" +
            "        AND usr_col.collection_type = 'RECOMMENDED_PLACES'\n" +
            "Group By usr_col_places.place_fk", nativeQuery = true)
    Object[] getPlacesRecommendationsCount(@Param("ids") List<Long> ids);

//    //@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
//    @Override
//    void deleteAll();
//
//    //@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
//    //@Override
//    //Page<Establishment> findAll(Pageable pageable);
//
//    //@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
//    //@Override
//    //Iterable<Establishment> findAll(Iterable<Long> iterable);
//
//    @RestResource(rel = "name", path = "name")
//    List<Establishment> findByNameIgnoreCase(@Param("name") String name);
//
//    @RestResource(rel = "name-like", path = "name-like")
//    List<Establishment> findByNameContainingIgnoreCase(@Param("name") String name);
//
//

}