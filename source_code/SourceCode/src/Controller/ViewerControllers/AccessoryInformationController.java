package Controller.ViewerControllers;

import Controller.Main;
import Model.Accessory;
import Model.Assignment;
import Model.Equipment;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class AccessoryInformationController implements Initializable {

    ////////////////////////
    //Variable Declaration//
    ////////////////////////
    @FXML
    AnchorPane Anchor;
    @FXML
    private TableView<Assignment> AssignmentTable;
    @FXML
    private TableColumn<Assignment, Integer> AssignID;
    @FXML
    private TableColumn<Assignment, String> UserFullName, Username;
    @FXML
    private TextField SearchField;
    @FXML
    private Text SystemInfo, Name, Serial, Type,
            NamePlaceHolder, TotalPlaceHolder, StockPlaceHolder;
    @FXML
    private Button UpdateButton, CancelButton;
    @FXML
    private Spinner<Integer> StockSpinner, TotalSpinner;
    private static int SELECTED_ACCESSORY;
    private static FilteredList<Assignment> AssignmentList;
    private boolean SPINNER_LOCK = true;

    ////////////////
    //Initializers//
    ////////////////
    public static void setView(int selectedAccessory) throws Exception{
        SELECTED_ACCESSORY = selectedAccessory;
        Main.getViewContainer().setRight(FXMLLoader.load(Main.class.getResource("../View/Viewers/AccessoryInformation.fxml")));
    }


    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            updateTable();
            Anchor.setPrefWidth(Main.getOuter().getWidth()/2-35);
            Anchor.setPrefHeight(Main.getOuter().getHeight()-35);

            Accessory accessory = new Accessory(SELECTED_ACCESSORY);
            NamePlaceHolder.setText(accessory.getAccessoryName());
            TotalPlaceHolder.setText(String.valueOf(accessory.getTotal()));
            StockPlaceHolder.setText(String.valueOf(accessory.getInStock()));

            if(AssignmentList.isEmpty()){
                SearchField.setDisable(true);
            }
            else
                SearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                    AssignmentList.setPredicate(obj -> {
                        // If filter text is empty, display all persons.
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Compare first name and last name field in your object with filter.
                        String lowerCaseFilter = newValue.toLowerCase();

                         if (String.valueOf(obj.getUserName()).toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches username.
                        }
                        else if (String.valueOf(obj.getUserFullName()).toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches user's full name.
                        }
                         else if (String.valueOf(obj.getAssignID()).toLowerCase().contains(lowerCaseFilter)) {
                             return true; // Filter matches Assignment ID.
                         }

                        return false; // Does not match.
                    });
                });

            StockSpinner.setValueFactory(
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20000, Integer.parseInt(StockPlaceHolder.getText()), 1));

            TotalSpinner.setValueFactory(
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20000, Integer.parseInt(TotalPlaceHolder.getText()), 1));

            StockSpinner.valueProperty().addListener((obj, oldValue, newValue)-> {
                if(SPINNER_LOCK) {
                    SPINNER_LOCK=false;
                    if (newValue > TotalSpinner.getValueFactory().getValue()) {
                        TotalSpinner.increment(newValue - TotalSpinner.getValueFactory().getValue());
                    }
                    SPINNER_LOCK = true;
                }
            });


            TotalSpinner.valueProperty().addListener((obj, oldValue, newValue)-> {
                if(SPINNER_LOCK) {
                    SPINNER_LOCK = false;
                    if (newValue > oldValue) {
                        StockSpinner.increment(1);
                    } else if (oldValue > newValue) {
                        StockSpinner.decrement(1);
                    }
                    SPINNER_LOCK=true;
                }
            });

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTable()throws Exception{
        AssignmentTable.setPlaceholder(new Label("This computer has not been assigned to anyone"));
        AssignmentList = new FilteredList<>(Assignment.generateAssignmentList(SELECTED_ACCESSORY), s->true);
        AssignmentTable.setItems(AssignmentList);
        AssignID.setCellValueFactory(new PropertyValueFactory<Assignment, Integer>("assignID"));
        UserFullName.setCellValueFactory(new PropertyValueFactory<Assignment, String>("userFullName"));
        Username.setCellValueFactory(new PropertyValueFactory<Assignment, String>("userName"));
    }


    ///////////////////
    //List Generators//
    ///////////////////


    //////////////////
    //Button Methods//
    //////////////////
    @FXML
    private void updateStock() {
        UpdateButton.setText("Save Update");
        CancelButton.setVisible(true);
        StockSpinner.setVisible(true);
        StockSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20000, Integer.parseInt(StockPlaceHolder.getText()), 1));
        TotalSpinner.setVisible(true);

        UpdateButton.setOnAction((nv)->{
            UpdateButton.setText("Update Stock");
            CancelButton.setVisible(false);
            StockSpinner.setVisible(false);
            StockPlaceHolder.setText(String.valueOf(StockSpinner.getValueFactory().getValue()));

            TotalSpinner.setVisible(false);
            TotalPlaceHolder.setText(String.valueOf(TotalSpinner.getValueFactory().getValue()));

            UpdateButton.setOnAction((ov) -> {
                this.updateStock();
            });

            if(validateAccessoryAmounts()){
                try {
                    Accessory.updateAccessoryStock(SELECTED_ACCESSORY, StockSpinner.getValueFactory().getValue(), TotalSpinner.getValueFactory().getValue());
                }catch (Exception e ){
                    e.printStackTrace();
                    System.out.println("Cannot Update accessory Stock");
                }
            }
        });


    }

    @FXML
    private void cancelUpdateStock(){
        UpdateButton.setText("Update Stock");
        CancelButton.setVisible(false);
        StockSpinner.setVisible(false);
        TotalSpinner.setVisible(false);
        UpdateButton.setOnAction((nv)->{
            updateStock();
        });
    }


    ///////////////////
    //Form Validation//
    ///////////////////
    private boolean validateAccessoryAmounts() {
        return true;
    }

}
