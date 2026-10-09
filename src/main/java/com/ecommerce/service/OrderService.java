package com.ecommerce.service;
import com.ecommerce.dao.OrderDAO; import com.ecommerce.exception.*; import com.ecommerce.model.CartItem; import java.sql.SQLException; import java.util.Map;
public class OrderService {
    private final OrderDAO dao=new OrderDAO();
    public int checkout(int userId,Map<Integer,CartItem> cart) throws AppException {
        try{return dao.createOrder(userId,cart);}catch(SQLException|RuntimeException e){throw new AppException("Checkout failed: "+e.getMessage(),e);}
    }
}
