package dk.au.mad22spring.appproject.group22.ideanator.roundActivity;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;
import dk.au.mad22spring.appproject.group22.ideanator.model.Round;

public class RoundActivityViewModel extends ViewModel {
    public Repository repository= Repository.getInstance();

    public String getUserName() {
        return repository.thePlayer.getName();
    }

    public String getImageUrl() {

        return repository.thePlayer.getImgUrl();
    }

    public int getRoundNumber() {
        return repository.theGame.getValue().getRoundCounter();
    }

    public String getProblem() {
        ArrayList<Round> rounds=repository.theGame.getValue().getRounds();
        if(rounds!= null) return rounds.get(getRoundNumber()-1).getProblems();
        else return "Error codes would be a thing of the past with _";
    }

    public ArrayList<OptionCard> getOptionCards() {
        return repository.thePlayer.getOptions();
    }
}
