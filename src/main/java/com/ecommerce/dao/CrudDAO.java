package com.ecommerce.dao;
import java.sql.SQLException;
import java.util.List;
public interface CrudDAO<T> {
    T findById(int id) throws SQLException;
    List<T> findAll() throws SQLException;
    int save(T entity) throws SQLException;
    boolean update(T entity) throws SQLException;
    boolean delete(int id) throws SQLException;
}
