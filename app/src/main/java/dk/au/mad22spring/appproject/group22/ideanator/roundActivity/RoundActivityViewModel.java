package dk.au.mad22spring.appproject.group22.ideanator.roundActivity;

import android.app.LauncherActivity;
import android.content.Intent;
import android.graphics.Path;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.core.ServerValues;
import com.google.firestore.v1.DocumentTransform;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;
import dk.au.mad22spring.appproject.group22.ideanator.model.Round;

public class RoundActivityViewModel extends ViewModel {
    public boolean hasChosenOption = false;

    public Repository repository= Repository.getInstance();

    public String getUserName() {
        return repository.thePlayer.getName();
    }

    public String getImageUrl() {

        return repository.thePlayer.getImgUrl();
    }

    public int getRoundNumber() {
        if(repository.theGame.getValue()==null){
            Log.e("tag", "getRoundNumber: Thats not good :(" );
            return 1;
        }
        return repository.theGame.getValue().getRoundCounter();
    }

    public String getProblem() {
        ArrayList<Round> rounds=repository.theGame.getValue().getRounds();
        if(rounds!= null) return rounds.get(getRoundNumber()-1).getProblems();
        else return "Error codes would be a thing of the past with _";
    }

    public ArrayList<OptionCard> getOptionCards() {
        ArrayList<OptionCard> options= new ArrayList<>();
        for (OptionCard option:repository.thePlayer.getOptions()){
            if (option !=null) options.add(option);
        }
        return options;
    }

    public void sendSelectedOption(int voteIndex, CanHandleOptionsUpdate caller) {
        // Set chosen option
        DatabaseReference optionsRef = Repository
                .getRealtimeInstance()
                .getReference("Ideainator/Games/" + repository.joinCode+"/rounds/"+(getRoundNumber()-1))
                .child("playedOptions/"+repository.playerIndex);
        optionsRef.setValue(repository.thePlayer.getOptions().get(voteIndex));

        //Remove chosen option
        DatabaseReference OwnOptionsRef = Repository
                .getRealtimeInstance()
                .getReference("Ideainator/Games/" + repository.joinCode+"/players/"+repository.playerIndex+"/options");
        ArrayList<OptionCard> options = repository.thePlayer.getOptions();
        options.remove(voteIndex);
        OwnOptionsRef.setValue(options);

        //Update game state
        DatabaseReference gameStateRef = Repository
                .getRealtimeInstance()
                .getReference("Ideainator/Games/" + repository.joinCode+"/state");
        gameStateRef.setValue(Game.gameState.VOTE);
        gameStateRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                caller.startIntent();
            }
        });
    }
    public void observeOnNumberOfOptionsSent(LifecycleOwner owner, CanHandleOptionsUpdate caller){
        repository.theGame.observe(owner, game -> {
            Log.d("adaptor", "something new has happened");
            int numberOfOptions = getNumberOfOptions(game);
            caller.newOptionPlayed(numberOfOptions);
        });
    }

    private int getNumberOfOptions(Game game) {
        ArrayList<OptionCard> Options= game.getRounds().get(getRoundNumber()-1).getPlayedOptions();
        int numberOfOptions =0;
        for(OptionCard card: Options){
            if(card!=null&&card.getOption()!=null)numberOfOptions++;
        }
        return numberOfOptions;
    }

    public int getNumberOfPlayers() {
        return repository.theGame.getValue().getPlayers().size();
    }

    public interface CanHandleOptionsUpdate{
        void newOptionPlayed(int numberOfOptionsPlayed);
        void startIntent();
    }
}
