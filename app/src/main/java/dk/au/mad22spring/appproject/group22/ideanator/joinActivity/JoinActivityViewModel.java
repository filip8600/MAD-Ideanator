package dk.au.mad22spring.appproject.group22.ideanator.joinActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.Firebase;
import dk.au.mad22spring.appproject.group22.ideanator.Game;
import dk.au.mad22spring.appproject.group22.ideanator.Player;

public class JoinActivityViewModel extends ViewModel {

    Firebase firebase = new Firebase();
    //public MutableLiveData<Boolean> JoinGame = new MutableLiveData<>();
    //private ActivityResultLauncher<Intent> launcher;


    public void JoinGame(String roomid, Context app,Intent intent, ActivityResultLauncher<Intent> launcher){

        DatabaseReference myRef = firebase.getInstance().getReference("Ideainator/Games/"+roomid);

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.getResult().getValue() != null) {
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            firebase.theGame = dataSnapshot.getValue(Game.class);
                            Log.d("GAME", Integer.toString(firebase.theGame.getPlayers().size()));

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            // Log.w("TAG", "Failed to read value.", error.toException());

                        }
                    });
                    DatabaseReference myRef = firebase.getInstance().getReference("Ideainator/Games/" + roomid + "/players");

                    myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (firebase.theGame.getState() == Game.gameState.LOBBY) {
                                GenericTypeIndicator<ArrayList<Player>> t = new GenericTypeIndicator<ArrayList<Player>>() {
                                };
                                ArrayList<Player> players = task.getResult().getValue(t);
                                Player player = new Player();
                                player.setName("Peter");
                                players.add(player);
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


}
