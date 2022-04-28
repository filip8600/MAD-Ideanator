package dk.au.mad22spring.appproject.group22.ideanator.roundActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;

import dk.au.mad22spring.appproject.group22.ideanator.R;

//https://www.journaldev.com/9896/android-countdowntimer-example TODO: Count down bar on round view + vote view

public class RoundActivity extends AppCompatActivity {

    private RoundActivityViewModel vm;
    private OptionAdapter adapter;

    private TextView userName,roundNumber,problemText;
    private RecyclerView rc;
    private ActivityResultLauncher<Intent> launcher;
    private ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);

        setupUI();

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
}