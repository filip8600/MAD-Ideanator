package dk.au.mad22spring.appproject.group22.ideanator;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

import dk.au.mad22spring.appproject.group22.ideanator.finalActivity.FinalActivity;
import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.model.Player;
import dk.au.mad22spring.appproject.group22.ideanator.model.Round;
import dk.au.mad22spring.appproject.group22.ideanator.roundActivity.RoundActivity;

public class GameManager {
    //Singleton:
    private static GameManager gameManager;
    //Class attributes:
    private final Repository repository = Repository.getInstance();
    private final Context app = IdeainatorApplication.getAppContext();
    private DatabaseReference joinRef;
    private ValueEventListener listener;

    //Singleton:
    static public GameManager getInstance() {
        if (gameManager == null) gameManager = new GameManager();
        return gameManager;
    }

    public void startNewGame() {
        // Generate joinCode here
        // No great as it only checks if the code exists once.
        // https://stackoverflow.com/questions/20389890/generating-a-random-number-between-1-and-10-java
        Random rand = new Random();
        int randomNum = rand.nextInt((1000 - 100) + 1) + 100;
        joinRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + randomNum);
        joinRef.get().addOnCompleteListener(task1 -> {
            if (task1.getResult().getValue() == null) {
                repository.joinCode = Integer.toString(randomNum);
                finishCreatingGame();
            } else {
                int randomNum1 = rand.nextInt((10000 - 1000) + 1) + 1000;
                repository.joinCode = Integer.toString(randomNum1);
                joinRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + randomNum1);
                finishCreatingGame();
            }
        });

    }

    private void finishCreatingGame() {
        String playerName = Repository.getInstance().thePlayer.getName();
        Game newLocalGame = new Game();

        repository.thePlayer.setAdmin(true);

        newLocalGame.getPlayers().add(repository.thePlayer);
        addProblemcardsToRemote();
        joinRef.setValue(newLocalGame);

        addGameListener(playerName);
    }

    private void addProblemcardsToRemote() {
        ArrayList<Round> rounds = new ArrayList<>();
        Repository.getStaticInstance().collection("ProblemCards").get().addOnCompleteListener(task -> {
            //Determine app language and serve correct problems
            String language = Locale.getDefault().getLanguage();
            String danish = IdeainatorApplication.getAppContext().getString(R.string.Danish);
            String english = IdeainatorApplication.getAppContext().getString(R.string.English);
            //todo: start a random spot in the array
            for (QueryDocumentSnapshot document : task.getResult()) {
                if (rounds.size() >= repository.theGame.getValue().numberOfRounds) break;
                Round round;
                if (language.contains("da")) round = new Round(document.get(danish, String.class));
                else round = new Round(document.get(english, String.class));

                rounds.add(round);
            }
            // https://www.geeksforgeeks.org/shuffle-elements-of-arraylist-in-java/
            Collections.shuffle(rounds);

            joinRef.child("rounds").setValue(rounds);
        });
    }

    public void joinExistingGame(String joinCode) {
        String playerName = Repository.getInstance().thePlayer.getName();
        repository.thePlayer.setAdmin(false);
        repository.joinCode = joinCode;


        joinRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + joinCode);

        joinRef.get().addOnCompleteListener(task -> {

            if (task.getResult().getValue() == null) {
                Toast.makeText(app, IdeainatorApplication.getAppContext().getString(R.string.RoomNotFound), Toast.LENGTH_SHORT).show();
                return;
            }
            addGameListener(playerName);

            // Adds player to list of players and opens lobby (only if the game is not started)
            addLocalPlayerToRemote(joinCode);
        });

    }

    private void addGameListener(String playerName) {
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // This will keep the value of game and player updated
                Game remoteGame = dataSnapshot.getValue(Game.class);
                // This will open the round view when the admin starts the game:
                progressToCorrectActivity(remoteGame);

                repository.theGame.setValue(remoteGame);
                getPlayerIndex(playerName, remoteGame);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        };
        joinRef.addValueEventListener(listener);
    }

    private void progressToCorrectActivity(Game remoteGame) {
        if (repository.theGame.getValue() == null) return;
        Game.gameState remoteState = remoteGame.getState();
        Game.gameState localState = repository.theGame.getValue().getState();

        if (!repository.thePlayer.getAdmin() &&
                localState != Game.gameState.ROUND &&
                remoteState == Game.gameState.ROUND) {
            Intent roundIntent = new Intent(app, RoundActivity.class);
            roundIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//Clear top of callstack - so you cant go back
            app.startActivity(roundIntent);
        } else if (repository.theGame.getValue().getState() != Game.gameState.FINAL && remoteState == Game.gameState.FINAL) {
            Intent finalActivityIntent = new Intent(app, FinalActivity.class);
            finalActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//Clear callstack - so you cant go back
            app.startActivity(finalActivityIntent);
        }
    }

    private void addLocalPlayerToRemote(String joinCode) {
        DatabaseReference myRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + joinCode + "/players");
        myRef.get().addOnCompleteListener(task1 -> {
            if (repository.theGame.getValue().getState() == Game.gameState.LOBBY) {
                GenericTypeIndicator<ArrayList<Player>> t = new GenericTypeIndicator<ArrayList<Player>>() {
                };
                ArrayList<Player> players = task1.getResult().getValue(t);

                players.add(repository.thePlayer);
                myRef.setValue(players);
            } else {
            }
        });
    }

    private void getPlayerIndex(String playerName, Game remoteGame) {
        int i = 0;
        for (Player remotePlayer : remoteGame.getPlayers()) {
            if (remotePlayer.getName().equals(playerName)) {
                repository.thePlayer = remotePlayer;
                repository.playerIndex = i;
            }
            i++;
        }
    }

    public void removeListeners() {
        if (listener != null) joinRef.removeEventListener(listener);
    }
}
