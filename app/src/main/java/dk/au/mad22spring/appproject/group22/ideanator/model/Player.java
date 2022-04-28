package dk.au.mad22spring.appproject.group22.ideanator.model;



import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.API_Repository;

public class Player {
    private String imgUrl;
    private String name;
    private ArrayList<OptionCard> options;
    private Boolean isAdmin;

    public Player(){
        options = new ArrayList<OptionCard>();
        isAdmin = false;
    }
    public Player(boolean generateNewName)
    {
        setName("Default");
        if(generateNewName==true) {
            API_Repository.getInstance().getRandomName();
        }
        options = new ArrayList<OptionCard>();
        isAdmin = false;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public ArrayList<OptionCard> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<OptionCard> options) {
        this.options = options;
    }


}
