package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
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

    @FXML
    Label wrongArgsLabel;

    @FXML
    Label metadaneLabel;

    @FXML
    Label nazwaLabel;

    @FXML
    Label nazwaValue;

    @FXML
    Label klasaLabel;

    @FXML
    Label klasaValue;

    @FXML
    Label returnLabel;

    @FXML
    Label returnValue;

    @FXML
    Label paramesLabel;

    @FXML
    Label paramesValue;

    @FXML
    Label interLabel;

    @FXML
    Label interValue;

    MethodsImport methodsImport;

    List<Method> methodList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        methodsImport = new MethodsImport();
        methodList = new ArrayList<>();
        wrongArgsLabel.setVisible(false);
        clearMetaData();
    }

    public void btnOpenClicked(ActionEvent event){

        wrongArgsLabel.setVisible(false);
        textFieldArg1.setText("");
        textFieldArg2.setText("");
        textFieldRes.setText("");
        clearMetaData();

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
        if(listView.getSelectionModel().isEmpty())
            return;

        wrongArgsLabel.setVisible(false);
        textFieldArg1.setText("");
        textFieldArg2.setText("");
        textFieldRes.setText("");

        if(methodList.get(listView.getSelectionModel().getSelectedIndex()).getParameterCount() == 1)
            textFieldArg2.setDisable(true);

            else if(methodList.get(listView.getSelectionModel().getSelectedIndex()).getParameterCount() == 2){
                textFieldArg1.setDisable(false);
                textFieldArg2.setDisable(false);
            }


            //wypisanie metadanych metody

        Method method = methodList.get(listView.getSelectionModel().getSelectedIndex());
            String methodParameters="";
            StringBuilder sB1 = new StringBuilder(methodParameters);

            Type[] methodParams = method.getGenericParameterTypes();
            for(int i=0; i<methodParams.length; i++)
                sB1.append(methodParams[i].getTypeName() + " ");

            methodParameters = sB1.toString();

        String methodInterfaces="";
        StringBuilder sB2 = new StringBuilder(methodInterfaces);

        Type[] methodInterfs = method.getDeclaringClass().getGenericInterfaces();
        for(int i=0; i<methodInterfs.length; i++)
            sB2.append(methodInterfs[i] + " ");

        methodInterfaces = sB2.toString();

        metadaneLabel.setVisible(true);

        nazwaLabel.setVisible(true);
        nazwaValue.setVisible(true);
        nazwaValue.setText(method.getName());

        klasaLabel.setVisible(true);
        klasaValue.setVisible(true);
        klasaValue.setText(method.getDeclaringClass().toString().substring(6));

        returnLabel.setVisible(true);
        returnValue.setVisible(true);
        returnValue.setText(method.getReturnType().getTypeName());

        paramesLabel.setVisible(true);
        paramesValue.setVisible(true);
        paramesValue.setText(methodParameters);

        interLabel.setVisible(true);
        interValue.setVisible(true);
        interValue.setText(methodInterfaces.substring(10));




    }

    public void btnRunClicked(ActionEvent event){

        if(listView.getItems().isEmpty() || listView.getSelectionModel().isEmpty())
            return;

        String par1 = textFieldArg1.getText();
        String par2 = textFieldArg2.getText();

        Object[] args = new Object[]{par1, par2};

        Method method = methodList.get(listView.getSelectionModel().getSelectedIndex());
        String result;
        try {
            result = methodsImport.invokeMethod(method, args);
            wrongArgsLabel.setVisible(false);
            textFieldRes.setText(result);
        } catch (IllegalArgumentException e){
            wrongArgsLabel.setVisible(true);
        }

    }

    public void clearMetaData(){
        metadaneLabel.setVisible(false);

        nazwaLabel.setVisible(false);
        nazwaValue.setVisible(false);

        klasaLabel.setVisible(false);
        klasaValue.setVisible(false);

        returnLabel.setVisible(false);
        returnValue.setVisible(false);

        paramesLabel.setVisible(false);
        paramesValue.setVisible(false);

        interLabel.setVisible(false);
        interValue.setVisible(false);
    }



}
