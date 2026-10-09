package com.ecommerce.model;

import java.math.BigDecimal;

public class Product {
    private int id; private String name; private String description; private BigDecimal price;
    private int stock; private String category; private String imageUrl;
    public Product() {}
    public Product(int id,String name,String description,BigDecimal price,int stock,String category,String imageUrl){
        this.id=id;this.name=name;this.description=description;this.price=price;this.stock=stock;this.category=category;this.imageUrl=imageUrl;
    }
    public int getId(){return id;} public String getName(){return name;} public String getDescription(){return description;}
    public BigDecimal getPrice(){return price;} public int getStock(){return stock;} public String getCategory(){return category;} public String getImageUrl(){return imageUrl;}
    public void setId(int id){this.id=id;} public void setName(String v){name=v;} public void setDescription(String v){description=v;}
    public void setPrice(BigDecimal v){price=v;} public void setStock(int v){stock=v;} public void setCategory(String v){category=v;} public void setImageUrl(String v){imageUrl=v;}
}
