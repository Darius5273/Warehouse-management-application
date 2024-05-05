package Presentation;

import Business_logic.*;
import Models.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class OrdersController {
    @FXML
    private Button addProductBtn;

    @FXML
    private ComboBox<String> clientCb;

    @FXML
    private Button createOrderBtn;

    @FXML
    private ComboBox<String> productCb;
    @FXML
    private Button completeOrderBtn;

    @FXML
    private TextField quantityTf;
    @FXML
    private Label label;
    @FXML
    private TableView<Bill> tableView;
    ControllerHelper<Bill> tableViewGenerator = new ControllerHelper<>();
    private ObservableList<Bill> billData = FXCollections.observableArrayList();
    private BillBLL billBLL= new BillBLL();
    private OrderBLL orderBLL = new OrderBLL();
    private OrderProductBLL orderProductsBLL = new OrderProductBLL();
    private int orderId=-1;
    private boolean clientsActive=true;
    private boolean productsActive=true;

    @FXML
    public void initialize() {

        tableViewGenerator.generateTableView(tableView, Bill.class,true);
        ChangeListener<String> clientsListener = (observable, oldValue, newValue) -> {
            if(clientsActive) {
                if (newValue == null || newValue.isEmpty()) {
                    populateComboBoxWithAllClients();
                } else {
                    updateComboBoxWithFilteredClients(newValue);
                }
            }
        };
        clientCb.getEditor().textProperty().addListener(clientsListener);
        populateComboBoxWithAllClients();

        ChangeListener<String> productsListener = (observable, oldValue, newValue) -> {
            if(productsActive) {
                if (newValue == null || newValue.isEmpty()) {
                    populateComboBoxWithAllProducts();
                } else {
                    updateComboBoxWithFilteredProducts(newValue);
                }
            }
        };
        productCb.getEditor().textProperty().addListener(productsListener);
        populateComboBoxWithAllProducts();

        billData.addAll(billBLL.findAllBills());
        tableView.setItems(billData);
        createOrderBtn.setOnAction(event->createOrder());
        addProductBtn.setOnAction(event->addProduct());
        completeOrderBtn.setOnAction(event->completeOrder());
    }
    @FXML
    private void createOrder()
    {
        label.setVisible(false);
        if(clientCb.getSelectionModel().getSelectedItem()!=null||!clientCb.getEditor().getText().isEmpty()) {
            String clientName=clientCb.getSelectionModel().getSelectedItem().toString();
            int clientId = new ClientBLL().getIdByName(clientName);
            var order = new Orders(clientId, null);
            orderBLL.insert(order);
            orderId = order.getOrderId();
        }
        else {
            tableViewGenerator.openAlert("No client was selected!");
        }
    }
    @FXML
    private void addProduct()
    {
        label.setVisible(false);
        if(orderId!=-1)
        {
            if(productCb.getSelectionModel().getSelectedItem()!=null&&!quantityTf.getText().isEmpty()) {
                String productName=productCb.getSelectionModel().getSelectedItem();
                var productBll = new ProductBLL();
                int productId = productBll.getIdByName(productName);
                int quantity = Integer.parseInt(quantityTf.getText());
                if (quantity > productBll.getStockQuantity(productId))
                    label.setVisible(true);
                else {
                    var orderProducts = new OrderProducts();
                    try {
                         orderProducts = new OrderProducts(orderId, productId, quantity);
                         var p=productBll.findProductById(productId);
                         p.setStockQuantity(p.getStockQuantity()-quantity);
                         productBll.update(p);
                    }
                    catch (IllegalArgumentException e)
                    {
                        tableViewGenerator.openAlert(e.getMessage());
                    }
                    orderProductsBLL.insert(orderProducts);
                }
            }
            else {
                tableViewGenerator.openAlert("Please fill in all fields!");
            }
        }
    }
    @FXML
    private void completeOrder()
    {
        label.setVisible(false);
        if(orderId!=-1)
        {
            billBLL.insert(orderId);
            tableView.getItems().clear();
            billData.clear();
            billData.addAll(billBLL.findAllBills());
            tableView.setItems(billData);
            orderId=-1;
        }
    }

    private void populateComboBoxWithAllClients()
    {
        clientsActive=false;
        ObservableList<String> clientNames = FXCollections.observableArrayList();
        List<Clients> allClients = new ClientBLL().findAllClients();
        for (Clients client : allClients) {
            clientNames.add(client.getName());
        }
        clientCb.setValue(null);
        clientCb.setItems(clientNames);
        clientsActive=true;
    }
    private void updateComboBoxWithFilteredClients(String name)
    {
        clientsActive=false;
        ObservableList<String> clientNames = FXCollections.observableArrayList();
        List<String> filteredClients = new ClientBLL().filterClients(name);
        for (String client : filteredClients) {
            clientNames.add(client);
        }
        clientCb.setValue(name);
        clientCb.setItems(clientNames);
        clientsActive=true;
    }
    private void populateComboBoxWithAllProducts()
    {
        productsActive=false;
        ObservableList<String> productNames = FXCollections.observableArrayList();
        List<Products> allProducts = new ProductBLL().findAllProducts();
        for (Products product : allProducts) {
            productNames.add(product.getName());
        }
        productCb.setValue(null);
        productCb.setItems(productNames);
        productsActive=true;
    }
    private void updateComboBoxWithFilteredProducts(String name)
    {
        productsActive=false;
        ObservableList<String> productNames = FXCollections.observableArrayList();
        List<String> filteredProducts = new ProductBLL().filterProducts(name);
        for (String product : filteredProducts) {
            productNames.add(product);
        }
        productCb.setValue(name);
        productCb.setItems(productNames);
        productsActive=true;
    }
}