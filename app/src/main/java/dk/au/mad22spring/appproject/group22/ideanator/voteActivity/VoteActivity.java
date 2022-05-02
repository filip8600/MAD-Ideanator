package dk.au.mad22spring.appproject.group22.ideanator.voteActivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;
import dk.au.mad22spring.appproject.group22.ideanator.roundActivity.OptionAdapter;

public class VoteActivity extends AppCompatActivity implements OptionAdapter.IOptionItemClickedListener, VoteActivityViewModel.CanUpdateUI {
    private VoteActivityViewModel vm;
    private OptionAdapter adapter;
    private RecyclerView rc;
    private ActivityResultLauncher<Intent> launcher;
    private TextView problemText;
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
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

            }
        });
    }

    private void setupUI() {
        problemText=findViewById(R.id.vote_txt_problem);
        problemText.setText(vm.getProblemText());
        progressBar=findViewById(R.id.vote_progressBar_countdown);
        progressBar.setMax(vm.getNumberOfPlayers());
    }

    private void setupRecyclerView() {
        adapter= new OptionAdapter(this);
        //adapter.updateOptionList(vm.getVoteOptions());
        rc=findViewById(R.id.vote_recyclerView);
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(adapter);
        //observe on game
        vm.observe(this, this);
        Log.d("adaptor", "recycler set up");

    }

    @Override
    public void onOptionClicked(int index) {
        vm.castVote(index);
        Toast.makeText(this, getString(R.string.voteSaved), Toast.LENGTH_SHORT).show();

    }
    public void updateView(int numberOfCastVotes, ArrayList<OptionCard> voteOptions){
        progressBar.setProgress(numberOfCastVotes);
        adapter.updateOptionList(voteOptions);

    }

}
