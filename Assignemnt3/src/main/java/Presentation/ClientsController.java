package Presentation;
import Business_logic.ClientBLL;
import Models.Clients;
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

public class ClientsController {
    @FXML
    private TableView<Clients> tableView;
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


    private ObservableList<Clients> clientData = FXCollections.observableArrayList();
    private List<TextField> dynamicTextFields = new ArrayList<>();
    ControllerHelper<Clients> tableViewGenerator = new ControllerHelper<>();
    ClientBLL clientBLL = new ClientBLL();
    int clientId=-1;



    @FXML
    public void initialize() {

        tableViewGenerator.generateTableView(tableView, Clients.class,false);
        clientData.addAll(clientBLL.findAllClients());
        tableView.setItems(clientData);
        tableViewGenerator.addDynamicTextFields(vbox,Clients.class,dynamicTextFields);
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
        Clients selectedClient = tableView.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            clientId=selectedClient.getClientId();
            List<String> rowData = tableViewGenerator.getRowData(selectedClient,Clients.class);
            for (int i = 0; i < dynamicTextFields.size() && i < rowData.size(); i++) {
                TextField textField = dynamicTextFields.get(i);
                String value = rowData.get(i);
                textField.setText(value);
            }
        }
    }
    @FXML
    public void addBtnAction()
    {
        Clients client = new Clients();
        try {
             client = tableViewGenerator.createObjectFromTextFields(dynamicTextFields, Clients.class);
        }
        catch (IllegalArgumentException e)
        {
            tableViewGenerator.openAlert(e.getMessage());
        }
       clientData.add(clientBLL.insert(client));
       tableView.setItems(clientData);
    }
    @FXML
    public void editBtnAction()
    {
        Clients client = new Clients();
        try {
            client = tableViewGenerator.createObjectFromTextFields(dynamicTextFields, Clients.class);
        }
        catch (IllegalArgumentException e)
        {
            tableViewGenerator.openAlert(e.getMessage());
        }
        client.setClientId(clientId);
        clientBLL.update(client);
        tableView.getItems().clear();
        clientData.clear();
        clientData.addAll(clientBLL.findAllClients());
        tableView.setItems(clientData);
        clientId=-1;
    }
    @FXML
    public void deleteBtnAction()
    {
        Clients client = new Clients();
        try {
            client = tableViewGenerator.createObjectFromTextFields(dynamicTextFields, Clients.class);
        }
        catch (IllegalArgumentException e)
        {
            tableViewGenerator.openAlert(e.getMessage());
        }
        client.setClientId(clientId);
        clientBLL.delete(client);
        tableView.getItems().clear();
        clientData.clear();
        clientData.addAll(clientBLL.findAllClients());
        tableView.setItems(clientData);
        clientId=-1;
    }
}
