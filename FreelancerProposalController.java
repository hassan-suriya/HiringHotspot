
package hiringhotspot;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class FreelancerProposalController implements Initializable {
    
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Statement statement = null;
    
    @FXML
    private Stage stage;
    @FXML
    private BorderPane freelancerProposalBorderPane;
    @FXML
    private Label Title;
    @FXML
    private Label Amount;
    @FXML
    private Button HireBtn;
    @FXML
    private Label jobPosted;
    @FXML
    private Label freelancerProposalText;
    @FXML
    private Circle freelancerProfilePic;
    @FXML
    private Label freelancerName;
    @FXML
    private Label freelancerLocation;
    @FXML
    private Label ID;
    @FXML
    private Alert alert;
    
    int projectAmount = 0;
    String freelancerUsername = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void getFreelancerProposal(Proposal pro){
        projectAmount = pro.getAmount();
        Title.setText(pro.getTitle());
        ID.setText("Job ID: " + pro.getId());
        Amount.setText("Amount: $" + pro.getAmount() );
        jobPosted.setText("Posted On: " + pro.getDatePosted());
        freelancerName.setText("Name: " + pro.getName());
        freelancerLocation.setText("Location: " + pro.getLocation());
        freelancerProposalText.setText(pro.getProposal());
        freelancerProfilePic.setFill(new ImagePattern(pro.getProfilepic()));
        freelancerUsername = pro.getUsername();
    }
    
    /**
     * Handles the Hire button action by updating the job status and creating a new contract.
     * 
     * @param event the ActionEvent object representing the button click event
     */
    public void HireBtnActionPerformed(ActionEvent event){
        int walletAmount = 0;
        
        // Connect to the database and retrieve the client's wallet balance
        DB connect = new DB();
        Connection con = connect.getConnection();
        String query = "SELECT funds FROM Wallet WHERE Username = '" + MainPlatformPaneController.getClientName() + "'";
        
        try{
            statement = con.createStatement();
            resultSet = statement.executeQuery(query);
            
            if (resultSet.next()){
                walletAmount = resultSet.getInt("funds");
            }
            
            // If the client does not have sufficient funds, display a confirmation alert
            if (walletAmount < projectAmount){  
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setX(725);
                alert.setY(400);
                
                alert.setHeaderText("You are about to create a contract on Hiring Hotspot.\nProject Amount: $" + projectAmount + "\n\nYou should have enough funds in your wallet before hiring.");
                
                Optional<ButtonType> result = alert.showAndWait();
                
                // If the user clicks OK, create the contract and update the job status
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    stage = (Stage) freelancerProposalBorderPane.getScene().getWindow();
                    stage.close();
                    
                    walletAmount = walletAmount - Integer.parseInt(Amount.getText());
                    
                    String query2 = "UPDATE jobsApplied, jobsPosted, Wallet SET Wallet.funds = '" + walletAmount + "', jobsPosted.HiringStatus = 'Hired', jobsApplied.jobCompletionStatus = 'In Progress' WHERE jobsApplied.id = '" + ID.getText() + "' AND jobsPosted.id = '" + ID.getText() + "' AND jobsApplied.Username = '" + freelancerUsername + "' AND Wallet.Username = '" + MainPlatformPaneController.getClientName() + "'";
                    try{
                        statement.executeUpdate(query2);
                        
                        String query3 = "INSERT INTO Communication (Freelancer, Client, Type) SELECT * FROM (SELECT '" + freelancerName + "', '" + MainPlatformPaneController.getClientName() + "', 'Allowed') AS tmp WHERE NOT EXISTS (SELECT * FROM Communication WHERE Freelancer = '" + freelancerName + "' AND Client = '" + MainPlatformPaneController.getClientName() + "' AND Type = 'Allowed') LIMIT 1";
                        statement.executeUpdate(query3);

                        MainPlatformPaneController.alert("Woo hoo! You have successfully created the contract.", Alert.AlertType.INFORMATION);
                        
                    } catch (SQLException ex) {
                        java.util.logging.Logger.getLogger(FreelancerProposalController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }     
                }
            }
        }catch (SQLException ex){
            java.util.logging.Logger.getLogger(FreelancerProposalController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }        
    }
}
