package dk.au.mad22spring.appproject.group22.ideanator.voteActivity;

import androidx.lifecycle.ViewModel;

import java.util.List;

import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;

public class VoteActivityViewModel extends ViewModel {

    private Repository repository=Repository.getInstance();

    public List<OptionCard> getVoteOptions(int roundNumber) {
        return repository.theGame.getValue().getRounds().get(roundNumber).getPlayedOptions();
    }

    public void castVote(int roundNumber, int voteIndex) {
        OptionCard optionToVote= repository.theGame.getValue().getRounds().get(roundNumber).getPlayedOptions().get(voteIndex);
        optionToVote.setVotes(optionToVote.getVotes()+1);//Todo Laurits skal godkende at det virker
    }
}
