package Controller.ListControllers;

import Controller.Main;
import Controller.ViewerControllers.UserFocusController;
import Model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;

import java.util.ResourceBundle;

public class UserListController implements Initializable {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    @FXML TableView             UserList;
    @FXML
    TableColumn<User, Integer>  UserID;
    @FXML
    TableColumn<User, String>   Username, FullName, Worksite;


      ////////////////
     //Initializers//
    ////////////////
    public static void setView() throws Exception{
        Main.setCenterPane("Lists/UserList.fxml");
    }

    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
             updateTable();
            UserList.setOnMouseClicked((MouseEvent event) -> {
                //DOUBLE CLICK ON CELL
                if(event.getButton().equals(MouseButton.PRIMARY) && UserList.getSelectionModel().getSelectedItem() != null) {
                    if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                        try {
                            Main.setUserFocus((User) UserList.getSelectionModel().getSelectedItem());
                            UserFocusController.setView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }catch (Exception e){ e.printStackTrace(); }
    }


    private void updateTable()throws Exception{
        UserList.setItems(User.generateUserList());
        UserID.setCellValueFactory(new PropertyValueFactory<User, Integer>("userID"));
        Username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        FullName.setCellValueFactory( new PropertyValueFactory<User, String>("fullName"));
        Worksite.setCellValueFactory(new PropertyValueFactory<User, String>("worksite"));
    }


      ///////////////////
     //List Generators//
    ///////////////////


      //////////////////
     //Button Methods//
    //////////////////
    @FXML private void newUser() throws Exception{
        Controller.FormControllers.NewUserFormController.setView();
    }


      ///////////////////
     //Form Validation//
    ///////////////////
}
