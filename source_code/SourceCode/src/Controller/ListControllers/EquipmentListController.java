package Controller.ListControllers;

import Controller.FormControllers.NewAccessoryController;
import Controller.FormControllers.NewSystemFormController;
import Controller.Main;
import Controller.ViewerControllers.AccessoryInformationController;
import Controller.ViewerControllers.SystemInformationController;
import Model.Accessory;
import Model.Equipment;
import com.sun.javafx.webkit.Accessor;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class EquipmentListController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML AnchorPane Anchor;
    @FXML TableView<Equipment> EquipmentList;
    @FXML TableView<Accessory> AccessoryList;
    @FXML TableColumn<Equipment, String> Name, SerialNum, Make, Model, Type;
    @FXML TableColumn<Equipment, Integer> SystemID;
    @FXML TableColumn<Accessory, Integer> AccessoryID, InStock, Total;
    @FXML TableColumn<Accessory, String> AccessoryName;
    @FXML Text AccessoryText;
    @FXML TextField filterSystems, filterAccessory;
    @FXML Button AddNewSystemButton,AddNewAccessoryButton;

      ////////////////
     //Initializers//
    ////////////////
    public static void setView() throws Exception {
        Main.setCenterPane("Lists/EquipmentList.fxml");
    }

    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            updateTable();
            EquipmentList.setPrefWidth((Main.getViewContainer().getWidth()/2)-20);
            EquipmentList.setPrefHeight((Main.getViewContainer().getHeight()/3)+10);
            EquipmentList.setMinHeight(250);
            EquipmentList.setMinWidth(250);
            EquipmentList.setOnMouseClicked((MouseEvent event) -> {
                //Single CLICK ON CELL
                try {
                    Main.setEquipmentFocus((Equipment) EquipmentList.getSelectionModel().getSelectedItem());
                    SystemInformationController.setView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            Anchor.setPrefWidth((Main.getOuter().getWidth()/2)-20);
            AccessoryList.setPrefWidth((Main.getViewContainer().getWidth()/2)-20);
            AccessoryList.setPrefHeight((Main.getViewContainer().getHeight()/3)+20);
            AccessoryText.setLayoutY(((Main.getViewContainer().getHeight()/3)*2));
            AccessoryList.setMinHeight(250);
            AccessoryList.setMinWidth(250);
            AccessoryList.setOnMouseClicked((MouseEvent event) -> {
                //Single CLICK ON CELL
                try {
                    AccessoryInformationController.setView(AccessoryList.getSelectionModel().getSelectedItem().getAccessoryID());
                } catch (Exception e) {
                }
            });

            AnchorPane.setTopAnchor(EquipmentList, 95.0);
            AnchorPane.setTopAnchor(filterSystems, 70.0);
            AnchorPane.setTopAnchor(AddNewSystemButton, 55.0);

            AnchorPane.setBottomAnchor(AddNewAccessoryButton, ((Main.getViewContainer().getHeight()/3))+60);
            AnchorPane.setBottomAnchor(AccessoryText,((Main.getViewContainer().getHeight()/3))+70);
            AnchorPane.setBottomAnchor(filterAccessory, ((Main.getViewContainer().getHeight()/3))+45);
            AnchorPane.setBottomAnchor(EquipmentList,450.0);
            AnchorPane.setBottomAnchor(AccessoryList, 25.0);

            AnchorPane.setRightAnchor(EquipmentList,0.0);
            AnchorPane.setRightAnchor(AccessoryList,0.0);
            AnchorPane.setRightAnchor(AddNewSystemButton,0.0);
            AnchorPane.setRightAnchor(AddNewAccessoryButton, 0.0);

            setRightPlaceHolder();

        }catch (Exception e){ e.printStackTrace(); }
    }


    public void updateTable()throws Exception{
        //Assignment.generateAssignmentList(Assignment.selectEquipmentAssignments(Main.getEquipmentFocus().getSystemID())), s->true);
        FilteredList<Equipment> eqFilter = new FilteredList<>(Equipment.generateEquipmentList(Equipment.queryAllEquipment()), s->true);
        EquipmentList.setItems(eqFilter);
        Name.setCellValueFactory(new PropertyValueFactory<Equipment, String>("systemName"));
        SerialNum.setCellValueFactory(new PropertyValueFactory<Equipment, String>("serialNumber"));
        Make.setCellValueFactory(new PropertyValueFactory<Equipment, String>("make"));
        Model.setCellValueFactory(new PropertyValueFactory<Equipment, String>("model"));
        Type.setCellValueFactory(new PropertyValueFactory<Equipment, String>("type"));
        SystemID.setCellValueFactory(new PropertyValueFactory<Equipment, Integer>("systemID"));

        filterSystems.textProperty().addListener((observable, oldValue, newValue) -> {
            eqFilter.setPredicate(obj -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name field in your object with filter.
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(obj.getSerialNumber()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;// Filter matches Serial Number.
                } else if (String.valueOf(obj.getSystemName()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches system name.
                } else if (String.valueOf(obj.getMake()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches system name.
                }else if (String.valueOf(obj.getModel()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches system name.
                }else if (String.valueOf(obj.getType()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches system name.
                }

                return false; // Does not match.
            });
        });

        ObservableList<Accessory> obAccessoryList = Accessory.generateAccessoryList();
        obAccessoryList.remove(0);
        FilteredList<Accessory> acFilter = new FilteredList<>(obAccessoryList, s->true);
        AccessoryList.setItems(acFilter);
        AccessoryList.getItems();
        AccessoryID.setCellValueFactory(new PropertyValueFactory<Accessory, Integer>("accessoryID"));
        InStock.setCellValueFactory(new PropertyValueFactory<Accessory, Integer>("inStock"));
        Total.setCellValueFactory(new PropertyValueFactory<Accessory, Integer>("total"));
        AccessoryName.setCellValueFactory(new PropertyValueFactory<Accessory, String>("accessoryName"));

        filterAccessory.textProperty().addListener((observable, oldValue, newValue) -> {
            acFilter.setPredicate(obj -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name field in your object with filter.
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(obj.getAccessoryName()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;// Filter matches name.
                }
                return false; // Does not match.
            });
        });

    }

      ///////////////////
     //List Generators//
    ///////////////////


      //////////////////
     //Button Methods//
    //////////////////
    @FXML private void addNewSystem() throws Exception{
        NewSystemFormController.setView();
    }
    @FXML private void addNewAccessory() throws Exception{
        NewAccessoryController.setView();
    }


      ///////////////////
     //Form Validation//
    ///////////////////
    private void setRightPlaceHolder() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/View/Viewers/PlaceHolder.fxml"));
        loader.setClassLoader(Main.class.getClassLoader());
        Parent root = (Parent)loader.load();
        Main.getViewContainer().setRight(root);
        Main.getViewContainer().setRight(root);
    }
}
