package br.com.devjf.salessync.dao;

import java.util.List;

public interface DAO<T> {
    boolean save(T object);
    boolean update(T object);
    boolean delete(Integer id);
    T findById(Integer id);
    List<T> findAll();
}