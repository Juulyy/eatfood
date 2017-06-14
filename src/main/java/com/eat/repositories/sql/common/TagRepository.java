package com.eat.repositories.sql.common;

import com.eat.models.common.Tag;
import com.eat.models.common.enums.TagType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("select v from Tag v where v.name = :nameParam")
    List<Tag> findByName(@Param("nameParam") String name);

    @Query("select distinct v from Tag v where v.name = :nameParam")
    Set<Tag> findDistinctByName(@Param("nameParam") String name);

    @Query("select distinct v from Tag v where v.name in :namesList")
    Set<Tag> findDistinctByNames(@Param("namesList") List<String> name);

    @Query("select v from Tag v where v.parent = :parentParam")
    List<Tag> findByParent(@Param("parentParam") Tag parent);

    @Query("select v from Tag v where v.type = :typeParam")
    List<Tag> findByType(@Param("typeParam") TagType type);

    Set<Tag> findByNameIn(Set<String> names);

    Tag findByNameAndType(String name, TagType type);

    void deleteByIdIn(List<Long> ids);

}