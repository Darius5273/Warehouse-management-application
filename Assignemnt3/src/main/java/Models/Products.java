package Models;

import java.math.BigDecimal;

public class Products {
    private int productId;
    private String name;
    private BigDecimal price;
    private int stockQuantity;

    public Products()
    {}
    public Products(String name, BigDecimal price, int stockQuantity) {
        super();
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
    public Products(int productId, String name, BigDecimal price, int stockQuantity) {
        super();
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
