package dk.au.mad22spring.appproject.group22.ideanator.mainActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.Firebase;
import dk.au.mad22spring.appproject.group22.ideanator.Game;
import dk.au.mad22spring.appproject.group22.ideanator.Player;

public class MainActivityViewModel extends ViewModel {

    Firebase firebase = new Firebase();

    public void CreateGame(){
        Game theGame = new Game();

        ArrayList<String> problems = new ArrayList<>();
        problems.add("This be a question 1");
        problems.add("This be a question 2");
        problems.add("This be a question 3");

        ArrayList<String> Options = new ArrayList<>();
        Options.add("This be a option 1");
        Options.add("This be a Option 2");
        Options.add("This be a Option 3");

        theGame.setProblems(problems);
        theGame.setOptions(Options);

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
