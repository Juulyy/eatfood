package com.eat.repositories.sql.b2b;

import com.eat.models.b2b.MenuItem;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends BaseB2BRepository<MenuItem, Long> {

}