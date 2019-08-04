package Controller.FormControllers;

import Controller.Main;
import Model.Equipment;
import Model.FXModifiers.ComboBoxAutoComplete;
import Model.Worksite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NewSystemFormController implements Initializable {

    ////////////////////////
    //Variable Declaration//
    ////////////////////////
    @FXML
    private ComboBox<String> MakeComboBox,ModelComboBox,TypeComboBox;

    @FXML
    private TextField SerialNumberField;

    private ArrayList makeList, modelList;


    ////////////////
    //Initializers//
    ////////////////
    public static void setView() throws Exception{
        Main.getPopupWindow().setWidth(250);
        Main.getPopupWindow().setHeight(315);
        Main.setPopupWindow("Forms/NewComputerSystem.fxml");
    }

    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            makeList = Equipment.generateMakeList();

            MakeComboBox.setItems(FXCollections.observableArrayList(makeList));
            MakeComboBox.valueProperty().addListener((ov, oldValue, newValue) -> {
                try {
                    ModelComboBox.setDisable(false);
                    modelList = Equipment.generateModelList(MakeComboBox.getSelectionModel().getSelectedItem());
                    ModelComboBox.setItems(FXCollections.observableArrayList(modelList));
                }catch (Exception e) { }
            });

            MakeComboBox.getEditor().textProperty().addListener((ov, oldValue, newValue) -> {
                try {
                    ModelComboBox.setDisable(false);
                }catch (Exception e) { }
            });

            ObservableList<String> list = FXCollections.observableArrayList();
            list.add("Laptop");
            list.add("Desktop");
            TypeComboBox.setItems(list);
        }catch (Exception e){ e.printStackTrace(); }
    }


    ///////////////////
    //List Generators//
    ///////////////////


    //////////////////
    //Button Methods//
    //////////////////
    @FXML private void close()throws Exception{
        Main.getPopupWindow().close();
        Main.getOuter().setDisable(false);
        Main.setCenterPane("Lists/EquipmentList.fxml");
    }

    @FXML private void submitSystem() throws Exception {
        int makeID, modelID = 0;
        MakeComboBox.setStyle(null);
        ModelComboBox.setStyle(null);
        SerialNumberField.setStyle(null);
        TypeComboBox.setStyle(null);
        if (validateForm()) {

            if (!makeList.contains(MakeComboBox.getSelectionModel().getSelectedItem())) {
                makeID = Equipment.insertNewMake(MakeComboBox.getSelectionModel().getSelectedItem());
            }else {
                makeID = Equipment.queryMakeByName(MakeComboBox.getSelectionModel().getSelectedItem());
            }

            if (!modelList.contains(ModelComboBox.getSelectionModel().getSelectedItem())) {
                modelID = Equipment.insertNewModel(ModelComboBox.getSelectionModel().getSelectedItem(), makeID);
            } else {
                modelID = Equipment.queryModelByName(ModelComboBox.getSelectionModel().getSelectedItem(), makeID);
            }

            Equipment.updateComputerModels(modelID, makeID, ModelComboBox.getSelectionModel().getSelectedItem());
            Equipment.insertNewEquipment(SerialNumberField.getText(), TypeComboBox.getSelectionModel().getSelectedItem(), modelID);

           close();
        }
        else{
            if(MakeComboBox.getValue() == null){
                MakeComboBox.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            }
            if(ModelComboBox.getValue() == null){
                ModelComboBox.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            }
            if(SerialNumberField.getText().trim().isEmpty()){
                SerialNumberField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            }
            if(TypeComboBox.getSelectionModel().isEmpty()){
                TypeComboBox.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            }
        }
    }


    ///////////////////
    //Form Validation//
    ///////////////////
    private boolean validateForm() {
        if(MakeComboBox.getValue() == null || ModelComboBox.getValue() == null ||
        !SerialNumberField.getText().matches("^(?=.*[a-zA-Z0-9-_#$&*+~.]).*$") || TypeComboBox.getSelectionModel().isEmpty()){
            return false;
        }
        else
            return true;
    }
}
