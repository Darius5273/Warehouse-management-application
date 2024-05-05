package Business_logic;

import Data_access.OrderDAO;
import Models.Orders;

import java.util.List;
import java.util.NoSuchElementException;
public class OrderBLL {

    private OrderDAO orderDAO;

    public OrderBLL() {
        orderDAO = new OrderDAO();
    }

    public Orders findOrderById(int id) {
        Orders order = orderDAO.findById("orderId", id);
        if (order == null) {
            throw new NoSuchElementException("The order with id =" + id + " was not found!");
        }
        return order;
    }

    public Orders insert(Orders order) {
        return orderDAO.insert(order, "orderId");
    }

    public Orders update(Orders order) {
        return orderDAO.update(order, "orderId");
    }

    public List<Orders> findAllOrders() {
        return orderDAO.findAll();
    }
}