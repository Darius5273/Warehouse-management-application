package Data_access;

import Models.Bill;
import Connection.ConnectionFactory;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BillDAO {
    private static final Logger LOGGER = Logger.getLogger(BillDAO.class.getName());



    public void insertBill(Bill bill) {
        String query = "INSERT INTO Bill (orderid, orderdate, totalamount) VALUES (?, ?, ?)";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bill.orderId());
            statement.setDate(2, java.sql.Date.valueOf(bill.orderDate().toString()));
            statement.setBigDecimal(3, bill.totalAmount());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error inserting bill: " + e.getMessage());
        }
    }

    public List<Bill> findAll() {
        List<Bill> records = new ArrayList<>();
        String query = "SELECT * FROM Bill";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                var record = new Bill(resultSet.getInt(2), resultSet.getDate(3), resultSet.getBigDecimal(4));
                records.add(record);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error occurred while fetching data from table: Bill", e);
        }
        return records;
    }
}
