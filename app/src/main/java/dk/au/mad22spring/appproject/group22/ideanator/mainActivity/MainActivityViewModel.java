package dk.au.mad22spring.appproject.group22.ideanator.mainActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.model.Player;
import dk.au.mad22spring.appproject.group22.ideanator.model.Round;

public class MainActivityViewModel extends ViewModel {

    public Repository repository = Repository.getInstance();
    private ValueEventListener listener;

    public void CreateGame(){
        Game theGame = new Game();

        ArrayList<Round> rounds = new ArrayList<>();
        rounds.add(new Round("This be a question 1"));
        rounds.add(new Round("This be a question 2"));
        rounds.add(new Round("This be a question 3"));

        theGame.setRounds(rounds);
  //      theGame.setProblems(problems);
  //      theGame.setOptions(Options);

        Player player = new Player();
        player.setName("TheAdmin");
        player.setAdmin(true);
        repository.thePlayer = player;

        theGame.getPlayers().add(player);

        // Generate joinCode here
        repository.joinCode = "321";

        DatabaseReference myRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/"+ repository.joinCode);
        myRef.setValue(theGame);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                repository.theGame.setValue(snapshot.getValue(Game.class));
                for (Integer i = 0; i < repository.theGame.getValue().getPlayers().size(); i++){
                    if (repository.theGame.getValue().getPlayers().get(i).getName() == player.getName()){
                        repository.thePlayer = repository.theGame.getValue().getPlayers().get(i);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        myRef.addValueEventListener(listener);

    }
    public void removeListener(){
       if (listener != null) {
           DatabaseReference myRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + repository.joinCode);
           myRef.removeEventListener(listener);
       }
       }
}
