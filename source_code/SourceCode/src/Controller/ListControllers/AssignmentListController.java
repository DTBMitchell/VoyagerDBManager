package Controller.ListControllers;

import Controller.FormControllers.AssignmentFormController;
import Controller.Main;
import Controller.ViewerControllers.AssignmentInformationController;
import Model.Assignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AssignmentListController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML Button                        newAssign;
    @FXML TableView<Assignment>         AssignList;
    @FXML
    TableColumn<Assignment, String>     Username, SystemName, SystemModel, IssueDate;
    @FXML
    TableColumn<Assignment, Integer>    AssignID;
    @FXML
    TextField SearchField;
    @FXML
    ToggleButton ToggleCurrent, TogglePast, ToggleAll;
    private FilteredList<Assignment> AssignmentList;

      ////////////////
     //Initializers//
    ////////////////
    public static void setView() throws Exception{
          Main.setCenterPane("Lists/AssignmentList.fxml");
      }

    @SuppressWarnings("Duplicates")
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            updateTable();
        }catch (Exception e){ e.printStackTrace(); }

        AssignList.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                try{
                    Main.setAssignmentFocus(AssignList.getSelectionModel().getSelectedItem());
                    AssignmentInformationController.setView();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        SearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            AssignmentList.setPredicate(obj -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                ToggleAll.setSelected(true);

                // Compare first name and last name field in your object with filter.
                String lowerCaseFilter = newValue.toLowerCase();
                if (ToggleCurrent.isSelected()) {
                    if (String.valueOf(obj.getUserName()).toLowerCase().contains(lowerCaseFilter) && obj.getReturnDate() == "Not Returned") {
                        return true;
                        // Filter matches username.
                    } else if (String.valueOf(obj.getIssueDate()).toLowerCase().contains(lowerCaseFilter) && obj.getReturnDate() == "Not Returned") {
                        return true; // Filter matches issue date.
                    } else if (String.valueOf(obj.getReturnDate()).toLowerCase().contains(lowerCaseFilter) && obj.getReturnDate() == "Not Returned") {
                        return true; // Filter matches return date.
                    } else if (String.valueOf(obj.getUser().getFirstName()).toLowerCase().contains(lowerCaseFilter) && obj.getReturnDate() == "Not Returned") {
                        return true; // Filter matches first name.
                    } else if (String.valueOf(obj.getUser().getLastName()).toLowerCase().contains(lowerCaseFilter) && obj.getReturnDate() == "Not Returned") {
                        return true; // Filter matches last name.
                    } else if ((obj.getSystemModel().toLowerCase()).contains(lowerCaseFilter) && obj.getReturnDate() == "Not Returned") {
                        return true; // Filter matches last name.
                    } else if ((obj.getSystemMake().toLowerCase()).contains(lowerCaseFilter) && obj.getReturnDate() == "Not Returned") {
                        return true; // Filter matches last name.
                    }
                    return false;
                } else if (TogglePast.isSelected()) {
                    if (String.valueOf(obj.getUserName()).toLowerCase().contains(lowerCaseFilter) && obj.getReturnDate() != "Not Returned") {
                        return true;
                        // Filter matches username.
                    } else if (String.valueOf(obj.getIssueDate()).toLowerCase().contains(lowerCaseFilter) && obj.getReturnDate() != "Not Returned") {
                        return true; // Filter matches issue date.
                    } else if (String.valueOf(obj.getReturnDate()).toLowerCase().contains(lowerCaseFilter) && obj.getReturnDate() != "Not Returned") {
                        return true; // Filter matches return date.
                    } else if (String.valueOf(obj.getUser().getFirstName()).toLowerCase().contains(lowerCaseFilter) && obj.getReturnDate() != "Not Returned") {
                        return true; // Filter matches first name.
                    } else if (String.valueOf(obj.getUser().getLastName()).toLowerCase().contains(lowerCaseFilter) && obj.getReturnDate() != "Not Returned") {
                        return true; // Filter matches last name.
                    } else if ((obj.getSystemModel().toLowerCase()).contains(lowerCaseFilter) && obj.getReturnDate() != "Not Returned") {
                        return true; // Filter matches last name.
                    } else if ((obj.getSystemMake().toLowerCase()).contains(lowerCaseFilter) && obj.getReturnDate() != "Not Returned") {
                        return true; // Filter matches last name.
                    }
                    return false;
                } else {
                    if (String.valueOf(obj.getUserName()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                        // Filter matches username.
                    } else if (String.valueOf(obj.getIssueDate()).toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches issue date.
                    } else if (String.valueOf(obj.getReturnDate()).toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches return date.
                    } else if (String.valueOf(obj.getUser().getFirstName()).toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches first name.
                    } else if (String.valueOf(obj.getUser().getLastName()).toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    } else if ((obj.getSystemModel().toLowerCase()).contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    } else if ((obj.getSystemMake().toLowerCase()).contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    }
                    return false;
                }
            });
        });
    }

    public void updateTable()throws Exception{
        AssignmentList = new FilteredList<>(Assignment.generateAssignmentList(Assignment.selectAllAssignments()), s->true);
        AssignList.setItems(AssignmentList);
        AssignID.setCellValueFactory(new PropertyValueFactory<Assignment, Integer>("assignID"));
        Username.setCellValueFactory(new PropertyValueFactory<Assignment, String>("userName"));
        SystemName.setCellValueFactory(new PropertyValueFactory<Assignment, String>("systemName"));
        SystemModel.setCellValueFactory(new PropertyValueFactory<Assignment, String>("systemMakeModel"));
        IssueDate.setCellValueFactory(new PropertyValueFactory<Assignment, String>("issueDate"));
    }

      ///////////////////
     //List Generators//
    ///////////////////

      //////////////////
     //Button Methods//
    //////////////////
    public void setNewAssignmentWindows() throws Exception{
        Main.setAssignmentFocus(null);
        AssignmentFormController.setView(3);
    }

    @SuppressWarnings("Duplicates")
    @FXML private void toggleAssignments(){
        if(ToggleCurrent.isSelected()){
            AssignmentList.setPredicate(s-> s.getReturnDate() == "Not Returned");
        }
        else if (TogglePast.isSelected()){
            AssignmentList.setPredicate(s-> s.getReturnDate() != "Not Returned");
        }
        else{
            AssignmentList.setPredicate(s-> true);
        }
        if(!ToggleCurrent.isSelected() &&
            !TogglePast.isSelected() &&
            !ToggleAll.isSelected()){
            ToggleAll.setSelected(true);
        }
    }


      ///////////////////
     //Form Validation//
    ///////////////////
}
