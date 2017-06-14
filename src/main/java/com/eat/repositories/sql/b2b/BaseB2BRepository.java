package com.eat.repositories.sql.b2b;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
//@RestResource(exported = false)
@RepositoryRestResource(exported = false)
public interface BaseB2BRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    List<T> findByIdIn(List<Long> ids);

    @Override
    List<T> findAll();

    @Override
    List<T> findAll(Sort sort);

    @Override
    List<T> findAll(Iterable<ID> iterable);

    @Override
    <S extends T> List<S> findAll(Example<S> example);

    @Override
    <S extends T> List<S> findAll(Example<S> example, Sort sort);

    @Override
    Page<T> findAll(Pageable pageable);

    @Override
    <S extends T> List<S> save(Iterable<S> iterable);

    @Override
    void flush();

    @Override
    <S extends T> S saveAndFlush(S s);

    @Override
    void deleteInBatch(Iterable<T> iterable);

    @Override
    void deleteAllInBatch();

    @Override
    T getOne(ID id);

    @Override
    <S extends T> S save(S s);

    @Override
    T findOne(ID id);

    @Override
    boolean exists(ID id);

    @Override
    long count();

    @Override
    void delete(ID id);

    @Override
    void delete(T t);

    @Override
    void delete(Iterable<? extends T> iterable);

    @Override
    void deleteAll();

    @Override
    <S extends T> S findOne(Example<S> example);

    @Override
    <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);

    @Override
    <S extends T> long count(Example<S> example);

    @Override
    <S extends T> boolean exists(Example<S> example);

}
