package com.panaderia.ruminahui.model;

import java.util.List;

public interface Repository<T> {
    void save(T entity);
    T findById(String id);
    List<T> findAll();
    void update(T entity);
    void delete(String id);
}