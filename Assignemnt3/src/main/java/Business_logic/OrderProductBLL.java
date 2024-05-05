package Business_logic;

import Business_logic.Validators.QuantityValidator;
import Business_logic.Validators.Validator;
import Data_access.OrderProductDAO;
import Models.OrderProducts;
import java.util.List;

public class OrderProductBLL {

    private Validator<OrderProducts> validator= new QuantityValidator();
    private OrderProductDAO orderProductDAO;

    public OrderProductBLL() {
        orderProductDAO = new OrderProductDAO();
    }

    public OrderProducts insert(OrderProducts orderProduct) {
        validator.validate(orderProduct);
        return orderProductDAO.insert(orderProduct,"");
    }

    public List<OrderProducts> findAllOrderProducts() {
        return orderProductDAO.findAll();
    }
    public double getTotalAmountByOrderId(int orderId)
    {
        return orderProductDAO.getTotalAmountByOrderId(orderId);
    }
}
