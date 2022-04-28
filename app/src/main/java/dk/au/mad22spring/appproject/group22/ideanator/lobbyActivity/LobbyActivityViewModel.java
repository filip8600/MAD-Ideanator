package dk.au.mad22spring.appproject.group22.ideanator.lobbyActivity;

import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;
import dk.au.mad22spring.appproject.group22.ideanator.model.Player;

public class LobbyActivityViewModel extends ViewModel {

    Repository repository = Repository.getInstance();

    public void StartGame(ActivityResultLauncher<Intent> launcher, Intent intent){
        DatabaseReference myRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/"+ repository.joinCode);

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                GenericTypeIndicator<Game> t = new GenericTypeIndicator<Game>() {};
                Game theGame = task.getResult().getValue(t);

                if (theGame.getState() == Game.gameState.LOBBY) {
                   ArrayList<Player> players = theGame.getPlayers();
                   for (Integer i = 0;i<players.size();i++)
                   {
                        players.get(i).getOptions().add(new OptionCard("this be an option 1"));
                        players.get(i).getOptions().add(new OptionCard("this be an option 2"));
                        players.get(i).getOptions().add(new OptionCard("this be an option 3"));
                   }
                   theGame.setPlayers(players);
                   theGame.setState(Game.gameState.ROUND);
                    myRef.setValue(theGame);
                    launcher.launch(intent);
                    Log.d("Lobby","Given players their cards");
                    //JoinGame.setValue(true);
                }
            }
        });

    }
}
