package com.nebarrow.dao;

import java.util.List;
import java.util.Optional;

public interface ICrudDao<T> {
    T create(T entity);

    Optional<T> update(T entity);

    List<T> findAll();

    Optional<T> findByName(String name);
}
