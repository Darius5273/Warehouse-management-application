package Business_logic.Validators;

import Models.OrderProducts;

public class QuantityValidator implements Validator<OrderProducts>{
    public void validate(OrderProducts o) {
        if (o.getQuantity()<=0) {
            throw new IllegalArgumentException("Quantity is not valid!");
        }
    }
}