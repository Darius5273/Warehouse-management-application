package Business_logic;


import Data_access.BillDAO;
import Models.Bill;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class BillBLL {
    private BillDAO billDAO;
    public BillBLL()
    {
        billDAO = new BillDAO();
    }
    public void insert(int orderId) {
        OrderBLL orderBLL=new OrderBLL();
        OrderProductBLL orderProductsBLL=new OrderProductBLL();
        var order=orderBLL.findOrderById(orderId);
        order.setOrderDate(Date.valueOf(LocalDate.now()));
        orderBLL.update(order);
        BigDecimal totalAmount= BigDecimal.valueOf(orderProductsBLL.getTotalAmountByOrderId(orderId));
        var bill=new Bill(orderId,order.getOrderDate(),totalAmount);
        billDAO.insertBill(bill);
    }

    public List<Bill> findAllBills()
    {
        return billDAO.findAll();
    }
}
