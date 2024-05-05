package Data_access;

import Models.OrderProducts;
import Connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderProductDAO extends AbstractDAO<OrderProducts>{
    public double getTotalAmountByOrderId(int orderId) {
        String query = "SELECT SUM(p.price * op.quantity) AS totalAmount " +
                "FROM Products p " +
                "JOIN OrderProducts op ON p.productId = op.productId " +
                "WHERE op.orderId = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("totalAmount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
