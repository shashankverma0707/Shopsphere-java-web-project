package com.ecommerce.model;
public class Customer extends User {
    public Customer(int id,String name,String email,String passwordHash){ super(id,name,email,passwordHash); }
    @Override public String getRole(){ return "CUSTOMER"; }
}
