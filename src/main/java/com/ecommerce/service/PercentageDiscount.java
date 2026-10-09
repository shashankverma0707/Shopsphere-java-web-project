package com.ecommerce.service;
import java.math.*;
public class PercentageDiscount implements DiscountStrategy {
    private final BigDecimal percent; public PercentageDiscount(BigDecimal percent){this.percent=percent;}
    public BigDecimal apply(BigDecimal subtotal){return subtotal.subtract(subtotal.multiply(percent).divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP));}
}
