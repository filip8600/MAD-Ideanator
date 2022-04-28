package dk.au.mad22spring.appproject.group22.ideanator.roundActivity;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;

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

    public List<OptionCard> getOptionCards() {
        OptionCard card= new OptionCard();
        card.setOption("Meget god løsning");
        ArrayList<OptionCard> cards= new ArrayList<OptionCard>();
        cards.add(card);
        return cards;
    }
}
