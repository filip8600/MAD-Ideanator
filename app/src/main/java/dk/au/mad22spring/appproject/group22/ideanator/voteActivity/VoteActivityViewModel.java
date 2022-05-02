package dk.au.mad22spring.appproject.group22.ideanator.voteActivity;

import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.IdeainatorApplication;
import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;
import dk.au.mad22spring.appproject.group22.ideanator.roundActivity.RoundActivity;

public class VoteActivityViewModel extends ViewModel {

    private ActivityResultLauncher<Intent> launcher;
    private boolean hasVoted=false;
    private final Repository repository=Repository.getInstance();
    private final int round= repository.theGame.getValue().getRoundCounter();


    public ArrayList<OptionCard> getVoteOptions() {
        return repository.theGame.getValue().getRounds().get(round-1).getPlayedOptions();
    }

    public void castVote( int voteIndex) {
        if(hasVoted) return;
        DatabaseReference roundRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + repository.joinCode+"/rounds/"+(round-1));
        DatabaseReference countRef=roundRef.child("playedOptions").child(String.valueOf(voteIndex)).child("votes");
        countRef.setValue(ServerValue.increment(1));//From https://stackoverflow.com/a/63928484
        hasVoted=true;
    }

    public String getProblemText() {
        return repository.theGame.getValue().getRounds().get(round-1).getProblems();
    }

    public void observe(LifecycleOwner owner, CanUpdateUI caller, ActivityResultLauncher<Intent> launcherRef) {
        launcher=launcherRef;
        repository.theGame.observe(owner, game -> {
            Log.d("adaptor", "something new has happened");
            adminCheckVoteCount(game);
            caller.updateView(countNumberOfVotes(),getVoteOptions());


        });
    }

    public void adminCheckVoteCount(Game game) {
       if ( !repository.thePlayer.getAdmin()) return;//Only game admin will check if everyone has voted
       //else
        int counter = countNumberOfVotes();
        int numberOfPlayers=game.getPlayers().size();
        if(numberOfPlayers<=counter) moveToNextRound();

    }

    private int countNumberOfVotes() {
        int counter=0;
        for (OptionCard optionCard:getVoteOptions()) {
            counter+=optionCard.getVotes();
        }
        return counter;
    }

    private void moveToNextRound() {
        DatabaseReference gameRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + repository.joinCode);
        gameRef.get().addOnCompleteListener(task -> {
            GenericTypeIndicator<Game> gameT =new GenericTypeIndicator<Game>() {};
            Game game =task.getResult().getValue(gameT);
            OptionCard winner= determineWinner(game);
            game.getRounds().get(round-1).setWinner(winner);
            game.setState(Game.gameState.ROUND);
            if(round>=10) {//End game
                game.setState(Game.gameState.FINAL);
                gameRef.setValue(game);
            }
            else {//continue game
                game.setRoundCounter(round+1);
                gameRef.setValue(game);
                Intent intent = new Intent(IdeainatorApplication.getAppContext(), RoundActivity.class);
                launcher.launch(intent);
            }
        });

    }

    ///Look through all played option, and return first card with highest number of votes
    private OptionCard determineWinner(Game game) {
        OptionCard currentLeader=game.getRounds().get(round-1).getPlayedOptions().get(0);
        for (OptionCard optionCard:game.getRounds().get(round-1).getPlayedOptions()) {
            if(currentLeader.getVotes()<optionCard.getVotes()) currentLeader=optionCard;
        }
        return currentLeader;
    }

    public int getNumberOfPlayers() {
        return repository.theGame.getValue().getPlayers().size();
    }

    public interface CanUpdateUI{
        void updateView(int numberOfPlayers, ArrayList<OptionCard> voteOptions);
    }
}
