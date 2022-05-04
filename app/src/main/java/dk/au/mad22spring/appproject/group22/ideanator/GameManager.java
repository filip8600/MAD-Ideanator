package dk.au.mad22spring.appproject.group22.ideanator;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
    //Singleton Mechanism
    private static GameManager gameManager;
    static public GameManager getInstance(){
        if(gameManager==null)gameManager=new GameManager();
        return gameManager;
    }

    //Class atributes:
    private Repository repository = Repository.getInstance();
    private Context app =IdeainatorApplication.getAppContext();
    private DatabaseReference joinRef;
    private ValueEventListener joinListener;




    public void startNewGame() {

        Game theGame = new Game();

        ArrayList<Round> rounds = new ArrayList<>();
        Repository.getStaticInstance().collection("ProblemCards").get().addOnCompleteListener(task -> {
            //Determine app language and serve correct problems
            String language = Locale.getDefault().getLanguage();
            String danish = IdeainatorApplication.getAppContext().getString(R.string.Danish);
            String english = IdeainatorApplication.getAppContext().getString(R.string.English);
            for (QueryDocumentSnapshot document : task.getResult()) {
                if(rounds.size()>=theGame.numberOfRounds) break;
                Round round;
                if (language.contains("da")) round = new Round(document.get(danish, String.class));
                else round = new Round(document.get(english, String.class));

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


                DatabaseReference newGameRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + repository.joinCode);
                newGameRef.setValue(theGame);

                ValueEventListener gameListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Game theGame1 = snapshot.getValue(Game.class);

                        //Move to correct activity if needed:
                        Game.gameState state = theGame1.getState();
                        if (repository.theGame.getValue() == null) {
                            Log.d("TAG", "onDataChange: øv");
                        }
                        else if (repository.theGame.getValue().getState() != Game.gameState.FINAL && state == Game.gameState.FINAL) {
                            Intent finalActivityIntent = new Intent(app, FinalActivity.class);
                            finalActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//Clear callstack - so you cant go back
                            app.startActivity(finalActivityIntent);
                        }
                        //find "you" among other players
                        for (int i = 0; i < theGame1.getPlayers().size(); i++) {
                            if (theGame1.getPlayers().get(i).getName().equals(repository.thePlayer.getName())) {
                                repository.thePlayer = theGame1.getPlayers().get(i);
                                repository.playerIndex = i;
                                Log.d("GAME", "PLAYER UPDATED");
                            }
                        }
                        //Update local copy with new online data
                        repository.theGame.setValue(theGame1);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("createGame", "onCancelled: gameListener encountered an error: ",error.toException() );
                    }
                };



                newGameRef.addValueEventListener(gameListener);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //IdeainatorApplication.getAppContext().startActivity(intent);
                //launcher.launch(intent);
            });
        });
    }
    public void joinExistingGame(String joinCode){
        String playerName = Repository.getInstance().thePlayer.getName();
        repository.thePlayer.setAdmin(false);
        repository.joinCode = joinCode;


        joinRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + joinCode);

        joinRef.get().addOnCompleteListener(task -> {

            if (task.getResult().getValue() == null) {
                Toast.makeText(app, IdeainatorApplication.getAppContext().getString(R.string.RoomNotFound), Toast.LENGTH_SHORT).show();
                return;
            }
            joinListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    // This will keep the value of game and player updated
                    Game theGame;
                    try{
                        theGame = dataSnapshot.getValue(Game.class);
                    }
                    catch (Error e){
                        Log.e("joinActivity", "onDataChange: error parsing game update - perhaps hash/list error?",e );
                        Toast.makeText(app, "Bad data downloaded :(", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    // This will open the round view when the admin starts the game
                    Game.gameState state = theGame.getState();
                    if (repository.theGame.getValue() == null) {
                        Log.d("TAG", "onDataChange: øv");
                    } else if (repository.theGame.getValue().getState() != Game.gameState.ROUND && state == Game.gameState.ROUND) {
                        Intent roundIntent = new Intent(app, RoundActivity.class);
                        roundIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//Clear top of callstack - so you cant go back
                        app.startActivity(roundIntent);
                    } else if (repository.theGame.getValue().getState() != Game.gameState.FINAL && state == Game.gameState.FINAL) {
                        Intent finalActivityIntent = new Intent(app, FinalActivity.class);
                        finalActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//Clear callstack - so you cant go back
                        app.startActivity(finalActivityIntent);
                    }

                    repository.theGame.setValue(theGame);
                    for (int i = 0; i < repository.theGame.getValue().getPlayers().size(); i++) {
                        if (repository.theGame.getValue().getPlayers().get(i).getName().equals(playerName)) {
                            repository.thePlayer = repository.theGame.getValue().getPlayers().get(i);
                            repository.playerIndex = i;
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("TAG", "Failed to read value.", error.toException());

                }
            };
            joinRef.addValueEventListener(joinListener);

            // Adds player to list of players and opens lobby (only if the game is not started)
            DatabaseReference myRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + joinCode + "/players");
            myRef.get().addOnCompleteListener(task1 -> {
                if (repository.theGame.getValue().getState() == Game.gameState.LOBBY) {
                    GenericTypeIndicator<ArrayList<Player>> t = new GenericTypeIndicator<ArrayList<Player>>() {
                    };
                    ArrayList<Player> players = task1.getResult().getValue(t);

                    players.add(repository.thePlayer);
                    myRef.setValue(players);
                } else {
                    Toast.makeText(app, IdeainatorApplication.getAppContext().getString(R.string.Gamealreadystarted), Toast.LENGTH_SHORT).show();
                }
            });

        });

    }
}
