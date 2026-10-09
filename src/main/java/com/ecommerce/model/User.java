package com.ecommerce.model;

public abstract class User {
    private int id;
    private String name;
    private String email;
    private String passwordHash;

    protected User(int id, String name, String email, String passwordHash) {
        this.id = id; this.name = name; this.email = email; this.passwordHash = passwordHash;
    }
    public int getId(){ return id; }
    public String getName(){ return name; }
    public String getEmail(){ return email; }
    public String getPasswordHash(){ return passwordHash; }
    public abstract String getRole();
}
