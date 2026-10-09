package com.ecommerce.dao;
import com.ecommerce.model.*; import java.sql.*;
public class UserDAO {
    public User findByEmail(String email) throws SQLException {
        String sql="SELECT * FROM users WHERE email=?";
        try(Connection c=DbConnection.get();PreparedStatement p=c.prepareStatement(sql)){p.setString(1,email);try(ResultSet r=p.executeQuery()){
            if(!r.next()) return null; int id=r.getInt("id"); String name=r.getString("name"), hash=r.getString("password_hash"), role=r.getString("role");
            return "ADMIN".equals(role)?new Admin(id,name,email,hash):new Customer(id,name,email,hash);
        }}
    }
}
