package dk.au.mad22spring.appproject.group22.ideanator.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.finalActivity.FinalActivity;
import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.model.Round;
import dk.au.mad22spring.appproject.group22.ideanator.roundActivity.RoundActivity;

public class MainActivityViewModel extends ViewModel {

    public Repository repository = Repository.getInstance();
    private ValueEventListener listener;

    public void CreateGame(ActivityResultLauncher<Intent> launcher, Intent intent, Context app) {
        Game theGame = new Game();

        ArrayList<Round> rounds = new ArrayList<>();
        Repository.getStaticInstance().collection("ProblemCards").get().addOnCompleteListener(task -> {

            for (QueryDocumentSnapshot document : task.getResult()) {
                Round round = new Round(document.get("English", String.class));
                rounds.add(round);
            }
            // https://www.geeksforgeeks.org/shuffle-elements-of-arraylist-in-java/
            Collections.shuffle(rounds);


            theGame.setRounds(rounds);

           repository.thePlayer.setAdmin(true);

            theGame.getPlayers().add(repository.thePlayer);

            // Generate joinCode here
            // No great as it only checks if the code exists once.
            // https://stackoverflow.com/questions/20389890/generating-a-random-number-between-1-and-10-java
            Random rand = new Random();
            int randomNum = rand.nextInt((1000 - 100) + 1) + 100;
            DatabaseReference myJoinCodeRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + randomNum);
            myJoinCodeRef.get().addOnCompleteListener(task1 -> {
                if (task1.getResult().getValue() == null)
                    repository.joinCode = Integer.toString(randomNum);
                else {
                    int randomNum1 = rand.nextInt((1000 - 100) + 1) + 100;
                    repository.joinCode = Integer.toString(randomNum1);
                }


                DatabaseReference myRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + repository.joinCode);
                myRef.setValue(theGame);

                listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Game theGame1 = snapshot.getValue(Game.class);

                        for (int i = 0; i < theGame1.getPlayers().size(); i++) {
                            if (theGame1.getPlayers().get(i).getName().equals(repository.thePlayer.getName())) {
                                repository.thePlayer = theGame1.getPlayers().get(i);
                                repository.playerIndex = i;
                                Log.d("GAME", "PLAYER UPDATED");
                            }
                        }
                        repository.theGame.setValue(theGame1);
                        if (!repository.thePlayer.getAdmin() && repository.theGame.getValue().getState() == Game.gameState.ROUND) {
                            repository.currentGameState = Game.gameState.ROUND;
                            Intent intent1 = new Intent(app, RoundActivity.class);
                            launcher.launch(intent1);
                        }
                        else if (repository.theGame.getValue().getState() == Game.gameState.FINAL){
                            Intent intent1 = new Intent(app, FinalActivity.class);
                            launcher.launch(intent1);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };

                myRef.addValueEventListener(listener);

                launcher.launch(intent);
            });
        });
    }

    public void removeListener() {
        if (listener != null) {
            DatabaseReference myRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + repository.joinCode);
            myRef.removeEventListener(listener);
        }
    }
}
