package com.ecommerce.service;
/** Synchronization demo for concurrent inventory reservations. */
public class StockManager {
    private int stock;
    public StockManager(int initialStock){this.stock=initialStock;}
    public synchronized boolean reserve(int quantity){if(quantity<=0||quantity>stock)return false;stock-=quantity;return true;}
    public synchronized int getStock(){return stock;}
}
