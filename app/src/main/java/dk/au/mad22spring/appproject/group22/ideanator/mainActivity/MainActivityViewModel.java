package dk.au.mad22spring.appproject.group22.ideanator.mainActivity;

import android.widget.Toast;

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

import dk.au.mad22spring.appproject.group22.ideanator.Firebase;
import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;
import dk.au.mad22spring.appproject.group22.ideanator.model.Player;
import dk.au.mad22spring.appproject.group22.ideanator.model.Round;

public class MainActivityViewModel extends ViewModel {

    Firebase firebase = new Firebase();

    public void CreateGame(){
        Game theGame = new Game();

        ArrayList<Round> rounds = new ArrayList<>();
        rounds.add(new Round("This be a question 1"));
        rounds.add(new Round("This be a question 2"));
        rounds.add(new Round("This be a question 3"));

  //      theGame.setProblems(problems);
  //      theGame.setOptions(Options);

        Player player = new Player();
        player.setName("TheAdmin");
        player.setAdmin(true);

        theGame.getPlayers().add(player);

        DatabaseReference myRef = Firebase.getInstance().getReference("Ideainator/Games/"+"321");
        myRef.setValue(theGame);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firebase.theGame = snapshot.getValue(Game.class);
                for (Integer i= 0; i < firebase.theGame.getPlayers().size();i++){
                    if (firebase.theGame.getPlayers().get(i).getName() == player.getName()){
                        firebase.thePlayer = firebase.theGame.getPlayers().get(i);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
