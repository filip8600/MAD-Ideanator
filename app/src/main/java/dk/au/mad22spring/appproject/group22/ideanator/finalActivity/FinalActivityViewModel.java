package dk.au.mad22spring.appproject.group22.ideanator.finalActivity;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.GameManager;
import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;
import dk.au.mad22spring.appproject.group22.ideanator.model.Round;

public class FinalActivityViewModel extends ViewModel {
    private final Repository repo = Repository.getInstance();
    private ArrayList<String> winners;

    public FinalActivityViewModel() {
        GameManager.getInstance().removeListeners();
    }

    public String getSolution(int position) {
        if(winners==null) {
            winners=new ArrayList<String>();
            for (int i=0;i<getNumberOfRounds();i++) winners.add(null);
        }
        if(winners.get(position)!=null) return winners.get(position);
        if (repo.theGame.getValue() == null) {
            return "Game is missing :(";
        } else {
            Round round = repo.theGame.getValue().getRounds().get(position);
            //Get text:
            String prob = round.getProblems();
            OptionCard winner = round.getWinner();
            String winnerOption;
            if (winner != null) winnerOption = winner.getOption();
            else winnerOption = "foodProcessor";

            //Replace _ with option:
            winners.set(position,prob.replace("_", "\"" + winnerOption + "\""));
            return winners.get(position);
        }
    }

    public int getNumberOfRounds() {
        ArrayList<Round> rounds = Repository.getInstance().theGame.getValue().getRounds();
        if (rounds != null) return rounds.size();
        else return 1;
    }

    public void deleteGame() {
        Repository.deleteGame();
    }
}
