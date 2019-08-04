package Controller.ViewerControllers;

import Controller.Main;
import Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;

public class LoginViewController {

    ////////////////////////
    //Variable Declaration//
    ////////////////////////
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordTextField;

    ////////////////
    //Initializers//
    ////////////////
    public static void setView() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/View/Viewers/LoginView.fxml"));
        loader.setClassLoader(Main.class.getClassLoader());
        Parent root = (Parent)loader.load();
        Main.getOuter().setCenter(root);
    }


    ///////////////////
    //List Generators//
    ///////////////////


    //////////////////
    //Button Methods//
    //////////////////
    @FXML
    private void onLoginButtonPushed() throws Exception{
        if(User.verifyLogin(passwordTextField.getText(), usernameTextField.getText())){
            User su = new User();
            su.setUsername(usernameTextField.getText());
            Main.setSessionUser(su);
            Main.setMainStage();
        }
    }

    ///////////////////
    //Form Validation//
    ///////////////////
}
