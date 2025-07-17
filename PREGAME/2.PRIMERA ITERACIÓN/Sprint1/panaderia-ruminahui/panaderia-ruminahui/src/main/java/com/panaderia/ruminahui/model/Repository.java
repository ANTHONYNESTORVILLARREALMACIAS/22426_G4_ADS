package com.panaderia.ruminahui.model;

public interface Repository<T> {
    void save(T entity);
    T findById(String id);
    java.util.List<T> findAll();
    void update(T entity);
    void delete(String id);
}