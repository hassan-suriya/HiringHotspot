
package hiringhotspot;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class ClientDirectoryController implements Initializable {
    private Stage stage;
    private Alert alert;
    int x = 0, y = 0;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Statement statement = null;
    public static Image clientImage = null;
    public static String clientFullName = null;
    
    Image freelancerImage = null;
    int jobID = 0;
    String jobTitle = null;
    int jobAmount = 0;
    String jobDeadline = null;
    String jobPosted = null;
    String freelancerName = null;
    String freelancerLocation = null;
    
    @FXML
    private FontAwesomeIcon freelancerCloseAppBtn;
    @FXML
    private GridPane gridFixedBarFreelancer;
    @FXML
    private FontAwesomeIcon jobsButtonFreelancer;
    @FXML
    private Circle myProfileCircle;
    @FXML
    private BorderPane jobsBorderPane;
    @FXML
    private Label myJobFeedLogo;
    @FXML
    private ScrollPane jobListScrollPane;
    @FXML
    private VBox jobListViewClient;
    @FXML
    private AnchorPane clientDirectoryMainPage;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Call methods to load client details and job list when the controller is initialized
        loadClientDetails();
        loadCurrentJobList();
    }    
    
    public void loadClientDetails(){
        // Get the username of the client from the main platform pane controller
        String username = MainPlatformPaneController.getClientName();
        
        // Connect to the database
        DB connect = new DB();
        Connection con = connect.getConnection();
        
        // Query the database for the client's details
        String query2 = "SELECT * FROM accounts WHERE username = '" + username + "' ";
        try{
            statement = con.createStatement();
            resultSet = statement.executeQuery(query2);
            
            // If the client exists in the database, set their profile picture and full name
            if (resultSet.next()){
                InputStream in = resultSet.getBinaryStream("ProfilePic");
                clientFullName = resultSet.getString("FullName");
                
                BufferedImage bufferedImage = ImageIO.read(in);
                clientImage = SwingFXUtils.toFXImage(bufferedImage, null);
                myProfileCircle.setFill(new ImagePattern(clientImage)); 
            }
        }catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(FreelancerDirectoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadCurrentJobList(){
        String username = MainPlatformPaneController.getClientName();
        gridFixedBarFreelancer.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
        jobListScrollPane.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
        DB connect = new DB();
        Connection con = connect.getConnection();

//        String query = "SELECT jp.id, jp.Title, jp.Amount, jp.Deadline, jp.datePosted, ja.Proposal, a.FullName as freelancer_name, a.Location as freelancer_location, a.ProfilePic as freelancer_profile_pic FROM jobsPosted jp LEFT JOIN jobsApplied ja ON jp.id = ja.id LEFT JOIN accounts a ON ja.Username = a.Username AND a.AccountType = 'Freelancer' WHERE jp.Username = '" + username + "' AND jp.HiringStatus = 'Not Hired'";
        String query = "SELECT id, Title, Amount, datePosted FROM jobsPosted WHERE Username = '" + username + "' AND HiringStatus = 'Not Hired'";
        try{
            statement = con.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                jobID = resultSet.getInt("id");
                jobTitle = resultSet.getString("Title");
                jobAmount = resultSet.getInt("Amount");
                jobPosted = resultSet.getString("datePosted");
                
                clientJobs job = new clientJobs(jobID, jobTitle, jobAmount, jobPosted);
                
                BorderPane jobPane = new BorderPane();
                jobPane.setStyle("-fx-border-color: #f5f5f5; -fx-border-radius: 0;");
                                
                jobPane.setOnMouseEntered(e -> {
                    jobPane.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #f5f5f5; -fx-border-radius: 0; -fx-cursor: hand;");
                });
                jobPane.setOnMouseExited(e -> {
                    jobPane.setStyle("-fx-background-color: white; -fx-border-color: #f5f5f5; -fx-border-radius: 0;");
                });
                
                Label title = new Label(jobTitle);
                title.setFont(Font.font("SansSerif", FontWeight.BOLD, 20));
                title.setTextFill(Color.web("#1679c9"));
                title.setTranslateX(50);
                title.setTranslateY(5);
                
                Label amount = new Label("$" + String.valueOf(jobAmount));
                amount.setFont(Font.font("Dubai Medium", FontWeight.BOLD, 14));
                amount.setTextFill(Color.web("#1679c9"));
                amount.setTranslateX(50);
                
                Label id = new Label("ID: " + jobID);
                id.setFont(Font.font("Dubai Medium", FontWeight.BOLD, 14));
                id.setTextFill(Color.web("#1679c9"));
                id.setTranslateX(50);
                id.setTranslateY(8);
                
                Label posted = new Label("Posted on: " + jobPosted);
                posted.setFont(Font.font("Dubai Medium", FontWeight.BOLD, 14));
                posted.setTextFill(Color.web("#1679c9"));
                posted.setTranslateX(-50);
                
                jobPane.setTop(title);
                jobPane.setLeft(id);
                jobPane.setBottom(amount);
                jobPane.setRight(posted);
                jobPane.setOnMouseClicked(ev -> {
                    try{
                        // load the FXML file for the job application view
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("clientApplications.fxml"));
                        Pane view = loader.load();

                        // set the job details in the job application view
                        // pass the job details as arguments to the constructor of the controller class for the job application view
                        ClientApplicationsController controller = loader.getController();
                        controller.setApplicationDetails(job);

                        // display the job application view in the border pane
                        jobsBorderPane.setTop(null);
                        jobsBorderPane.setCenter(view);
                        
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                });
               
                jobListViewClient.getChildren().add(jobPane);
                jobListViewClient.setSpacing(0);
                jobListViewClient.setPrefWidth(1240);
                jobListViewClient.setPrefHeight(520);

            }
            }catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } 
    }
    
    public void closeApp(MouseEvent event){
        stage = (Stage) clientDirectoryMainPage.getScene().getWindow();
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setX(725);
        alert.setY(400);
        alert.setHeaderText("Do you want to close the application?");
        alert.showAndWait().ifPresent(type -> {
            
            if (type == ButtonType.OK){
                stage =(Stage) clientDirectoryMainPage.getScene().getWindow();
                stage.close();
            } 
            else{
                stage.setOpacity(1);
            }
        });
    }
    
    @FXML
    private void cleintWindowDragged(MouseEvent event) {
        stage = (Stage) clientDirectoryMainPage.getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }
    
    @FXML
    private void clientWindowPressed(MouseEvent event) {
        x = (int) event.getSceneX();
        y = (int) event.getSceneY();
    }
}
