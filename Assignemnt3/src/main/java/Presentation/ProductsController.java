package Presentation;

import Business_logic.ProductBLL;
import Models.Products;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ProductsController {
    @FXML
    private TableView<Products> tableView;
    @FXML
    private AnchorPane pane;
    @FXML
    private Button add;
    @FXML
    private Button edit;
    @FXML
    private Button delete;
    @FXML
    private VBox vbox;

    private ObservableList<Products> productData = FXCollections.observableArrayList();
    ControllerHelper<Products> tableViewGenerator = new ControllerHelper<>();
    private List<TextField> dynamicTextFields = new ArrayList<>();
    private ProductBLL productBLL = new ProductBLL();
    int productId=0;

    @FXML
    public void initialize() {

        tableViewGenerator.generateTableView(tableView, Products.class,false);
        productData.addAll(productBLL.findAllProducts());
        tableView.setItems(productData);
        tableViewGenerator.addDynamicTextFields(vbox,Products.class,dynamicTextFields);
        setTableRowClickListener();
        add.setOnAction(event->addBtnAction());
        edit.setOnAction(event->editBtnAction());
        delete.setOnAction(event->deleteBtnAction());
    }
    private void setTableRowClickListener() {
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                fillTextFieldsFromSelectedRow();
            }
        });
    }

    private void fillTextFieldsFromSelectedRow() {
        Products selectedProduct = tableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            productId=selectedProduct.getProductId();
            List<String> rowData = tableViewGenerator.getRowData(selectedProduct,Products.class);
            for (int i = 0; i < dynamicTextFields.size() && i < rowData.size(); i++) {
                TextField textField = dynamicTextFields.get(i);
                String value = rowData.get(i);
                textField.setText(value);
            }
        }
    }
    @FXML
    private void addBtnAction()
    {
        var product = new Products();
        try {
            product = tableViewGenerator.createObjectFromTextFields(dynamicTextFields, Products.class);
        }
        catch (IllegalArgumentException e)
        {
            tableViewGenerator.openAlert(e.getMessage());
        }
        productData.add(productBLL.insert(product));
        tableView.setItems(productData);
    }
    @FXML
    private void editBtnAction()
    {
        var product = new Products();
        try {
             product = tableViewGenerator.createObjectFromTextFields(dynamicTextFields, Products.class);
        }
        catch (IllegalArgumentException e)
        {
            tableViewGenerator.openAlert(e.getMessage());
        }
        product.setProductId(productId);
        productBLL.update(product);
        tableView.getItems().clear();
        productData.clear();
        productData.addAll(productBLL.findAllProducts());
        tableView.setItems(productData);
        productId=-1;
    }
    @FXML
    private void deleteBtnAction()
    {
        var product = new Products();
        try {
            product = tableViewGenerator.createObjectFromTextFields(dynamicTextFields, Products.class);
        }
         catch (IllegalArgumentException e)
        {
            tableViewGenerator.openAlert(e.getMessage());
        }
        product.setProductId(productId);
        productBLL.delete(product);
        tableView.getItems().clear();
        productData.clear();
        productData.addAll(productBLL.findAllProducts());
        tableView.setItems(productData);
        productId=-1;
    }
}
