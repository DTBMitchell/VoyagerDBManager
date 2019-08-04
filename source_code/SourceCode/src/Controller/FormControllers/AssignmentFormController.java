package Controller.FormControllers;

import Controller.ListControllers.AssignmentListController;
import Controller.ListControllers.EquipmentListController;
import Controller.Main;
import Controller.ViewerControllers.AssignmentInformationController;
import Controller.ViewerControllers.SystemInformationController;
import Controller.ViewerControllers.UserFocusController;
import Model.*;
import Model.FXModifiers.ComboBoxAutoComplete;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static Model.User.generateUserList;

public class AssignmentFormController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML ComboBox<Equipment>   systemBox;
    @FXML ComboBox<User>        userBox;
    @FXML ComboBox<String>      serialBox;
    @FXML ComboBox<Accessory>   accessoryBox;
    @FXML ListView<Accessory>   AccessoryList;
    @FXML Text                  fullName,   username,   serialNumber;
    @FXML TextField             systemName;
    private static int close_option=-1;
    int generatedID = -1;

      ////////////////
     //Initializers//
    ////////////////
    public static void setView() throws Exception{
        Stage pop = Main.getPopupWindow();
        pop.setWidth(600);
        pop.setHeight(450);
        Main.setPopupWindow("Forms/AssignmentForm.fxml");
    }

    public static void setView(int i) throws Exception{
        close_option=i;
        Stage pop = Main.getPopupWindow();
        pop.setWidth(600);
        pop.setHeight(450);
        Main.setPopupWindow("Forms/AssignmentForm.fxml");
    }

    public void initialize(URL url, ResourceBundle resourceBundle){
        try{
            //UserBox
            userBox.setItems(generateUserList());
            ComboBoxAutoComplete<User> filterBoxUser = new ComboBoxAutoComplete<User>(userBox);
            userBox.valueProperty().addListener((ov, oldValue, newValue) -> {
                try {
                    fullName.setText(userBox.getSelectionModel().getSelectedItem().getFullName());
                    username.setText(userBox.getSelectionModel().getSelectedItem().getUsername());
                }catch (Exception e) { }
            });
            if(Main.getUserFocus() != null){
                userBox.getSelectionModel().select(Main.getUserFocus());
            }
            //SystemBox and SerialBox
            systemBox.setItems(Equipment.generateEquipmentList(Equipment.queryAllEquipmentGrouped()));
            ComboBoxAutoComplete<Equipment> filterEQBox = new ComboBoxAutoComplete<>(systemBox);

            Equipment eq = Main.getEquipmentFocus();
            if (eq!=null) {
                systemBox.getSelectionModel().select(eq);
                serialBox.setItems(Equipment.generateSerialList(
                        systemBox.getSelectionModel().getSelectedItem().getModelID(),
                        systemBox.getSelectionModel().getSelectedItem().getMakeID()
                ));
                serialBox.getSelectionModel().select(eq.getSerialNumber());
                serialNumber.setText(serialBox.getSelectionModel().getSelectedItem());
            }
            systemBox.valueProperty().addListener((ov, oldValue, newValue) -> {
                try {
                    //Serial Box
                    serialBox.setItems(Equipment.generateSerialList(
                            systemBox.getSelectionModel().getSelectedItem().getModelID(),
                            systemBox.getSelectionModel().getSelectedItem().getMakeID()
                    ));
                    ComboBoxAutoComplete<String> filterSerialBox = new ComboBoxAutoComplete<>(serialBox);

                    if(eq!=null){
                        serialBox.getSelectionModel().select(eq.getSerialNumber());
                    }

                    serialBox.valueProperty().addListener((olv, olderValue, newerValue) -> {
                        try {
                            serialNumber.setText(serialBox.getSelectionModel().getSelectedItem());

                        }catch (Exception e) { }
                    });

                }catch (Exception e) { }
            });

            accessoryBox.setItems(Accessory.generateAccessoryList());
            accessoryBox.valueProperty().addListener((ov, oldValue, newValue) -> {
                try {
                    if(accessoryBox.getSelectionModel().getSelectedIndex()!=0) {
                        AccessoryList.getItems().add(accessoryBox.getSelectionModel().getSelectedItem());
                        accessoryBox.getSelectionModel().select(0);
                    }
                }catch (Exception e) { }
            });
            AccessoryList.setOnMouseClicked((MouseEvent event) -> {
                try{
                    AccessoryList.getItems().remove(AccessoryList.getSelectionModel().getSelectedItem());
                }catch(Exception e){
                    e.printStackTrace();
                }
            });

        } catch (Exception e){e.printStackTrace();}
    }


      ///////////////////
     //List Generators//
    ///////////////////


      //////////////////
     //Button Methods//
    //////////////////
    public void submitAssignment() throws Exception{
        if (validateForm()) {
            generatedID = Assignment.insertNewAssignment(
                    userBox.getSelectionModel().getSelectedItem(),
                    systemBox.getSelectionModel().getSelectedItem(),
                    AccessoryList.getItems(),
                    systemName.getText());
            close();
        }
    }


      ///////////////////
     //Form Validation//
    ///////////////////
    private void close()throws Exception {
        Main.getPopupWindow().setOnCloseRequest(null);
        Main.getPopupWindow().close();
        Main.getOuter().setDisable(false);
        if (close_option == 1) {
            Main.getViewContainer().setRight(null);
            Main.getViewContainer().setCenter(null);
            EquipmentListController.setView();
            SystemInformationController.setView();
        } else if(close_option==2){
            Main.getViewContainer().setRight(null);
            Main.getViewContainer().setCenter(null);
            Equipment eq =systemBox.getSelectionModel().getSelectedItem();
            Main.setAssignmentFocus(new Assignment(
                    generatedID,
                    eq.getSystemID(),
                    systemBox.getSelectionModel().getSelectedItem(),
                    userBox.getSelectionModel().getSelectedItem(),
                    Date.valueOf(LocalDate.now())
            ));
            AssignmentInformationController.setView();
        }else if(close_option==3){
            AssignmentListController.setView();
        }else if (close_option==4){
            UserFocusController.setView();
        } else
            Main.setCenterPane("Lists/EquipmentList.fxml");
    }

    private boolean validateForm() {

        systemName.setStyle(null);
        systemBox.setStyle(null);
        userBox.setStyle(null);
        serialBox.setStyle(null);


        if(systemName.getText().matches("^(?=.*[a-zA-Z0-9]).*$")
                && systemBox.getSelectionModel().getSelectedItem()!=null
                && userBox.getSelectionModel().getSelectedItem()!=null
                && serialBox.getSelectionModel().getSelectedItem() != null){
            return true;
        }
        if(!systemName.getText().matches("^(?=.*[a-zA-Z0-9]).*$")){
            systemName.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }
        if(systemBox.getSelectionModel().getSelectedItem()==null){
            systemBox.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }
        if(userBox.getSelectionModel().getSelectedItem()==null){
            userBox.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }
        if(serialBox.getSelectionModel().getSelectedItem() == null){
            serialBox.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }
        return false;
    }
}
