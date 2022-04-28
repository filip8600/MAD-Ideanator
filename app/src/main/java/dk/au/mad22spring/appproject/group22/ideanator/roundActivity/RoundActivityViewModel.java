package dk.au.mad22spring.appproject.group22.ideanator.roundActivity;

import androidx.lifecycle.ViewModel;

public class RoundActivityViewModel extends ViewModel {
    //public rep = Repository.getInstance()

    public String getUserName() {

        //return rep.player.getUserName()
        return "The Kinky Horse";
    }

    public String getImageUrl() {

        return "https://i.imgur.com/eROlKpP.png";
    }

    public String getRoundNumber() {
        return "1";
    }

    public String getProblem() {
        return "The tooth fairy has forgotten to whom the teeth belong - design a solution using ____";
    }
}
