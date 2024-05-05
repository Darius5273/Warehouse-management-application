package Business_logic.Validators;

import Models.Products;

import java.math.BigDecimal;

public class PriceValidator implements Validator<Products>{
    public void validate(Products p) {
        if (p.getPrice().compareTo(new BigDecimal("0"))<=0) {
            throw new IllegalArgumentException("Price is not a valid price!");
        }
    }
}
