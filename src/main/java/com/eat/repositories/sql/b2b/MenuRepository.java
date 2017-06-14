package com.eat.repositories.sql.b2b;

import com.eat.models.b2b.Menu;
import com.eat.models.b2b.Place;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends ExtendedB2BRepository<Menu, Long> {

    List<Menu> findByPlace(Place place);

}