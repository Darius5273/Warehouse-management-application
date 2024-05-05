package Data_access;

import Connection.ConnectionFactory;
import Models.Products;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ProductDAO extends AbstractDAO<Products>{

    public int getStockQuantity(int productId) {
        int stockQuantity = 0;

        String query = "SELECT stockquantity FROM products WHERE productId = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    stockQuantity = resultSet.getInt("stockquantity");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());

        }

        return stockQuantity;
    }
    public int getProductIdByName(String productName) {
        int productId = -1;

        String query = "SELECT productId FROM products WHERE name = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, productName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    productId = resultSet.getInt("productId");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return productId;
    }
    public List<String> getClientsByName(String startingName) {
        List<String> products = new ArrayList<>();
        String query = "SELECT name FROM products WHERE LOWER(name) LIKE ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, startingName.toLowerCase() + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return products;
    }
}
