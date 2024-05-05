package Data_access;

import Models.Clients;
import Connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ClientDAO extends AbstractDAO<Clients>{
    public int getClientIdByName(String clientName) {
        int clientId = -1;
        String query = "SELECT clientId FROM clients WHERE name = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, clientName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    clientId = resultSet.getInt("clientId");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return clientId;
    }
    public List<String> getClientsByName(String startingName) {
        List<String> clients = new ArrayList<>();
        String query = "SELECT name FROM clients WHERE LOWER(name) LIKE ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, startingName.toLowerCase() + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    clients.add(resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return clients;
    }

}
