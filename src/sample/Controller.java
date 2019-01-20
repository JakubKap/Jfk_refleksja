package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    @FXML
    TextField textFieldArg1;

    @FXML
    TextField textFieldArg2;

    @FXML
    TextField textFieldRes;


    MethodsImport methodsImport;

    List<Method> methodList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        methodsImport = new MethodsImport();
        methodList = new ArrayList<>();
    }

    public void btnOpenClicked(ActionEvent event){

        if(methodList.size()>0) methodList.clear();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));

        Stage stage = (Stage) anchorPane.getScene().getWindow();
        selectedDirectory = directoryChooser.showDialog(stage);

        if(directoryChooser == null)
            return;

        listView.getItems().clear();


        methodsImport.importClasses(selectedDirectory);

        //zapełnienie ListView nazwami metod
        methodList = methodsImport.getMethodList();

        for(Method m : methodList){
            listView.getItems().add(m.toString());

        }

    }

    @FXML public void handleMouseClick(MouseEvent event) {
        textFieldArg1.setText("");
        textFieldArg2.setText("");
        textFieldRes.setText("");

        if(methodList.get(listView.getSelectionModel().getSelectedIndex()).getParameterCount() == 1)
            textFieldArg2.setDisable(true);

            else if(methodList.get(listView.getSelectionModel().getSelectedIndex()).getParameterCount() == 2){
                textFieldArg1.setDisable(false);
                textFieldArg2.setDisable(false);
            }


    }

    public void btnRunClicked(ActionEvent event){

        String par1 = textFieldArg1.getText();
        String par2 = textFieldArg2.getText();

        Object[] args = new Object[]{par1, par2};

        Method method = methodList.get(listView.getSelectionModel().getSelectedIndex());
        String result = methodsImport.invokeMethod(method, args);

        textFieldRes.setText(result);


    }

}
