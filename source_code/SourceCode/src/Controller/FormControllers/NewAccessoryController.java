package Controller.FormControllers;

import Controller.Main;
import Model.Accessory;
import Model.Equipment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class NewAccessoryController {

    ////////////////////////
    //Variable Declaration//
    ////////////////////////
    @FXML
    private TextField AccName,AmountInStock;

    ////////////////
    //Initializers//
    ////////////////
    public static void setView() throws Exception{
        Main.getPopupWindow().setWidth(275);
        Main.getPopupWindow().setHeight(225);
        Main.setPopupWindow("Forms/NewAccessoryForm.fxml");
    }

    //public void initialize(URL url, ResourceBundle resourceBundle){ }


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

    @FXML
    void submitNewAccessory() throws Exception{
        if(validateForm()) {
            Accessory.insertNewAccessory(new Accessory(
                    AccName.getText(),
                    Integer.parseInt(AmountInStock.getText())
            ));
        }
    }


    ///////////////////
    //Form Validation//
    ///////////////////
    private boolean validateForm() {
        AccName.setStyle(null);
        AmountInStock.setStyle(null);


        if(AccName.getText().matches("^(?=.*[a-zA-Z]).*$")
                && AmountInStock.getText().matches("^(?=.*[0-9]).*$")){
            return true;
        }
        if(!AccName.getText().matches("^(?=.*[a-zA-Z]).*$")){
            AccName.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }
        if(AmountInStock.getText().matches("^(?=.*[0-9]).*$")){
            AmountInStock.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }

        return false;
    }
}
