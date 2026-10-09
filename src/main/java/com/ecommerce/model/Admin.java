package com.ecommerce.model;
public class Admin extends User {
    public Admin(int id,String name,String email,String passwordHash){ super(id,name,email,passwordHash); }
    @Override public String getRole(){ return "ADMIN"; }
}
