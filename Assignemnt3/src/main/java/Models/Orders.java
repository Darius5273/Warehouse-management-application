package Models;

import java.sql.Date;

public class Orders {
    private int orderId;
    private int clientId;
    private Date orderDate;

    public Orders()
    {

    }
    public Orders(int clientId, Date orderDate) {
        super();
        this.clientId = clientId;
        this.orderDate = orderDate;
    }

    public Orders(int orderId, int clientId, Date orderDate) {
        super();
        this.orderId = orderId;
        this.clientId = clientId;
        this.orderDate = orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}
