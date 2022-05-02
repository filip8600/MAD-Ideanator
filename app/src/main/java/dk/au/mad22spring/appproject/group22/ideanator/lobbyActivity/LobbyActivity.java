package dk.au.mad22spring.appproject.group22.ideanator.lobbyActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.roundActivity.RoundActivity;

public class LobbyActivity extends AppCompatActivity {
    private Button startButton;
    private TextView txtJoinCode, txtPlayers;


    private ActivityResultLauncher<Intent> launcher;
    private LobbyActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        viewModel = new ViewModelProvider(this).get(LobbyActivityViewModel.class);

        setupUI();
        setupListeners();
        setupLauncher();
    }

    private void setupUI() {
        startButton=findViewById(R.id.LobbyBtnStart);
        txtJoinCode=findViewById(R.id.LobbyTxtCode);
        txtPlayers=findViewById(R.id.LobbyTxtPlayes);

        txtJoinCode.setText(viewModel.repository.joinCode);
        viewModel.repository.theGame.observe(this, game -> {
            StringBuilder text = new StringBuilder();
            for (int i = 0; i<game.getPlayers().size(); i++){
                text.append(game.getPlayers().get(i).getName());
                if (viewModel.repository.thePlayer.getName().equals(game.getPlayers().get(i).getName())) text.append(getApplicationContext().getResources().getString(R.string.you));
                text.append("\n");
            }
            txtPlayers.setText(text.toString());
        });


        if (!viewModel.repository.thePlayer.getAdmin()){
            startButton.setEnabled(false);
            startButton.setVisibility(View.GONE);
            findViewById(R.id.LobbyTxtStartHint).setVisibility(View.GONE);
        }
    }

    private void setupLauncher() {
        //Launcher - Based on code from MAD lecture 2
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        });
    }
    private void setupListeners() {
        startButton.setOnClickListener(view -> startGame());

    }

    private void startGame() {

        Intent intent = new Intent(this, RoundActivity.class);
        viewModel.StartGame(launcher,intent);
        //launcher.launch(intent);

    }

}