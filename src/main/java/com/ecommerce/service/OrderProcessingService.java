package com.ecommerce.service;
import java.util.concurrent.*;
/** Demonstrates multithreading: order jobs are processed asynchronously. */
public class OrderProcessingService implements AutoCloseable {
    private final ExecutorService executor=Executors.newFixedThreadPool(3);
    public Future<String> processAsync(int orderId){return executor.submit(() -> {Thread.sleep(300);return "Order #"+orderId+" processing started by "+Thread.currentThread().getName();});}
    @Override public void close(){executor.shutdown();}
}
