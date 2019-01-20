package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    @FXML
    AnchorPane anchorPane;

    @FXML
    File selectedDirectory;

    @FXML
    Button btnOpen;

    @FXML
    ListView listView;

    MethodsImport methodsImport;

    public void btnOpenClicked(ActionEvent event){

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));

        Stage stage = (Stage) anchorPane.getScene().getWindow();
        selectedDirectory = directoryChooser.showDialog(stage);

        if(directoryChooser == null)
            return;

            listView.getItems().clear();


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        methodsImport = new MethodsImport();
    }
}
