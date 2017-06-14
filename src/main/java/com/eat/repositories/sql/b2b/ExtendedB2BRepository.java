package com.eat.repositories.sql.b2b;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface ExtendedB2BRepository<T, ID extends Serializable> extends BaseB2BRepository<T, ID>{

    List<T> findByNameIgnoreCase(@Param("name") String name);

    List<T> findByNameContainingIgnoreCase(@Param("name") String name);

}
