package com.ecommerce.service;
import com.ecommerce.dao.ProductDAO; import com.ecommerce.model.Product; import java.sql.SQLException; import java.util.List;
public class ProductService {
    private final ProductDAO dao=new ProductDAO();
    public List<Product> list(String q) throws SQLException {return q==null||q.isBlank()?dao.findAll():dao.search(q.trim());}
    public Product get(int id) throws SQLException {return dao.findById(id);}
    public int add(Product p) throws SQLException {return dao.save(p);} public boolean update(Product p)throws SQLException{return dao.update(p);} public boolean delete(int id)throws SQLException{return dao.delete(id);}
}
