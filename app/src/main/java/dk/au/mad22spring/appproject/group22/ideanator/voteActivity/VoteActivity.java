package dk.au.mad22spring.appproject.group22.ideanator.voteActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;
import dk.au.mad22spring.appproject.group22.ideanator.roundActivity.OptionAdapter;

public class VoteActivity extends AppCompatActivity implements OptionAdapter.IOptionItemClickedListener, VoteActivityViewModel.CanUpdateUI {
    private VoteActivityViewModel vm;
    private OptionAdapter adapter;
    private ActivityResultLauncher<Intent> launcher;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        vm = new ViewModelProvider(this).get(VoteActivityViewModel.class);
        setupLauncher();
        setupRecyclerView();
        setupUI();

    }

    private void setupLauncher() {
        //Launcher - Based on code from MAD lecture 2
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        });
    }

    private void setupUI() {
        TextView problemText = findViewById(R.id.vote_txt_problem);
        problemText.setText(vm.getProblemText());
        progressBar=findViewById(R.id.vote_progressBar_countdown);
        progressBar.setMax(vm.getNumberOfPlayers());
    }

    private void setupRecyclerView() {
        adapter= new OptionAdapter(this);
        //adapter.updateOptionList(vm.getVoteOptions());
        RecyclerView rc = findViewById(R.id.vote_recyclerView);
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(adapter);
        //observe on game
        vm.observe(this, this,launcher);
        Log.d("adaptor", "recycler set up");

    }

    @Override
    public void onOptionClicked(int index) {
        if (vm.hasVoted) Toast.makeText(this, getString(R.string.optionAllreadySelected), Toast.LENGTH_SHORT).show();
        else {
            vm.castVote(index);
            Toast.makeText(this, getString(R.string.voteSaved), Toast.LENGTH_SHORT).show();
        }
    }
    public void updateView(int numberOfCastVotes, ArrayList<OptionCard> voteOptions){
        progressBar.setProgress(numberOfCastVotes);
        adapter.updateOptionList(voteOptions);

    }

}
