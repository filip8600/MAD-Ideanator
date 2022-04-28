package dk.au.mad22spring.appproject.group22.ideanator.roundActivity;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;
import dk.au.mad22spring.appproject.group22.ideanator.model.Round;

public class RoundActivityViewModel extends ViewModel {
    public Repository repository= Repository.getInstance();

    public String getUserName() {

        //return rep.player.getUserName()
        return repository.thePlayer.getName();
    }

    public String getImageUrl() {

        return repository.thePlayer.getImgUrl();
    }

    public String getRoundNumber() {
        return "1";
    }

    public String getProblem() {
        ArrayList<Round> rounds=repository.theGame.getValue().getRounds();
        if(rounds!= null) return rounds.get(1).getProblems();//Todo fix 1
        else return "Error codes would be a thing of the past with _";
    }

    public List<OptionCard> getOptionCards() {
        OptionCard card= new OptionCard();
        card.setOption("Meget god løsning");
        ArrayList<OptionCard> cards= new ArrayList<OptionCard>();
        cards.add(card);
        return cards;
    }
}
