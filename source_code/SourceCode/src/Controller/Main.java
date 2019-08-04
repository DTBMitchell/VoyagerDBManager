package Controller;

import Controller.ListControllers.UserListController;
import Controller.ViewerControllers.LoginViewController;
import Model.Assignment;
import Model.DatabaseConfig;
import Model.Equipment;
import Model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    private static Stage primaryStage = new Stage();
    private static BorderPane Outer = new BorderPane();
    private static BorderPane View_Container = new BorderPane();
    private static Node backNode;
    private static Scene MainScene = new Scene(Outer,1600, 900);
    private static Stage popup = new Stage();

    private static User sessionUser;
    private static User userFocus;
    private static Assignment assignmentFocus;
    private static Equipment equipmentFocus;



      ////////////////////////
     //View Getters/Setters//
    ////////////////////////
    public static void setCenterPane(String fxmlName){
        try {
            backNode = View_Container.getCenter();
            View_Container.setRight(null);
            View_Container.setCenter(null);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/View/"+fxmlName));
            loader.setClassLoader(Main.class.getClassLoader());
            Parent root = (Parent)loader.load();
            View_Container.setCenter(root);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void setPopupWindow(String fxmlName) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/View/"+fxmlName));
        loader.setClassLoader(Main.class.getClassLoader());
        Parent root = (Parent)loader.load();
        Scene view = new Scene(root, 520, 300);
        popup.setScene(view);
        popup.setAlwaysOnTop(true);
        popup.show();
        Outer.setDisable(true);
    }

    public static void setMainStage() throws Exception{
        primaryStage.setMinHeight(500);
        primaryStage.setWidth(1600);
        primaryStage.setHeight(900);
        primaryStage.setMinWidth(500);
        Outer.setCenter(View_Container);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/View/Menus/VoyagerMenu.fxml"));
        loader.setClassLoader(Main.class.getClassLoader());
        Parent root = (Parent)loader.load();
        Outer.setLeft(root);
        Outer.setStyle("-fx-background-color: rgb(50,50,50)");

        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Move the window
        primaryStage.setX((dim.width-1600)/2);
        primaryStage.setY((dim.height-900)/2);


        UserListController.setView();
    }

    public static Stage getPopupWindow() throws Exception{
        return popup;
    }

    public static BorderPane getViewContainer() {
        return View_Container;
    }

    public static BorderPane getOuter(){return Outer;}

    public static User getUserFocus() {
        return userFocus;
    }
    public static void setUserFocus(User userFocus) {
        Main.userFocus = userFocus;
    }

    public static Assignment getAssignmentFocus() {
        return assignmentFocus;
    }
    public static void setAssignmentFocus(Assignment assignmentFocus) {
        Main.assignmentFocus = assignmentFocus;
    }

    public static Equipment getEquipmentFocus() {
        return equipmentFocus;
    }
    public static void setEquipmentFocus(Equipment equipmentFocus) {
        Main.equipmentFocus = equipmentFocus;
    }

    public static User getSessionUser() {
        return sessionUser;
    }
    public static void setSessionUser(User sessionUser) {
        Main.sessionUser = sessionUser;
    }

      //////////////////
     //Event Handlers//
    //////////////////



      /////////////////
     //Start Methods//
    /////////////////
    @Override
    public void start(Stage stage) throws Exception {
        DatabaseConfig.getConnection();
        primaryStage.setTitle("Voyager");
        primaryStage.setScene(MainScene);

        primaryStage.setHeight(450);
        primaryStage.setWidth(450);

        popup.setOnCloseRequest(event -> {
            Outer.setDisable(false);
        });

        //Set Initial View
        LoginViewController.setView();
        //Finally show initial stage
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
