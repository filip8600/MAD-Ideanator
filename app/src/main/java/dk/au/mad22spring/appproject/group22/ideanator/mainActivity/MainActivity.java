package dk.au.mad22spring.appproject.group22.ideanator.mainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.joinActivity.JoinActivity;
import dk.au.mad22spring.appproject.group22.ideanator.lobbyActivity.LobbyActivity;

public class MainActivity extends AppCompatActivity {
    private Button joinButton, createButton;
    private ActivityResultLauncher<Intent> launcher;
    private MainActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.createPlayer();
        setupUI();
        setupListeners();
        setupLauncher();
    }

    private void setupLauncher() {
        //Launcher - Based on code from MAD lecture 2
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        });
    }


    private void setupUI() {
        joinButton = findViewById(R.id.mainBtnJoin);
        createButton = findViewById(R.id.mainBtnCreate);
    }

    private void setupListeners() {
        joinButton.setOnClickListener(view -> joinGame());
        createButton.setOnClickListener(view -> createGame());

    }


    private void createGame() {
        Intent intent = new Intent(this, LobbyActivity.class);
        launcher.launch(intent);
        viewModel.CreateGame();

    }

    private void joinGame() {
        Intent intent = new Intent(this, JoinActivity.class);
        launcher.launch(intent);
    }
}