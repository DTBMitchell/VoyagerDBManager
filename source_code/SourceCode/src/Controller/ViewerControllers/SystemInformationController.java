package Controller.ViewerControllers;

import Controller.FormControllers.AssignmentFormController;
import Controller.ListControllers.EquipmentListController;
import Controller.Main;
import Model.Assignment;
import Model.Equipment;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ResourceBundle;

public class SystemInformationController implements Initializable {

    ////////////////////////
    //Variable Declaration//
    ////////////////////////
    @FXML AnchorPane Anchor;
    @FXML
    private Text    NamePlaceHolder,    TypePlaceHolder,    MakePlaceHolder,
                    ModelPlaceHolder,   SerialPlaceHolder,
                    Name, Serial, Make, Model, Type;
    @FXML
    TableView<Assignment> AssignmentTable;
    @FXML
    private TableColumn<Assignment, Integer> AssignID;
    @FXML
    private TableColumn<Assignment, String> Username, SystemName, IssueDate, ReturnDate;
    @FXML
    private TextField SearchField;
    private static FilteredList<Assignment> AssignmentList;


    ////////////////
    //Initializers//
    ////////////////
    public static void setView() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/View/Viewers/SystemInformation.fxml"));
        loader.setClassLoader(Main.class.getClassLoader());
        Parent root = (Parent)loader.load();
        Main.getViewContainer().setRight(root);
    }
    public static void setView(Assignment reAssign) throws Exception{
        if (reAssign != null) {
            Assignment.updateUndoUnAssignment(reAssign);
        }
        setView();
    }

    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            updateTable();
            Anchor.setPrefWidth(Main.getOuter().getWidth()/2-35);
            Anchor.setPrefHeight(Main.getOuter().getHeight()-35);


            Equipment eq = Main.getEquipmentFocus();
            NamePlaceHolder.setText(eq.getSystemName());
            MakePlaceHolder.setText(eq.getMake());
            ModelPlaceHolder.setText(eq.getModel());
            SerialPlaceHolder.setText(eq.getSerialNumber());
            TypePlaceHolder.setText(eq.getType());

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
                            return true;
                            // Filter matches username.

                        } else if (String.valueOf(obj.getIssueDate()).toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches system name.
                        }
                        else if (String.valueOf(obj.getReturnDate()).toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches system name.
                        }

                        return false; // Does not match.
                    });
                });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTable()throws Exception{
        AssignmentTable.setPlaceholder(new Label("This computer has not been assigned to anyone"));
        AssignmentList = new FilteredList<>(Assignment.generateAssignmentList(Assignment.selectEquipmentAssignments(Main.getEquipmentFocus().getSystemID())), s->true);
        AssignmentTable.setItems(AssignmentList);
        AssignID.setCellValueFactory(new PropertyValueFactory<Assignment, Integer>("assignID"));
        Username.setCellValueFactory(new PropertyValueFactory<Assignment, String>("userName"));
        IssueDate.setCellValueFactory(new PropertyValueFactory<Assignment, String>("issueDate"));
        ReturnDate.setCellValueFactory(new PropertyValueFactory<Assignment, String>("returnDate"));

    }

    ///////////////////
    //List Generators//
    ///////////////////


    //////////////////
    //Button Methods//
    //////////////////
    @FXML
    private void addNewAssignment() throws Exception{
        Assignment reAssign = Assignment.unAssign(Main.getEquipmentFocus().getSystemID());

        Main.getPopupWindow().setOnCloseRequest(event -> {
            Main.getOuter().setDisable(false);
            try {
                EquipmentListController.setView();
                SystemInformationController.setView(reAssign);
            }catch (Exception e){
                System.out.println("Could not Reset the view");
            }
        });
        AssignmentFormController.setView(1);
    }

    ///////////////////
    //Form Validation//
    ///////////////////
}
