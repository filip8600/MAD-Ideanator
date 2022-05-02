package dk.au.mad22spring.appproject.group22.ideanator.finalActivity;

import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.IdeainatorApplication;
import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;
import dk.au.mad22spring.appproject.group22.ideanator.model.Round;

public class FinalActivityViewModel extends ViewModel {
    private Repository repo= Repository.getInstance();

    public String getSolution(int position) {
        if(repo.theGame.getValue()==null) {
            return "Game is missing :(";
        }
        else{
            Round round= repo.theGame.getValue().getRounds().get(position);
            //Get text:
            String prob=round.getProblems();
            OptionCard winner=round.getWinner();
            String winnerOption;
            if(winner!=null) winnerOption = winner.getOption();
            else winnerOption="foodProcessor";

            //Replace _ with option:
            return prob.replace("_","\""+winnerOption+"\"");
        }
    }

    public int getNumberOfRounds() {
        ArrayList<Round> rounds=Repository.getInstance().theGame.getValue().getRounds();
        if(rounds!=null) return rounds.size();
        else return 1;
    }
}
