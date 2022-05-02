package dk.au.mad22spring.appproject.group22.ideanator.lobbyActivity;

import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;

import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;
import dk.au.mad22spring.appproject.group22.ideanator.model.Player;

public class LobbyActivityViewModel extends ViewModel {

    Repository repository = Repository.getInstance();

    public void StartGame(ActivityResultLauncher<Intent> launcher, Intent intent){
        DatabaseReference myRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/"+ repository.joinCode);

        myRef.get().addOnCompleteListener(task -> {
            GenericTypeIndicator<Game> t = new GenericTypeIndicator<Game>() {};
            Game theGame = task.getResult().getValue(t);
            Repository.getStaticInstance().collection("OptionCards").get().addOnCompleteListener(task1 -> {
                ArrayList<OptionCard> optionCards = new ArrayList<>();

                for (QueryDocumentSnapshot document : task1.getResult()){
                    OptionCard card = new OptionCard(document.get("English",String.class));
                    optionCards.add(card);
                }


                if (theGame.getState() == Game.gameState.LOBBY) {
                    ArrayList<Player> players = theGame.getPlayers();
                    for (int i = 0; i<players.size(); i++)
                    {
                        // https://www.geeksforgeeks.org/shuffle-elements-of-arraylist-in-java/
                        Collections.shuffle(optionCards);
                        ArrayList<OptionCard> options = new ArrayList<>();
                        for (int j = 0;j <15;j++){
                            options.add(optionCards.get(j));
                        }
                        players.get(i).setOptions(options);
                    }
                    theGame.setPlayers(players);
                    theGame.setState(Game.gameState.ROUND);
                    myRef.setValue(theGame);
                    myRef.get().addOnCompleteListener(task11 -> {
                        launcher.launch(intent);
                        Log.d("StartOfGame",Integer.toString(repository.thePlayer.getOptions().size()));
                    });

                    Log.d("Lobby","Given players their cards");
                    //JoinGame.setValue(true);
                }

            });
        });

    }
}
