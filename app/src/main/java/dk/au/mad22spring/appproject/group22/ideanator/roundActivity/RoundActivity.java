package dk.au.mad22spring.appproject.group22.ideanator.roundActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.joinActivity.JoinActivityViewModel;
import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;
import dk.au.mad22spring.appproject.group22.ideanator.model.Player;
import dk.au.mad22spring.appproject.group22.ideanator.model.Round;

//https://www.journaldev.com/9896/android-countdowntimer-example TODO: Count down bar on round view + vote view

public class RoundActivity extends AppCompatActivity implements OptionAdapter.IOptionItemClickedListener {

    private RoundActivityViewModel vm;
    private OptionAdapter adapter;

    private TextView userName, roundNumber, problemText;
    private RecyclerView rc;
    private ActivityResultLauncher<Intent> launcher;
    private ImageView userImage;
    private boolean hasChosenOption = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        vm = new ViewModelProvider(this).get(RoundActivityViewModel.class);

        setupRecyclerView();
        setupUI();

    }

    private void setupRecyclerView() {
        adapter = new OptionAdapter(this);
        adapter.updateOptionList(vm.getOptionCards());
        rc = findViewById(R.id.round_recyclerView);
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(adapter);

        vm.repository.theGame.observe(this, new Observer<Game>() {
            @Override
            public void onChanged(Game game) {
                adapter.updateOptionList(vm.repository.thePlayer.getOptions());
                userName.setText(vm.getUserName());
                Glide.with(getApplicationContext())
                        .load((vm.getImageUrl()))
                        .placeholder(R.mipmap.ic_smiley_round)
                        .into(userImage);
            }
        });

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

        roundNumber.setText(vm.getRoundNumber());
        problemText.setText(vm.getProblem());
    }

    @Override
    public void onOptionClicked(int index) {
        if (hasChosenOption == false) {
            Toast.makeText(this, "You chose" + vm.repository.thePlayer.getOptions().get(index).getOption(), Toast.LENGTH_SHORT).show();
            hasChosenOption = true;

            DatabaseReference dataRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + vm.repository.joinCode);

            dataRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    GenericTypeIndicator<Game> t = new GenericTypeIndicator<Game>() {
                    };
                    Game theGame = task.getResult().getValue(t);
                    Round theRound = theGame.getRounds().get((theGame.getRoundCounter() - 1));
                    ArrayList<OptionCard> optionlist = new ArrayList<>();
                    if (theRound.getPlayedOptions() != null)
                        optionlist = theRound.getPlayedOptions();
                    optionlist.add(vm.repository.thePlayer.getOptions().get(index));
                    theRound.setPlayedOptions(optionlist);
                    theGame.getPlayers().get(vm.repository.playerIndex).getOptions().remove(index);
                    dataRef.setValue(theGame);


                }
            });


        } else {
            Toast.makeText(this, "Slow down bucky, you have already chosen an option", Toast.LENGTH_SHORT).show();//Todo translate or delete
        }

    }
}