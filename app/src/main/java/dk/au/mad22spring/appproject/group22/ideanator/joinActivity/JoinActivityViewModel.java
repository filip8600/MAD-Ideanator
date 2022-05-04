package dk.au.mad22spring.appproject.group22.ideanator.joinActivity;

import android.content.Context;
import android.content.Intent;
import android.content.RestrictionEntry;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.IdeainatorApplication;
import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.finalActivity.FinalActivity;
import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.model.Player;
import dk.au.mad22spring.appproject.group22.ideanator.roundActivity.RoundActivity;

public class JoinActivityViewModel extends ViewModel {

    Repository repository = Repository.getInstance();
    ValueEventListener listener;
    DatabaseReference myRef;

    public void JoinGame(String joinCode, Context app, Intent intent, ActivityResultLauncher<Intent> launcher) {
        String playerName = Repository.getInstance().thePlayer.getName();
        repository.thePlayer.setAdmin(false);

        myRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + joinCode);

        myRef.get().addOnCompleteListener(task -> {

            if (task.getResult().getValue() == null) {
                Toast.makeText(app, IdeainatorApplication.getAppContext().getString(R.string.RoomNotFound), Toast.LENGTH_SHORT).show();
                return;
            }
            listener = new ValueEventListener() {
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
                        Intent intent1 = new Intent(app, RoundActivity.class);
                        launcher.launch(intent1);
                    } else if (repository.theGame.getValue().getState() != Game.gameState.FINAL && state == Game.gameState.FINAL) {
                        Intent intent1 = new Intent(app, FinalActivity.class);
                        launcher.launch(intent1);
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
            myRef.addValueEventListener(listener);

            // Adds player to list of players and opens lobby (only if the game is not started)
            DatabaseReference myRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + joinCode + "/players");
            myRef.get().addOnCompleteListener(task1 -> {
                if (repository.theGame.getValue().getState() == Game.gameState.LOBBY) {
                    GenericTypeIndicator<ArrayList<Player>> t = new GenericTypeIndicator<ArrayList<Player>>() {
                    };
                    ArrayList<Player> players = task1.getResult().getValue(t);

                    players.add(repository.thePlayer);
                    repository.joinCode = joinCode;
                    myRef.setValue(players);
                    launcher.launch(intent);
                } else {
                    Toast.makeText(app, IdeainatorApplication.getAppContext().getString(R.string.Gamealreadystarted), Toast.LENGTH_SHORT).show();
                }
            });

        });


    }

    // Remove database listeners
    public void removeListener() {
        if (listener != null) myRef.removeEventListener(listener);
    }

}
