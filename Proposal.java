
package hiringhotspot;

import javafx.scene.image.Image;

public class Proposal {
    private int id;
    private String title;
    private int amount;
    private String datePosted;
    private String Name;
    private String location;
    private String Proposal;
    private Image profilepic;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProposal() {
        return Proposal;
    }

    public void setProposal(String Proposal) {
        this.Proposal = Proposal;
    }

    public Image getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(Image profilepic) {
        this.profilepic = profilepic;
    }

    public Proposal(int id, String title, int amount, String datePosted, String Name, String location, String Proposal, Image profilepic, String username) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.datePosted = datePosted;
        this.Name = Name;
        this.location = location;
        this.Proposal = Proposal;
        this.profilepic = profilepic;
        this.username = username;
    }
}
