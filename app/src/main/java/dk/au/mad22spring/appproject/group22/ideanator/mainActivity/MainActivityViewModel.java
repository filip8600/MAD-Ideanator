package dk.au.mad22spring.appproject.group22.ideanator.mainActivity;

import androidx.lifecycle.ViewModel;

import dk.au.mad22spring.appproject.group22.ideanator.GameManager;
import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.model.Player;

public class MainActivityViewModel extends ViewModel {

    public Repository repository = Repository.getInstance();


    public void CreateGame() {
        GameManager.getInstance().startNewGame();
    }

    public void createPlayer() {
        repository.thePlayer = new Player(true);
    }
}
