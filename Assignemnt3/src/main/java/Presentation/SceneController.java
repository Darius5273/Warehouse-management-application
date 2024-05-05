package Presentation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class SceneController {
    @FXML
    private BorderPane main_border;

    @FXML
    private void showPane1() {
        switchPane(loadAnchorPane("/com/example/assignemnt3/Clients.fxml"));
    }

    @FXML
    private void showPane2() {
        switchPane(loadAnchorPane("/com/example/assignemnt3/Products.fxml"));
    }
    @FXML
    private void showPane3() {
        switchPane(loadAnchorPane("/com/example/assignemnt3/Orders.fxml"));
    }

    private AnchorPane loadAnchorPane(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            return loader.load();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    private void switchPane(AnchorPane newPane) {
        if (newPane != null) {
            main_border.setCenter(newPane);
        }
    }
}
