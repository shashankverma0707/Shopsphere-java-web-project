package com.ecommerce.dao;
import com.ecommerce.model.CartItem; import java.math.BigDecimal; import java.sql.*; import java.util.Map;
public class OrderDAO {
    public int createOrder(int userId, Map<Integer,CartItem> cart) throws SQLException {
        if(cart.isEmpty()) throw new IllegalArgumentException("Cart is empty");
        try(Connection c=DbConnection.get()) { c.setAutoCommit(false);
            try {
                BigDecimal total=cart.values().stream().map(CartItem::getSubtotal).reduce(BigDecimal.ZERO,BigDecimal::add);
                int orderId;
                try(PreparedStatement p=c.prepareStatement("INSERT INTO orders(user_id,total_amount,status) VALUES(?,?,?)",Statement.RETURN_GENERATED_KEYS)){
                    p.setInt(1,userId);p.setBigDecimal(2,total);p.setString(3,"PLACED");p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){if(!r.next())throw new SQLException("Order ID was not generated");orderId=r.getInt(1);}
                }
                for(CartItem item:cart.values()){
                    try(PreparedStatement stock=c.prepareStatement("SELECT stock FROM products WHERE id=? FOR UPDATE")){stock.setInt(1,item.getProduct().getId());try(ResultSet r=stock.executeQuery()){if(!r.next()||r.getInt(1)<item.getQuantity())throw new SQLException("Insufficient stock for "+item.getProduct().getName());}}
                    try(PreparedStatement i=c.prepareStatement("INSERT INTO order_items(order_id,product_id,quantity,unit_price) VALUES(?,?,?,?)")){i.setInt(1,orderId);i.setInt(2,item.getProduct().getId());i.setInt(3,item.getQuantity());i.setBigDecimal(4,item.getProduct().getPrice());i.executeUpdate();}
                    try(PreparedStatement u=c.prepareStatement("UPDATE products SET stock=stock-? WHERE id=?")){u.setInt(1,item.getQuantity());u.setInt(2,item.getProduct().getId());u.executeUpdate();}
                }
                c.commit(); return orderId;
            } catch(SQLException|RuntimeException ex){c.rollback();throw ex;} finally {c.setAutoCommit(true);}
        }
    }
}
