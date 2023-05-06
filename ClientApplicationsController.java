
package hiringhotspot;

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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javax.imageio.ImageIO;

public class ClientApplicationsController implements Initializable {

    @FXML
    private BorderPane applicationBorderPane;
    @FXML
    private Label myCurrentJobs;
    @FXML
    private ScrollPane applicationListScrollPane;
    @FXML
    private VBox applicationsBoxList;
    @FXML
    private Circle freelancerPicCircle;
    
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Statement statement = null;
    
    Image freelancerImage = null;
    String freelancerName = null;
    String freelancerLocation = null;
    int jobID = 0;
    String jobTitle = null;
    int jobAmount = 0;
    String jobDeadline = null;
    String jobPosted = null;
    ImageView profilePic = null;
    String freelancerProposal = null;
    String freelancerUsername = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }   
    
    public void setApplicationDetails(clientJobs job) {
        jobID = job.getId();
        jobTitle = job.getTitle();
        jobAmount = job.getAmount();
        jobPosted = job.getPosted();

        loadFreelancerDetails();
    }
    
    public void loadFreelancerDetails(){
        try (Connection con = new DB().getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT j.*, a.FullName, a.ProfilePic, a.Location FROM jobsApplied j INNER JOIN accounts a ON j.Username = a.Username WHERE a.AccountType = 'Freelancer' AND j.id = '" + jobID + "'");) {
            
            while (resultSet.next()) {
                freelancerUsername = resultSet.getString("Username");
                freelancerProposal = resultSet.getString("Proposal");
                freelancerName = resultSet.getString("FullName");
                freelancerLocation = resultSet.getString("Location");
                InputStream in = resultSet.getBinaryStream("ProfilePic");
                
                BufferedImage bufferedImage = ImageIO.read(in);
                freelancerImage = SwingFXUtils.toFXImage(bufferedImage, null);
                
                loadApplications();
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(ClientApplicationsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadApplications(){
        BorderPane jobPane = new BorderPane();
        jobPane.setStyle("-fx-border-color: #f5f5f5; -fx-border-radius: 0;");

        jobPane.setOnMouseEntered(e -> {
            jobPane.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #f5f5f5; -fx-border-radius: 0; -fx-cursor: hand;");
        });
        jobPane.setOnMouseExited(e -> {
            jobPane.setStyle("-fx-background-color: white; -fx-border-color: #f5f5f5; -fx-border-radius: 0;");
        });
        
        
        Label name = new Label(freelancerName + " (" + freelancerLocation + ")");
        name.setFont(Font.font("SansSerif", FontWeight.BOLD, 20));
        name.setTextFill(Color.web("#1679c9"));
        name.setTranslateX(50);
        name.setTranslateY(5);
 
        // Create an ImageView with the image you want to display
        ImageView imageView = new ImageView(freelancerImage);
        int size = 50;
        // Set the fit width and height of the ImageView
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);

        // Create a StackPane and add the ImageView to it
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(imageView);

        // Create a Circle with the same size as the StackPane
        Circle circle = new Circle(size/2, size/2, Math.min(size, size)/2);

        // Set the clip property of the StackPane to the Circle
        stackPane.setClip(circle);
        
        jobPane.setCenter(name);
        jobPane.setLeft(stackPane);
        
        Proposal proposal = new Proposal(jobID, jobTitle, jobAmount, jobPosted, freelancerName, freelancerLocation, freelancerProposal, freelancerImage, freelancerUsername);
        
        jobPane.setOnMouseClicked(ev -> {
            try {
                // load the FXML file for the job application view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FreelancerProposal.fxml"));
                Pane view = loader.load();

                // set the job details in the job application view
                // you can pass the job details as arguments to the constructor of the controller class for the job application view
                FreelancerProposalController controller = loader.getController();
                controller.getFreelancerProposal(proposal);

                // display the job application view in the border pane
                applicationBorderPane.setTop(null);
                applicationBorderPane.setCenter(view);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        applicationsBoxList.getChildren().add(jobPane);
        applicationsBoxList.setSpacing(0);
        applicationsBoxList.setPrefWidth(1240);
        applicationsBoxList.setPrefHeight(520);
    }
}
