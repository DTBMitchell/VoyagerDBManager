package Controller.FormControllers;

import Controller.Main;
import Model.User;
import Model.Worksite;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class NewUserFormController  implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML
    TextField firstNameField, lastNameField, usernameField;
    @FXML
    ComboBox<Worksite> worksiteSelection;


      ////////////////
     //Initializers//
    ////////////////
    public static void setView() throws Exception{
      Main.getPopupWindow().setWidth(285);
      Main.getPopupWindow().setHeight(325);
      Main.setPopupWindow("Forms/NewUserForm.fxml");
    }

    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            worksiteSelection.setItems(Worksite.getWorksiteList());
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
        Main.setCenterPane("Lists/UserList.fxml");
    }

    @FXML private void submitNewUser() throws Exception{
        //int worksiteID, String username, String fullName
        if(validateForm()) {
            String fName = firstNameField.getText().substring(0,1) + firstNameField.getText().substring(1);
            String lName = lastNameField.getText().substring(0,1) + lastNameField.getText().substring(1);
            User.insertNewUser(new User(
                    worksiteSelection.getSelectionModel().getSelectedItem().getWorksiteID(),
                    usernameField.getText().toLowerCase(),
                    fName,
                    lName
            ));

            close();
        }
    }

      ///////////////////
     //Form Validation//
    ///////////////////
    private boolean validateForm() {

        firstNameField.setStyle(null);
        lastNameField.setStyle(null);
        usernameField.setStyle(null);
        worksiteSelection.setStyle(null);

       if(firstNameField.getText().matches("^[A-Za-z]+$")
       && lastNameField.getText().matches("[A-Za-z]+$")
       && usernameField.getText().matches("[A-Za-z]+$")
       & worksiteSelection.getSelectionModel().getSelectedItem() != null) {
           return true;
       }

        if(!firstNameField.getText().matches("[A-Za-z]+$")) {
            firstNameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }
        if(!lastNameField.getText().matches("[A-Za-z]+$")) {
            lastNameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }

        if(!usernameField.getText().matches("[A-Za-z]+$")){
            usernameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }

        if(worksiteSelection.getSelectionModel().getSelectedItem() == null){
            worksiteSelection.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }

        return false;
    }
}
