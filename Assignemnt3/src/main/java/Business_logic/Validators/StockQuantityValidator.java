package Business_logic.Validators;

import Models.Products;

public class StockQuantityValidator implements Validator<Products>{
    public void validate(Products p) {
        if (p.getStockQuantity()<=0) {
            throw new IllegalArgumentException("Stock quantity is not valid!");
        }
    }
}
