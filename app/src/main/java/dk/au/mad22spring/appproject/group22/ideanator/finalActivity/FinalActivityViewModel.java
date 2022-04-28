package dk.au.mad22spring.appproject.group22.ideanator.finalActivity;

import androidx.lifecycle.ViewModel;

import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.model.Round;

public class FinalActivityViewModel extends ViewModel {
    private Repository repo= Repository.getInstance();

    public String getSolution(int position) {
        Round round= repo.theGame.getValue().getRounds().get(position);
        return round.getWinner().getOption();
    }
}
