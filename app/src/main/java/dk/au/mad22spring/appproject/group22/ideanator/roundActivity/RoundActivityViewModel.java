package dk.au.mad22spring.appproject.group22.ideanator.roundActivity;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
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

    public void sendSelectedOption(int voteIndex) {
        DatabaseReference optionsRef = Repository
                .getRealtimeInstance()
                .getReference("Ideainator/Games/" + repository.joinCode+"/rounds/"+(getRoundNumber()-1))
                .child("playedOptions");

        optionsRef.get().addOnCompleteListener(task -> {
            GenericTypeIndicator<ArrayList<OptionCard>> t = new GenericTypeIndicator<ArrayList<OptionCard>>() {};
            ArrayList<OptionCard> options = task.getResult().getValue(t);

            if(options==null)options=new ArrayList<>();
            options.add(repository.thePlayer.getOptions().get(voteIndex));

            optionsRef.setValue(options);
            //Update game state
            DatabaseReference gameStateRef = Repository
                    .getRealtimeInstance()
                    .getReference("Ideainator/Games/" + repository.joinCode+"/state");
            gameStateRef.setValue(Game.gameState.VOTE);



           });
    }
    public void observeOnNumberOfOptionsSent(LifecycleOwner owner, CanHandleOptionsUpdate caller){
        repository.theGame.observe(owner, game -> {
            Log.d("adaptor", "something new has happened");
            int options=game.getRounds().get(getRoundNumber()-1).getPlayedOptions().size();
            caller.newOptionPlayed(options);
        });
    }

    public int getNumberOfPlayers() {
        return repository.theGame.getValue().getPlayers().size();
    }

    public interface CanHandleOptionsUpdate{
        void newOptionPlayed(int numberOfOptionsPlayed);
    }
}
