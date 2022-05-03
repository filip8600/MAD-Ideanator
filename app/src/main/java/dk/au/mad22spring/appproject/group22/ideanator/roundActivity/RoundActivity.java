package dk.au.mad22spring.appproject.group22.ideanator.roundActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.IdeainatorApplication;
import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;
import dk.au.mad22spring.appproject.group22.ideanator.model.Round;
import dk.au.mad22spring.appproject.group22.ideanator.voteActivity.VoteActivity;

//https://www.journaldev.com/9896/android-countdowntimer-example TODO: Count down bar on round view + vote view

public class RoundActivity extends AppCompatActivity implements OptionAdapter.IOptionItemClickedListener, RoundActivityViewModel.CanHandleOptionsUpdate {

    private RoundActivityViewModel vm;
    private OptionAdapter adapter;

    private TextView userName, roundNumber, problemText;
    private RecyclerView rc;
    private ActivityResultLauncher<Intent> launcher;
    private ImageView userImage;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        vm = new ViewModelProvider(this).get(RoundActivityViewModel.class);

        setupRecyclerView();
        setupUI();
        setupLauncher();

    }

    private void setupLauncher() {
        //Launcher - Based on code from MAD lecture 2
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        });
    }

    private void setupRecyclerView() {
        adapter = new OptionAdapter(this);
        adapter.updateOptionList(vm.getOptionCards());
        rc = findViewById(R.id.round_recyclerView);
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(adapter);



    }

    private void setupUI() {
        //USER
        userName = findViewById(R.id.round_txt_username);
        userImage = findViewById(R.id.round_img_avatar);

        userName.setText(vm.getUserName());
        Glide.with(getApplicationContext())
                .load((vm.getImageUrl()))
                .placeholder(R.mipmap.ic_smiley_round)
                .into(userImage);

        //Round-specifics
        roundNumber = findViewById(R.id.round_txt_number);
        problemText = findViewById(R.id.round_txt_problem);
        progressBar=findViewById(R.id.round_progressBar_countdown);

        roundNumber.setText(Integer.toString(vm.getRoundNumber()));
        problemText.setText(vm.getProblem());
        progressBar.setMax(vm.getNumberOfPlayers());
        vm.observeOnNumberOfOptionsSent(this,this);

    }

    @Override
    public void onOptionClicked(int index) {
        if (!vm.hasChosenOption) {
            vm.hasChosenOption = true;

            vm.sendSelectedOption(index,this);




        } else {
            Toast.makeText(this, getString( R.string.optionAllreadySelected), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void newOptionPlayed(int numberOfOptionsPlayed) {
        if(vm.hasChosenOption) return;
        progressBar.setProgress(numberOfOptionsPlayed);
    }
    @Override
    public void startIntent(){
        Intent intent = new Intent(IdeainatorApplication.getAppContext(),VoteActivity.class);
        startActivity(intent);
    }
}