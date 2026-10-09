package com.ecommerce.service;
import java.math.BigDecimal;
public class NoDiscount implements DiscountStrategy { public BigDecimal apply(BigDecimal subtotal){return subtotal;} }
