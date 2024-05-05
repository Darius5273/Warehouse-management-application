package Data_access;

import Connection.ConnectionFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE "+field+" = ?");
        return sb.toString();
    }

    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + type.getSimpleName();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                T instance = createObject(resultSet);
                list.add(instance);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return list;
    }

    public T findById(String field, int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery(field);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            List<T> result = createObjects(resultSet);
            if (!result.isEmpty()) {
                return result.get(0);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<>();
        Constructor<T> ctor;
        try {
            ctor = type.getDeclaredConstructor();
            while (resultSet.next()) {
                T instance = ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | SQLException | NoSuchMethodException |
                 InvocationTargetException | IntrospectionException e) {
            LOGGER.log(Level.WARNING, "Error creating objects: " + e.getMessage());
        }
        return list;
    }
    private T createObject(ResultSet resultSet) {
        Constructor<T> ctor;
        try {
            ctor = type.getDeclaredConstructor();
            T instance = ctor.newInstance();
            for (Field field : type.getDeclaredFields()) {
                String fieldName = field.getName();
                Object value = resultSet.getObject(fieldName);
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                Method method = propertyDescriptor.getWriteMethod();
                method.invoke(instance, value);
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | SQLException | NoSuchMethodException |
                 InvocationTargetException | IntrospectionException e) {
            LOGGER.log(Level.WARNING, "Error creating object: " + e.getMessage());
        }
        return null;
    }
    public T insert(T t, String fieldId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        try {
            connection = ConnectionFactory.getConnection();
            for (Field field : type.getDeclaredFields()) {
                if (!field.getName().equals(fieldId)) {
                    columns.append(field.getName()).append(",");
                    values.append("?,");
                }
            }
            columns.deleteCharAt(columns.length() - 1);
            values.deleteCharAt(values.length() - 1);
            String query = "INSERT INTO " + type.getSimpleName() + " (" + columns.toString() + ") VALUES (" + values.toString() + ")";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            for (Field field : type.getDeclaredFields()) {
                if (!field.getName().equals(fieldId)) {
                    field.setAccessible(true);
                    statement.setObject(index++, field.get(t));
                }
            }
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insertion failed.");
            }
            resultSet = statement.getGeneratedKeys();
            if(!Objects.equals(fieldId, "")) {
                if (resultSet.next()) {
                    Field idField = type.getDeclaredField(fieldId);
                    idField.setAccessible(true);
                    idField.set(t, resultSet.getInt(1));
                } else {
                    throw new SQLException("Insertion failed, no ID obtained.");
                }
            }
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }

//    public T insert(T t,String fieldId) {
//        Connection connection = null;
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//        StringBuilder columns = new StringBuilder();
//        StringBuilder values = new StringBuilder();
//        try {
//            connection = ConnectionFactory.getConnection();
//            for (Field field : type.getDeclaredFields()) {
//                if (!field.getName().equals(fieldId)) {
//                    columns.append(field.getName()).append(",");
//                    values.append("?,");
//                }
//            }
//            columns.deleteCharAt(columns.length() - 1);
//            values.deleteCharAt(values.length() - 1);
//            String query = "INSERT INTO " + type.getSimpleName() + " (" + columns.toString() + ") VALUES (" + values.toString() + ")";
//            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
//            int index = 1;
//            for (Field field : type.getDeclaredFields()) {
//                field.setAccessible(true);
//                statement.setObject(index++, field.get(t));
//            }
//            int affectedRows = statement.executeUpdate();
//            if (affectedRows == 0) {
//                throw new SQLException("Insertion failed.");
//            }
//            resultSet = statement.getGeneratedKeys();
//            if (resultSet.next()) {
//                Field idField = type.getDeclaredField(fieldId);
//                idField.setAccessible(true);
//                idField.set(t, resultSet.getInt(1));
//            } else {
//                throw new SQLException("Insertion failed, no ID obtained.");
//            }
//        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
//            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
//        } finally {
//            ConnectionFactory.close(resultSet);
//            ConnectionFactory.close(statement);
//            ConnectionFactory.close(connection);
//        }
//        return t;
//    }

    public T update(T t,String fieldId) {
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuilder setClause = new StringBuilder();
        try {
            connection = ConnectionFactory.getConnection();
            for (Field field : type.getDeclaredFields()) {
                if (!field.getName().equals(fieldId)) {
                    setClause.append(field.getName()).append("=?,");
                }
            }
            setClause.deleteCharAt(setClause.length() - 1);
            String query = "UPDATE " + type.getSimpleName() + " SET " + setClause.toString() + " WHERE " +fieldId+" =?";
            statement = connection.prepareStatement(query);
            int index = 1;
            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                if (!field.getName().equals(fieldId)) {
                    statement.setObject(index++, field.get(t));
                }
            }
            Field idField = type.getDeclaredField(fieldId);
            idField.setAccessible(true);
            statement.setInt(index, (Integer) idField.get(t));
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }
    public void delete(T t, String fieldId) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            String query = "DELETE FROM " + type.getSimpleName() + " WHERE " + fieldId + "=?";
            statement = connection.prepareStatement(query);

            Field idField = type.getDeclaredField(fieldId);
            idField.setAccessible(true);
            Object idValue = idField.get(t);
            statement.setObject(1, idValue);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deletion failed, no rows affected.");
            }
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }
}

