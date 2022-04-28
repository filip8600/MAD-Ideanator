package dk.au.mad22spring.appproject.group22.ideanator.finalActivity;

import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import dk.au.mad22spring.appproject.group22.ideanator.IdeainatorApplication;
import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.model.Round;

public class FinalActivityViewModel extends ViewModel {
    private Repository repo= Repository.getInstance();

    public String getSolution(int position) {
        if(repo.theGame.getValue()==null) Toast.makeText(IdeainatorApplication.getAppContext(), "Game is missing :(", Toast.LENGTH_SHORT).show();

        Round round= repo.theGame.getValue().getRounds().get(position);
        return round.getWinner().getOption();
    }
}
