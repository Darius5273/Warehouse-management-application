package Presentation;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ControllerHelper<T> {
    public void generateTableView(TableView<T> tableView, Class<T> classType,boolean skip) {
        TableColumn<T, String> headerCol = new TableColumn<>(classType.getSimpleName());
        tableView.getColumns().add(headerCol);
        Field[] fields = classType.getDeclaredFields();
        boolean firstFieldSkipped = skip;

        for (Field field : fields) {
            if (!firstFieldSkipped) {
                firstFieldSkipped = true;
                continue;
            }
            TableColumn<T, Object> column = new TableColumn<>(field.getName());
            column.setCellValueFactory(cellData -> {
                try {
                    field.setAccessible(true);
                    Object value = field.get(cellData.getValue());
                    return new SimpleObjectProperty<>(value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return new SimpleObjectProperty<>("");
                }
            });
            column.setEditable(true);
            headerCol.getColumns().add(column);
        }
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    public boolean areAllFieldsFilled(List<TextField> textFields) {
        for (TextField textField : textFields) {
            if (textField.getText().isEmpty()) {
                openAlert("Please fill in all fields");
                return false;
            }
        }
        return true;
    }
    public void addDynamicTextFields(VBox container, Class<?> clas, List<TextField> dynamicTextFields) {
        Field[] fields = clas.getDeclaredFields();

        for (int i = 1; i < fields.length; i++) {
            Field field = fields[i];

            TextField textField = new TextField();
            textField.setPromptText(field.getName());
            textField.setStyle("-fx-padding: 5px;");
            container.getChildren().add(textField);
            dynamicTextFields.add(textField);
        }
    }
    public <T> T createObjectFromTextFields(List<TextField> textFields, Class<T> clas) {
        if (areAllFieldsFilled(textFields)) {
            try {
                Constructor<T> constructor = clas.getDeclaredConstructor();
                constructor.setAccessible(true);

                T object = constructor.newInstance();

                Field[] fields = clas.getDeclaredFields();

                for (int i = 1; i < fields.length && i - 1 < textFields.size(); i++) {
                    Field field = fields[i];
                    TextField textField = textFields.get(i - 1);
                    field.setAccessible(true);
                    String fieldValue = textField.getText();
                    Object value = parseValue(field.getType(), fieldValue);
                    field.set(object, value);
                }

                return object;
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }


    private Object parseValue(Class<?> type, String value) {
        if (type.equals(String.class)) {
            return value;
        } else if (type.equals(int.class)) {
            return Integer.parseInt(value);
        } else if (type.equals(BigDecimal.class)) {
            return new BigDecimal(value);
        } else if (type.equals(Date.class)) {
            return Date.valueOf(value);
        }
        return null;
    }
    public List<String> getRowData(T object, Class<T> clas) {
        List<String> rowData = new ArrayList<>();
        try {
            Field[] fields = clas.getDeclaredFields();
            boolean firstFieldSkipped = false;
            for (Field field : fields) {
                if (!firstFieldSkipped) {
                    firstFieldSkipped = true;
                    continue;
                }
                field.setAccessible(true);
                Object value = field.get(object);
                if(value!=null)
                    rowData.add(value.toString());
                else
                    rowData.add("");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return rowData;
    }
    public void openAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Status");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
