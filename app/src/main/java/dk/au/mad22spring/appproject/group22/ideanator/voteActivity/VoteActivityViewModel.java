package dk.au.mad22spring.appproject.group22.ideanator.voteActivity;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.List;

import dk.au.mad22spring.appproject.group22.ideanator.IdeainatorApplication;
import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;
import dk.au.mad22spring.appproject.group22.ideanator.model.Round;
import dk.au.mad22spring.appproject.group22.ideanator.roundActivity.OptionAdapter;

public class VoteActivityViewModel extends ViewModel {

    private Repository repository=Repository.getInstance();
    private int round= repository.theGame.getValue().getRoundCounter();


    public List<OptionCard> getVoteOptions() {
        return repository.theGame.getValue().getRounds().get(round).getPlayedOptions();
    }

    public void castVote( int voteIndex) {
        OptionCard optionToVote= repository.theGame.getValue().getRounds().get(round-1).getPlayedOptions().get(voteIndex);

        DatabaseReference roundRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + repository.joinCode+"/rounds/"+(repository.theGame.getValue().getRoundCounter()-1));
        roundRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                GenericTypeIndicator<Round> roundT =new GenericTypeIndicator<Round>() {};
                Round round =task.getResult().getValue(roundT);
                int voteAmount=round.getPlayedOptions().get(voteIndex).getVotes();
                voteAmount++;
                round.getPlayedOptions().get(voteIndex).setVotes(voteAmount);
                roundRef.setValue(round);
            }
        });

        optionToVote.setVotes(optionToVote.getVotes()+1);//Todo Laurits skal godkende at det virker
    }

    public String getProblemText() {
        return repository.theGame.getValue().getRounds().get(round-1).getProblems();
    }

    public void observe(LifecycleOwner owner, OptionAdapter adaptor) {
        repository.theGame.observe(owner, new Observer<Game>() {
            @Override
            public void onChanged(Game game) {
                Log.d("adaptor", "something new");

                adaptor.updateOptionList(game.getRounds().get(round-1).getPlayedOptions());
                Log.d("adaptor", "something new2");

            }
        });
    }
}
