package dk.au.mad22spring.appproject.group22.ideanator.voteActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.roundActivity.OptionAdapter;
import dk.au.mad22spring.appproject.group22.ideanator.roundActivity.RoundActivityViewModel;

public class VoteActivity extends AppCompatActivity implements OptionAdapter.IOptionItemClickedListener{
    private VoteActivityViewModel vm;
    private OptionAdapter adapter;
    private RecyclerView rc;
    private ActivityResultLauncher<Intent> launcher;
    private TextView problemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        vm = new ViewModelProvider(this).get(VoteActivityViewModel.class);

        setupRecyclerView();
        setupUI();
    }

    private void setupUI() {
        problemText=findViewById(R.id.vote_txt_problem);
    }

    private void setupRecyclerView() {
        adapter= new OptionAdapter(this);
        adapter.updateOptionList(vm.getVoteOptions(1));//Todo get round number instead of 1
        rc=findViewById(R.id.vote_recyclerView);
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(adapter);
    }

    @Override
    public void onOptionClicked(int index) {
        Toast.makeText(this, "Din stemme er (ikke) gemt!", Toast.LENGTH_SHORT).show();
        vm.castVote(1,index);//Todo get round number instead of 1
    }
}