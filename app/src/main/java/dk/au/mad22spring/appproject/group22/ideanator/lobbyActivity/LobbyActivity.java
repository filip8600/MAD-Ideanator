package dk.au.mad22spring.appproject.group22.ideanator.lobbyActivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.mainActivity.MainActivityViewModel;
import dk.au.mad22spring.appproject.group22.ideanator.roundActivity.RoundActivity;

public class LobbyActivity extends AppCompatActivity {
    private Button startButton;
    private ActivityResultLauncher<Intent> launcher;
    private LobbyActivityViewModel viewmodel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        viewmodel = new ViewModelProvider(this).get(LobbyActivityViewModel.class);

        setupUI();
        setupListeners();
        setupLauncher();
    }

    private void setupUI() {
        startButton=findViewById(R.id.LobbyBtnStart);
    }

    private void setupLauncher() {
        //Launcher - Based on code from MAD lecture 2
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

            }
        });
    }
    private void setupListeners() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });

    }

    private void startGame() {
        Intent intent = new Intent(this, RoundActivity.class);
        viewmodel.StartGame(launcher,intent);
        //launcher.launch(intent);

    }

}