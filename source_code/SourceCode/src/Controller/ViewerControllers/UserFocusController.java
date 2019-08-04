package Controller.ViewerControllers;

import Controller.FormControllers.AssignmentFormController;
import Controller.Main;
import Model.Assignment;
import Model.User;
import Model.Worksite;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class UserFocusController implements Initializable {

    ////////////////////////
    //Variable Declaration//
    ////////////////////////
    @FXML TextField FirstNameField, LastNameField, UserNameField;
    @FXML Text WorksiteLabel, WorksiteHolder;
    @FXML Button editInfoButton;
    @FXML ComboBox<Worksite> WorksiteBox;
    @FXML TableView<Assignment> AssignmentTable;
    @FXML ToggleButton ToggleCurrent, TogglePast;
    @FXML
    TableColumn<Assignment, Integer> AssignmentID;
    @FXML
    TableColumn<Assignment, String> AssignmentMake, AssignmentSerial, AssignmentDateIssued, AssignmentDateReturned;

    private static boolean editInfoLock = false;
    private static FilteredList<Assignment> AssignmentList;



    ////////////////
    //Initializers//
    ////////////////
    public static void setView() throws Exception{
        Main.setCenterPane("Viewers/UserFocus.fxml");
    }
    public static void setView(Assignment reAssign) throws Exception{
        Assignment.updateUndoUnAssignment(reAssign);
        Main.setCenterPane("Viewers/UserFocus.fxml");
    }


    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            updateTable();

            ToggleCurrent.setSelected(true);

            User u = Main.getUserFocus();
            FirstNameField.setText(u.getFirstName());
            LastNameField.setText(u.getLastName());
            UserNameField.setText(u.getUsername());
            WorksiteHolder.setText(u.getWorksite());
        }catch (Exception e){ e.printStackTrace(); }
    }

    private void updateTable() throws Exception{
        AssignmentList = new FilteredList<>(Assignment.generateAssignmentList(Assignment.selectUserAssignments(Main.getUserFocus().getUserID())), s-> s.getReturnDate() == "Not Returned");
        AssignmentTable.setItems(AssignmentList);
        AssignmentID.setCellValueFactory(new PropertyValueFactory<Assignment, Integer>("assignID"));
        AssignmentMake.setCellValueFactory(new PropertyValueFactory<Assignment, String>("systemMakeModel"));
        AssignmentSerial.setCellValueFactory(new PropertyValueFactory<Assignment, String>("systemSerial"));
        AssignmentDateIssued.setCellValueFactory(new PropertyValueFactory<Assignment, String>("issueDate"));
        AssignmentDateReturned.setCellValueFactory(new PropertyValueFactory<Assignment, String>("returnDate"));

        AssignmentTable.setOnMouseClicked((MouseEvent event) -> {
            //DOUBLE CLICK ON CELL
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                try{
                    Main.setAssignmentFocus(AssignmentTable.getSelectionModel().getSelectedItem());
                    AssignmentInformationController.setView();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    ///////////////////
    //List Generators//
    ///////////////////


    //////////////////
    //Button Methods//
    //////////////////
    @SuppressWarnings("Duplicates")
    @FXML
    private void editInformation() throws Exception{
        if(!editInfoLock){
            editInfoLock = !editInfoLock;
            FirstNameField.setEditable(true);
            LastNameField.setEditable(true);
            UserNameField.setEditable(true);

            WorksiteLabel.setVisible(false);
            WorksiteHolder.setVisible(false);

            WorksiteBox.setVisible(true);
            WorksiteBox.setItems(Worksite.getWorksiteList());
            for (Worksite wk :
                    WorksiteBox.getItems()) {
                if (wk.getWorksiteName().equals(WorksiteHolder.getText()))
                    WorksiteBox.getSelectionModel().select(wk);
            }

            editInfoButton.setText("Submit");
        }
        else{
            if(validateForm()) {
                editInfoLock = !editInfoLock;
                FirstNameField.setEditable(false);
                LastNameField.setEditable(false);
                UserNameField.setEditable(false);

                WorksiteLabel.setVisible(true);
                WorksiteHolder.setVisible(true);

                WorksiteBox.setVisible(false);
                editInfoButton.setText("Edit User Info.");

                String fName = FirstNameField.getText().substring(0,1) + FirstNameField.getText().substring(1);
                String lName = LastNameField.getText().substring(0,1) + LastNameField.getText().substring(1);

                User.updateUser(new User(
                        Main.getUserFocus().getUserID(),
                        WorksiteBox.getSelectionModel().getSelectedItem().getWorksiteID(),
                        UserNameField.getText(),
                        fName,
                        lName,
                        WorksiteBox.getSelectionModel().getSelectedItem()
                ));
            }
        }
    }

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
    }

    @FXML
    private void assignNew() throws Exception{

        Main.getPopupWindow().setOnCloseRequest(event -> {
            Main.getOuter().setDisable(false);
            try {
                UserFocusController.setView();
            } catch (Exception e) {
                System.out.println("Could not Reset the view");
            }
        });
        AssignmentFormController.setView(4);
    }


    ///////////////////
    //Form Validation//
    ///////////////////
    private boolean validateForm() {
        FirstNameField.setStyle(null);
        LastNameField.setStyle(null);
        UserNameField.setStyle(null);
        WorksiteBox.setStyle(null);


        if(FirstNameField.getText().matches("[A-Za-z]+$")
                && LastNameField.getText().matches("[A-Za-z]+$")
                && UserNameField.getText().matches("[A-Za-z]+$")
                & WorksiteBox.getSelectionModel().getSelectedItem() != null) {
            return true;
        }

        if (!FirstNameField.getText().matches("[A-Za-z]+$")) {
            FirstNameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }

        if (!LastNameField.getText().matches("[A-Za-z]+$")) {
            LastNameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }

        if (!UserNameField.getText().matches("[A-Za-z]+$")) {
            UserNameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }

        if (WorksiteBox.getSelectionModel().getSelectedItem() == null) {
            WorksiteBox.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }

        return false;

    }
}
