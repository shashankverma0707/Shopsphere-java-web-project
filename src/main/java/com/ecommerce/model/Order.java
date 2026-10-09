package com.ecommerce.model;
import java.math.BigDecimal;
public class Order {
    private int id; private int userId; private BigDecimal totalAmount; private String status;
    public Order(int id,int userId,BigDecimal totalAmount,String status){this.id=id;this.userId=userId;this.totalAmount=totalAmount;this.status=status;}
    public int getId(){return id;} public int getUserId(){return userId;} public BigDecimal getTotalAmount(){return totalAmount;} public String getStatus(){return status;}
}
