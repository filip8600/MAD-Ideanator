package dk.au.mad22spring.appproject.group22.ideanator.mainActivity;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.finalActivity.FinalActivity;
import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;
import dk.au.mad22spring.appproject.group22.ideanator.model.Player;
import dk.au.mad22spring.appproject.group22.ideanator.model.Round;
import dk.au.mad22spring.appproject.group22.ideanator.roundActivity.RoundActivity;

public class MainActivityViewModel extends ViewModel {

    public Repository repository = Repository.getInstance();
    private ValueEventListener listener;

    public void CreateGame(ActivityResultLauncher<Intent> launcher, Intent intent, Context app) {
        Game theGame = new Game();

        ArrayList<Round> rounds = new ArrayList<>();
        Repository.getStaticInstance().collection("ProblemCards").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot document : task.getResult()) {
                    Round round = new Round(document.get("English", String.class));
                    rounds.add(round);
                }
                // https://www.geeksforgeeks.org/shuffle-elements-of-arraylist-in-java/
                Collections.shuffle(rounds);


                theGame.setRounds(rounds);
                //      theGame.setProblems(problems);
                //      theGame.setOptions(Options);

               //Player player = new Player(true);
               //player.setName("TheAdmin"); //Name overwritten after API call
               //player.setAdmin(true);
               repository.thePlayer.setAdmin(true);

                theGame.getPlayers().add(repository.thePlayer);

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
                                Game theGame = snapshot.getValue(Game.class);

                                for (Integer i = 0; i < theGame.getPlayers().size(); i++) {
                                    if (theGame.getPlayers().get(i).getName().equals(repository.thePlayer.getName())) {
                                        repository.thePlayer = theGame.getPlayers().get(i);
                                        repository.playerIndex = i;
                                        Log.d("GAME", "PLAYERUPDTAED");
                                    }
                                }
                                repository.theGame.setValue(theGame);
                                if (repository.thePlayer.getAdmin() == false && repository.theGame.getValue().getState() == Game.gameState.ROUND) {
                                    repository.currentGameState = Game.gameState.ROUND;
                                    Intent intent = new Intent(app, RoundActivity.class);
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
        });
    }

    public void removeListener() {
        if (listener != null) {
            DatabaseReference myRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + repository.joinCode);
            myRef.removeEventListener(listener);
        }
    }
}
