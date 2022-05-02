package dk.au.mad22spring.appproject.group22.ideanator.mainActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.finalActivity.FinalActivity;
import dk.au.mad22spring.appproject.group22.ideanator.joinActivity.JoinActivity;
import dk.au.mad22spring.appproject.group22.ideanator.lobbyActivity.LobbyActivity;
import dk.au.mad22spring.appproject.group22.ideanator.model.Player;

public class MainActivity extends AppCompatActivity {
    private Button joinButton, createButton, debugFinalShortcut;
    private ActivityResultLauncher<Intent> launcher;
    private MainActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.repository.thePlayer = new Player(true);
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
        joinButton=findViewById(R.id.mainBtnJoin);
        createButton=findViewById(R.id.mainBtnCreate);
        debugFinalShortcut=findViewById(R.id.MainBtnFinal);
    }

    private void setupListeners() {
        joinButton.setOnClickListener(view -> joinGame());
        createButton.setOnClickListener(view -> createGame());
        debugFinalShortcut.setOnClickListener(view -> goToFinal());

        viewModel.removeListener();

    }

    private void goToFinal() {
        Intent intent = new Intent(this, FinalActivity.class);
        launcher.launch(intent);
    }

    private void createGame() {
        Intent intent = new Intent(this, LobbyActivity.class);
        viewModel.CreateGame(launcher, intent,this);

    }

    private void joinGame() {
        Intent intent = new Intent(this, JoinActivity.class);
        launcher.launch(intent);
    }
}