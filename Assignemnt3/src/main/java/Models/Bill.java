package Models;

import java.math.BigDecimal;
import java.sql.Date;
public record Bill(int orderId, Date orderDate, BigDecimal totalAmount) {

}
