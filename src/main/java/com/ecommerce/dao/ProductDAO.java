package com.ecommerce.dao;

import com.ecommerce.model.Product;
import java.sql.*; import java.util.*;

public class ProductDAO implements CrudDAO<Product> {
    private Product map(ResultSet rs) throws SQLException {
        return new Product(rs.getInt("id"),rs.getString("name"),rs.getString("description"),rs.getBigDecimal("price"),rs.getInt("stock"),rs.getString("category"),rs.getString("image_url"));
    }
    @Override public Product findById(int id) throws SQLException { String sql="SELECT * FROM products WHERE id=?"; try(Connection c=DbConnection.get();PreparedStatement p=c.prepareStatement(sql)){p.setInt(1,id);try(ResultSet r=p.executeQuery()){return r.next()?map(r):null;}}}
    @Override public List<Product> findAll() throws SQLException { List<Product> out=new ArrayList<>(); try(Connection c=DbConnection.get();PreparedStatement p=c.prepareStatement("SELECT * FROM products ORDER BY id DESC");ResultSet r=p.executeQuery()){while(r.next())out.add(map(r));} return out; }
    public List<Product> search(String q) throws SQLException { List<Product> out=new ArrayList<>(); String sql="SELECT * FROM products WHERE name LIKE ? OR category LIKE ? ORDER BY id DESC"; try(Connection c=DbConnection.get();PreparedStatement p=c.prepareStatement(sql)){String x="%"+q+"%";p.setString(1,x);p.setString(2,x);try(ResultSet r=p.executeQuery()){while(r.next())out.add(map(r));}} return out; }
    @Override public int save(Product x) throws SQLException {String sql="INSERT INTO products(name,description,price,stock,category,image_url) VALUES(?,?,?,?,?,?)";try(Connection c=DbConnection.get();PreparedStatement p=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){p.setString(1,x.getName());p.setString(2,x.getDescription());p.setBigDecimal(3,x.getPrice());p.setInt(4,x.getStock());p.setString(5,x.getCategory());p.setString(6,x.getImageUrl());p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){return r.next()?r.getInt(1):0;}}}
    @Override public boolean update(Product x) throws SQLException {String sql="UPDATE products SET name=?,description=?,price=?,stock=?,category=?,image_url=? WHERE id=?";try(Connection c=DbConnection.get();PreparedStatement p=c.prepareStatement(sql)){p.setString(1,x.getName());p.setString(2,x.getDescription());p.setBigDecimal(3,x.getPrice());p.setInt(4,x.getStock());p.setString(5,x.getCategory());p.setString(6,x.getImageUrl());p.setInt(7,x.getId());return p.executeUpdate()>0;}}
    @Override public boolean delete(int id) throws SQLException {try(Connection c=DbConnection.get();PreparedStatement p=c.prepareStatement("DELETE FROM products WHERE id=?")){p.setInt(1,id);return p.executeUpdate()>0;}}
}
