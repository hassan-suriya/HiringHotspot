
package hiringhotspot;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainPlatformPaneController extends Application {
    int mouseDragStartX = 0,mouseDragStartY = 0;
    
    @FXML
    public AnchorPane mainPlatformPane;
    @FXML
    public BorderPane mainBorderPane;
    @FXML
    private  FontAwesomeIcon closeAppBtn;
    
    private Stage stage;
    private Alert alert;
    private static Alert msg;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPlatformPane.fxml"));
        mainPlatformPane = loader.load();
        
        Scene scene = new Scene(mainPlatformPane);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        
        mainBorderPane = (BorderPane) loader.<BorderPane>getNamespace().get("mainBorderPane");
        
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("login.fxml"));
        
        Pane view = loader2.load();
        mainBorderPane.setCenter(view);
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public void setCenterPane(Pane pane) {
        mainBorderPane.setCenter(pane);
    }
    
    /**
    * Handles the event when the user clicks the close button on the main window.
    * Displays a confirmation dialog and closes the application if the user clicks OK.
    * 
    * @param event the mouse event that triggered the method
    */
    @FXML
    public void closeApp(MouseEvent event){
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setX(725);
        alert.setY(400);
        alert.setHeaderText("Do you want to close the application?");
        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.OK){
                stage = (Stage) mainPlatformPane.getScene().getWindow();
                stage.close();
            } 
            else{
                stage.setOpacity(1);
            }
        });
    }
    
    public static void alert(String text, Alert.AlertType type){
        msg = new Alert(type);
        msg.setX(725);
        msg.setY(400);
        msg.setHeaderText(text);
        msg.show();
    }
    
    @FXML
    private void mainPlatformWindowDragged(MouseEvent event){
        stage =(Stage) mainPlatformPane.getScene().getWindow();
        stage.setX(event.getScreenX() - mouseDragStartX);
        stage.setY(event.getScreenY() - mouseDragStartY);
    }
    
    @FXML
    private void mainPlatformWindowPressed(MouseEvent event) {
        mouseDragStartX = (int) event.getSceneX();
        mouseDragStartY = (int) event.getSceneY();
    }

    public static String getFreelancerName(){
        if (LoginController.freelancerUsername != null){
            return LoginController.freelancerUsername;
        }
        else{
            return SignupController.freelancerUsername;
        }
    }
    
    public static String getClientName(){
        if (LoginController.clientUsername != null){
            return LoginController.clientUsername;
        }
        else{
            return SignupController.clientUsername;
        }
    }
}
