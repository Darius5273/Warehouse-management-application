package Models;

public class OrderProducts {
    private int orderId;
    private int productId;
    private int quantity;
    public OrderProducts(){

    }
    public OrderProducts(int productId, int quantity) {
        super();
        this.productId = productId;
        this.quantity = quantity;
    }
    public OrderProducts(int orderId, int productId, int quantity) {
        super();
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}