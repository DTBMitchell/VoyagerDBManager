package Controller;

import Controller.ListControllers.AssignmentListController;
import Controller.ListControllers.EquipmentListController;
import Controller.ListControllers.UserListController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML AnchorPane MenuAnchor;
    @FXML
    VBox tabsVBox;
    @FXML
    HBox TabsHBox;
    @FXML
    Button collapseButton, UserList, EQList, AssignList;
    @FXML
    Label NameHolder;

    public void initialize(URL url, ResourceBundle resourceBundle){
        MenuAnchor.setPrefWidth(35);
        tabsVBox.setPrefWidth(35);

        TabsHBox.getChildren().get(0).setVisible(false);
        TabsHBox.getChildren().get(1).setVisible(false);

       setImages();

        ImageView user = new ImageView(new Image("resource/expand-arrow.png"));
        user.setFitWidth(30);
        user.setFitHeight(30);
        collapseButton.setGraphic(user);

        try{
            NameHolder.setText(Main.getSessionUser().getUsername());
        } catch (Exception e){

        }
    }

    public void collapseMenu(){
        if(MenuAnchor.getPrefWidth()!=35) {
            MenuAnchor.setPrefWidth(35);
            tabsVBox.setPrefWidth(35);
            TabsHBox.getChildren().get(0).setVisible(false);
            TabsHBox.getChildren().get(1).setVisible(false);

            setImages();

            collapseButton.getGraphic().rotateProperty().setValue(0);
        }
        else{
            MenuAnchor.setPrefWidth(225.0);
            tabsVBox.setPrefWidth(225.0);
            TabsHBox.getChildren().get(0).setVisible(true);
            TabsHBox.getChildren().get(1).setVisible(true);

            setText();

            collapseButton.getGraphic().rotateProperty().setValue(180);
        }
    }

    public void setUserList() throws Exception{
        UserListController.setView();
    }
    public void setEQList() throws Exception{
        EquipmentListController.setView();
    }
    public void setAssignList() throws Exception{
        AssignmentListController.setView();
    }

    private void setImages(){
        UserList.setText(null);
        ImageView user = new ImageView(new Image("resource/user_icon.png"));
        user.setFitWidth(30);
        user.setFitHeight(30);
        UserList.setGraphic(user);

        EQList.setText(null);
        ImageView eq = new ImageView(new Image("resource/equipment.png"));
        eq.setFitHeight(30);
        eq.setFitWidth(30);
        EQList.setGraphic(eq);

        AssignList.setText(null);
        ImageView assign = new ImageView(new Image("resource/assignment.png"));
        assign.setFitHeight(30);
        assign.setFitWidth(30);
        AssignList.setGraphic(assign);
    }

    private void setText(){
        UserList.setText("User List");
        UserList.setGraphic(null);

        EQList.setText("Equipment List");
        EQList.setGraphic(null);

        AssignList.setText("Assignment List");
        AssignList.setGraphic(null);
    }
}
