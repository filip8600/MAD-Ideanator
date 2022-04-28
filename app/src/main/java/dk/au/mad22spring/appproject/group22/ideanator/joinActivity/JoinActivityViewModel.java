package dk.au.mad22spring.appproject.group22.ideanator.joinActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.model.Player;
import dk.au.mad22spring.appproject.group22.ideanator.roundActivity.RoundActivity;
import dk.au.mad22spring.appproject.group22.ideanator.voteActivity.VoteActivity;

public class JoinActivityViewModel extends ViewModel {

    Repository repository = Repository.getInstance();
    ValueEventListener listener;
    DatabaseReference myRef;
    //public MutableLiveData<Boolean> JoinGame = new MutableLiveData<>();
    //private ActivityResultLauncher<Intent> launcher;


    public void JoinGame(String joinCode, Context app,Intent intent, ActivityResultLauncher<Intent> launcher,String playerName){

        myRef = repository.getRealtimeInstance().getReference("Ideainator/Games/"+joinCode);

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.getResult().getValue() != null) {
                    listener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            repository.theGame.setValue(dataSnapshot.getValue(Game.class));
                            for (Integer i = 0; i < repository.theGame.getValue().getPlayers().size(); i++){
                                if (repository.theGame.getValue().getPlayers().get(i).getName().equals(playerName)){
                                    repository.thePlayer = repository.theGame.getValue().getPlayers().get(i);
                                    repository.playerIndex = i;
                                }
                            }
                            if (repository.thePlayer.getAdmin() == false && repository.theGame.getValue().getState() == Game.gameState.ROUND){
                                Intent intent = new Intent(app, RoundActivity.class);
                                launcher.launch(intent);
                            }
                            else if (repository.thePlayer.getAdmin() == false && repository.theGame.getValue().getState() == Game.gameState.VOTE){
                                Intent intent = new Intent(app, VoteActivity.class);
                                launcher.launch(intent);
                            }
                            Log.d("GAME", Integer.toString(repository.theGame.getValue().getPlayers().size()));

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            // Log.w("TAG", "Failed to read value.", error.toException());

                        }
                    };
                    myRef.addValueEventListener(listener);
                    DatabaseReference myRef = repository.getRealtimeInstance().getReference("Ideainator/Games/" + joinCode + "/players");

                    myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (repository.theGame.getValue().getState() == Game.gameState.LOBBY) {
                                GenericTypeIndicator<ArrayList<Player>> t = new GenericTypeIndicator<ArrayList<Player>>() {
                                };
                                ArrayList<Player> players = task.getResult().getValue(t);
                                Player player = new Player();
                                player.setName(playerName);
                                players.add(player);
                                repository.thePlayer = player;
                                repository.joinCode = joinCode;
                                myRef.setValue(players);
                                launcher.launch(intent);
                                //JoinGame.setValue(true);
                            }
                            else {
                                Toast.makeText(app, "Game already started", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(app, "No such room", Toast.LENGTH_SHORT).show();
                    //JoinGame.setValue(false);
                }
            }
        });



    }

    public void removeListener(){
        if (listener != null) myRef.removeEventListener(listener);
    }

}
