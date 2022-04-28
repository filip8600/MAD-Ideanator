package dk.au.mad22spring.appproject.group22.ideanator.mainActivity;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.finalActivity.FinalActivity;
import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.model.Player;
import dk.au.mad22spring.appproject.group22.ideanator.model.Round;
import dk.au.mad22spring.appproject.group22.ideanator.roundActivity.RoundActivity;

public class MainActivityViewModel extends ViewModel {

    public Repository repository = Repository.getInstance();
    private ValueEventListener listener;

    public void CreateGame(ActivityResultLauncher<Intent> launcher, Intent intent, Context app) {
        Game theGame = new Game();

        ArrayList<Round> rounds = new ArrayList<>();
        rounds.add(new Round("This be a _ 1"));
        rounds.add(new Round("This be a _ 2"));
        rounds.add(new Round("This be a _ 3"));

        theGame.setRounds(rounds);
        //      theGame.setProblems(problems);
        //      theGame.setOptions(Options);

        Player player = new Player();
        //player.setName("TheAdmin"); //Name overwritten after API call
        player.setAdmin(true);
        repository.thePlayer = player;

        theGame.getPlayers().add(player);

        // Generate joinCode here
        // No great as it only checks if the code exists once.
        // https://stackoverflow.com/questions/20389890/generating-a-random-number-between-1-and-10-java
        Random rand = new Random();
        int randomNum = rand.nextInt((1000 - 100) + 1) + 100;
        DatabaseReference myJoincodeRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + Integer.toString(randomNum));
        myJoincodeRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.getResult().getValue() == null)
                    repository.joinCode = Integer.toString(randomNum);
                else {
                    int randomNum = rand.nextInt((1000 - 100) + 1) + 100;
                    repository.joinCode = Integer.toString(randomNum);
                }


                DatabaseReference myRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + repository.joinCode);
                myRef.setValue(theGame);

                listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        repository.theGame.setValue(snapshot.getValue(Game.class));
                        for (Integer i = 0; i < repository.theGame.getValue().getPlayers().size(); i++) {
                            if (repository.theGame.getValue().getPlayers().get(i).getName() == player.getName()) {
                                repository.thePlayer = repository.theGame.getValue().getPlayers().get(i);
                            }
                        }
                        if (repository.thePlayer.getAdmin() == false && repository.theGame.getValue().getState() == Game.gameState.ROUND){
                            Intent intent = new Intent(app,RoundActivity.class);
                            launcher.launch(intent);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };

                myRef.addValueEventListener(listener);

                launcher.launch(intent);
            }
        });
    }

    public void removeListener() {
        if (listener != null) {
            DatabaseReference myRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + repository.joinCode);
            myRef.removeEventListener(listener);
        }
    }
}
