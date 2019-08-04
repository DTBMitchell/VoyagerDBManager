package Controller.ViewerControllers;

import Controller.FormControllers.AssignmentFormController;
import Controller.Main;
import Model.Assignment;
import Model.Equipment;
import Model.User;
import Model.Worksite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import jdk.nashorn.internal.runtime.ECMAException;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AssignmentInformationController implements Initializable {

    ////////////////////////
    //Variable Declaration//
    ////////////////////////
    @FXML private AnchorPane Anchor;
    @FXML private TableView<Assignment> AssignmentTable;
    @FXML private TableColumn<Assignment, Integer> AssignID;
    @FXML private TableColumn<Assignment, String> Username, FirstName, LastName, IssueDate, ReturnDate;
    @FXML private Text SystemInfo, UserNotice;
    @FXML
    private TextField SearchField;
    @FXML
    private Text    SystemNamePlaceHolder,  TypePlaceHolder,    MakePlaceHolder,
                    ModelPlaceHolder,       SerialPlaceHolder,  UserFullNameHolder,
                    UsernameHolder,         WorksiteHolder,     DateAssignedHolder,
                    DateReturnedHolder;
    @FXML
    private Button EditUserInfoButton,ReassignmentButton,EditSystemButton, returnButton;
    @FXML
    private TextField UserFullNameField,UsernameField,SystemNameField,SerialNumberField;
    @FXML
    private DatePicker DatePicker,ReturnDatePicker;
    @FXML
    private ComboBox<String> MakeBox, ModelBox;
    @FXML
    private ComboBox<Worksite> WorksiteBox;
    @FXML
    private ComboBox<String> TypeBox;
    private FilteredList<Assignment> AssignmentList;
    private boolean UPDATE_USER_LOCK, UPDATE_SYSTEM_LOCK=false;


    ////////////////
    //Initializers//
    ////////////////
    public static void setView() throws Exception{
        Main.setEquipmentFocus(null);
        Main.setCenterPane("Viewers/AssignmentInformation.fxml");
    }
    public static void setView(Assignment reAssign) throws Exception{
        Assignment.updateUndoUnAssignment(reAssign);
        Main.setCenterPane("Viewers/AssignmentInformation.fxml");
    }

    @SuppressWarnings("Duplicates")
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            updateTable();

            ObservableList<String> list = FXCollections.observableArrayList();
            list.add("Laptop");
            list.add("Desktop");
            TypeBox.setItems(list);

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
                        }
                        else if (String.valueOf(obj.getIssueDate()).toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches issue date.
                        }
                        else if (String.valueOf(obj.getReturnDate()).toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches return date.
                        }
                        else // Filter matches last name.
                            if (String.valueOf(obj.getUser().getFirstName()).toLowerCase().contains(lowerCaseFilter)) {
                            return true; // Filter matches first name.
                        }
                        else return String.valueOf(obj.getUser().getLastName()).toLowerCase().contains(lowerCaseFilter);

                    });
                });

            if(DateReturnedHolder.getText().equals("Not Returned")){
                returnButton.setVisible(false);
            }

            setPlaceHolders();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    public void updateTable()throws Exception{
        AssignmentTable.setPlaceholder(new Label("This computer has not been assigned to anyone"));
        AssignmentList = new FilteredList<>(Assignment.generateAssignmentList(Assignment.selectEquipmentAssignments(Main.getAssignmentFocus().getSystemID())), s->true);
        AssignmentTable.setItems(AssignmentList);
        AssignID.setCellValueFactory(new PropertyValueFactory<Assignment, Integer>("assignID"));
        FirstName.setCellValueFactory(new PropertyValueFactory<Assignment, String>("firstName"));
        LastName.setCellValueFactory(new PropertyValueFactory<Assignment, String>("lastName"));
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
    @SuppressWarnings("Duplicates")
    @FXML
    private void reAssignSystem() throws Exception{
        Assignment reAssign = Assignment.unAssign(Main.getAssignmentFocus().getSystemID());
        Main.setEquipmentFocus(Main.getAssignmentFocus().getSystem());

        Main.getPopupWindow().setOnCloseRequest(event -> {
            Main.getOuter().setDisable(false);
            try {
                AssignmentInformationController.setView(reAssign);
            } catch (Exception e) {
                System.out.println("Could not Reset the view");
            }
        });
        AssignmentFormController.setView(2);
    }


    @SuppressWarnings("Duplicates")
    @FXML
    private void updateUserInfo() throws Exception {
        if (!UPDATE_USER_LOCK) {
            UPDATE_USER_LOCK = true;

            UserNotice.setVisible(true);

            UserFullNameField.setVisible(true);
            UserFullNameField.setText(UserFullNameHolder.getText());

            UsernameField.setVisible(true);
            UsernameField.setText(UsernameHolder.getText());

            WorksiteBox.setVisible(true);
            WorksiteBox.setItems(Worksite.getWorksiteList());
            for (Worksite wk :
                    WorksiteBox.getItems()) {
                if (wk.getWorksiteName().equals(WorksiteHolder.getText()))
                    WorksiteBox.getSelectionModel().select(wk);
            }

            EditUserInfoButton.setText("Submit Changes");
        } else {
            if (validateUserForm()) {
                UPDATE_USER_LOCK = false;
                UserNotice.setVisible(false);
                UserFullNameField.setVisible(false);
                UsernameField.setVisible(false);
                WorksiteBox.setVisible(false);

                UserFullNameHolder.setText(UserFullNameField.getText());
                UsernameHolder.setText(UsernameField.getText());

                String safetySplit = UserFullNameField.getText().split(" ").length == 2 ? UserFullNameField.getText().split(" ")[1] : " ";

                User.updateUser(new User(
                        Main.getAssignmentFocus().getUserID(),
                        WorksiteBox.getSelectionModel().getSelectedItem().getWorksiteID(),
                        UsernameField.getText(),
                        UserFullNameField.getText().split(" ")[0],
                        safetySplit,
                        WorksiteBox.getSelectionModel().getSelectedItem()
                ));

                EditUserInfoButton.setText("Edit User Info");
            }
        }
    }

    @FXML
    private void updateSystemInfo() throws Exception{
        if(!UPDATE_SYSTEM_LOCK){
            UPDATE_SYSTEM_LOCK = true;
            EditSystemButton.setText("Submit");
            SystemNameField.setVisible(true);
            TypeBox.setVisible(true);
            MakeBox.setVisible(true);
            ModelBox.setVisible(true);
            SerialNumberField.setVisible(true);
            DatePicker.setVisible(true);
            ReturnDatePicker.setVisible(true);

            SystemNameField.setText(SystemNamePlaceHolder.getText());
            SerialNumberField.setText(SerialPlaceHolder.getText());

            for (String st : TypeBox.getItems()) {
                if (st.equals(TypePlaceHolder.getText()))
                    TypeBox.getSelectionModel().select(st);
            }
            MakeBox.setItems(FXCollections.observableArrayList(Equipment.generateMakeList()));
            for (String eq : MakeBox.getItems()) {
                if (eq.equals(MakePlaceHolder.getText()))
                    MakeBox.getSelectionModel().select(eq);
            }
            MakeBox.valueProperty().addListener((ov, oldValue, newValue) -> {
                try {
                    ModelBox.setItems(FXCollections.observableArrayList(Equipment.generateModelList(newValue)));
                }catch (Exception e) { }
            });
            ModelBox.setItems(FXCollections.observableArrayList(Equipment.generateModelList(MakePlaceHolder.getText())));
            for (String eq : ModelBox.getItems()) {
                if (eq.equals(ModelPlaceHolder.getText()))
                    ModelBox.getSelectionModel().select(eq);
            }
            String[] dateAssigned = DateAssignedHolder.getText().split("/");
            String[] dateReturned = DateReturnedHolder.getText().split("/");

            DatePicker.setValue(LocalDate.of(Integer.parseInt(dateAssigned[2]),
                    Integer.parseInt(dateAssigned[0]), Integer.parseInt(dateAssigned[1])));
            if(dateReturned.length == 3) {
                ReturnDatePicker.setValue(LocalDate.of(Integer.parseInt(dateReturned[2]),
                        Integer.parseInt(dateReturned[0]), Integer.parseInt(dateReturned[1])));
            }
            else {
                ReturnDatePicker.setDisable(true);
                DateReturnedHolder.setVisible(false);
            }

        }else if (UPDATE_SYSTEM_LOCK) {
            if(validateSystemForm()) {
                SystemNameField.setVisible(false);
                EditSystemButton.setText("Edit System Information");
                TypeBox.setVisible(false);
                MakeBox.setVisible(false);
                ModelBox.setVisible(false);
                SerialNumberField.setVisible(false);
                DatePicker.setVisible(false);
                ReturnDatePicker.setVisible(false);

                DateReturnedHolder.setVisible(true);

                Equipment.updateSystem(
                        //int SystemID, String NewName, String Type, String Make, String Model, String SerialNumber, LocalDate DateAssigned, LocalDate DateReturned
                        Main.getAssignmentFocus().getSystemID(),
                        SystemNameField.getText(),
                        TypeBox.getSelectionModel().getSelectedItem(),
                        MakeBox.getSelectionModel().getSelectedItem(),
                        ModelBox.getSelectionModel().getSelectedItem(),
                        SerialNumberField.getText(),
                        DatePicker.getValue(),
                        ReturnDatePicker.getValue()
                );

                SystemNamePlaceHolder.setText(SystemNameField.getText());
                TypePlaceHolder.setText(TypeBox.getSelectionModel().getSelectedItem());
                MakePlaceHolder.setText(MakeBox.getSelectionModel().getSelectedItem());
                ModelPlaceHolder.setText(ModelBox.getSelectionModel().getSelectedItem());
                SerialPlaceHolder.setText(SerialNumberField.getText());

                AssignmentInformationController.setView();
                UPDATE_SYSTEM_LOCK = false;
            }
    }

    }

    @FXML private void returnSystem() throws Exception{
        Assignment.updateUnAssign(Main.getAssignmentFocus().getSystemID(), Assignment.getLatestUser(Main.getAssignmentFocus().getSystemID()));
        Main.getAssignmentFocus().setReturnDate(Date.valueOf(LocalDate.now()));
        setView();
    }

    ///////////////////
    //Form Validation//
    ///////////////////
    private void setPlaceHolders() {

        if(UserFullNameField.getText().equals(""))
            UserFullNameHolder.setText(Main.getAssignmentFocus().getUserFullName());
        else
            UserFullNameHolder.setText(UserFullNameField.getText());

        if(UsernameField.getText().equals(""))
            UsernameHolder.setText(Main.getAssignmentFocus().getUserName());
        else
            UsernameHolder.setText(UsernameField.getText());

        if (SystemNameField.getText().equals(""))
            SystemNamePlaceHolder.setText(Main.getAssignmentFocus().getSystemName());
        else
            SystemNamePlaceHolder.setText(SystemNameField.getText());

        if(SerialNumberField.getText().equals(""))
            SerialPlaceHolder.setText(Main.getAssignmentFocus().getSystemSerial());
        else
            SerialPlaceHolder.setText(SerialNumberField.getText());

        if(MakeBox.getSelectionModel().getSelectedItem() == null)
            MakePlaceHolder.setText(Main.getAssignmentFocus().getSystemMake());
        else
            MakePlaceHolder.setText(MakeBox.getSelectionModel().getSelectedItem());

        if(ModelBox.getSelectionModel().getSelectedItem() == null)
            ModelPlaceHolder.setText(Main.getAssignmentFocus().getSystemModel());
        else
            ModelPlaceHolder.setText(ModelBox.getSelectionModel().getSelectedItem());

        if(WorksiteBox.getSelectionModel().getSelectedItem() == null)
            WorksiteHolder.setText(Main.getAssignmentFocus().getUser().getWorksite());
        else
            WorksiteHolder.setText(WorksiteBox.getSelectionModel().getSelectedItem().getWorksiteName());

        if(DatePicker.getValue() == null)
            DateAssignedHolder.setText(Main.getAssignmentFocus().getIssueDate());
        else
            DatePicker.getValue().toString();

        if(ReturnDatePicker.getValue() == null)
            DateReturnedHolder.setText(Main.getAssignmentFocus().getReturnDate());
        else
            ReturnDatePicker.getValue().toString();

        if(TypeBox.getSelectionModel().getSelectedItem() == null)
            TypePlaceHolder.setText(Main.getAssignmentFocus().getSystemType());
        else
            TypePlaceHolder.setText(TypeBox.getSelectionModel().getSelectedItem());
    }

    private boolean validateSystemForm() {
        SystemNameField.setStyle(null);
        TypeBox.setStyle(null);
        MakeBox.setStyle(null);
        ModelBox.setStyle(null);
        SerialNumberField.setStyle(null);
        DatePicker.setStyle(null);
        ReturnDatePicker.setStyle(null);

        if(SystemNameField.getText().matches("^(?=.*[a-zA-Z0-9]).*$")
            && !TypeBox.getSelectionModel().getSelectedItem().isEmpty()
            && !MakeBox.getSelectionModel().getSelectedItem().isEmpty()
            && !ModelBox.getSelectionModel().getSelectedItem().isEmpty()
            && SerialNumberField.getText().matches("^(?=.*[a-zA-Z0-9-_#$&*+~.]).*$")){

            return true;
        }

        if(!SystemNameField.getText().matches("^(?=.*[a-zA-Z0-9]).*$")){
            SystemNameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }

        if(TypeBox.getSelectionModel().getSelectedItem().isEmpty()){
            TypeBox.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }

        if(MakeBox.getSelectionModel().getSelectedItem().isEmpty()){
            MakeBox.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }

        if(ModelBox.getSelectionModel().getSelectedItem().isEmpty()){
            ModelBox.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }

        if(!SerialNumberField.getText().matches("^(?=.*[a-zA-Z0-9-_#$&*+~.]).*$")){
            SerialNumberField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }

        if(!DatePicker.getValue().isBefore(LocalDate.now().plusDays(1))){
            DatePicker.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }

        if(!(ReturnDatePicker.getValue().isBefore(LocalDate.now()) || ReturnDatePicker.getValue() != null)){
            ReturnDatePicker.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }

        return false;
    }

    private boolean validateUserForm(){
        UserFullNameField.setStyle(null);
        UsernameField.setStyle(null);
        WorksiteBox.setStyle(null);

        if(UserFullNameField.getText().matches("[A-Za-z ]+$")
            && UsernameField.getText().matches("[A-Za-z]+$")
            && WorksiteBox.getSelectionModel().getSelectedItem() != null) {
            return true;
        }

        if (!UserFullNameField.getText().matches("[A-Za-z ]+$")) {
            UserFullNameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }


        if (!UsernameField.getText().matches("[A-Za-z]+$")) {
            UsernameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }

        if (WorksiteBox.getSelectionModel().getSelectedItem() == null) {
            WorksiteBox.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }

        return false;
    }

}

