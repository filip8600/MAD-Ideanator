package dk.au.mad22spring.appproject.group22.ideanator.lobbyActivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.roundActivity.RoundActivity;

public class LobbyActivity extends AppCompatActivity {
    private Button startButton;
    private TextView txtJoinCode, txtPlayers;


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
        txtJoinCode=findViewById(R.id.LobbyTxtCode);
        txtPlayers=findViewById(R.id.LobbyTxtPlayes);

        txtJoinCode.setText(viewmodel.repository.joinCode);
        viewmodel.repository.theGame.observe(this, new Observer<Game>() {
            @Override
            public void onChanged(Game game) {
                String text = "";
                for (Integer i = 0;i<game.getPlayers().size();i++){
                    text += game.getPlayers().get(i).getName();
                    if (viewmodel.repository.thePlayer.getName() == game.getPlayers().get(i).getName()) text +="*";
                    text += "\n";
                }
                txtPlayers.setText(text);
            }
        });

        if (viewmodel.repository.thePlayer.getAdmin() == false){
            startButton.setEnabled(false);
            startButton.setVisibility(View.GONE);
            findViewById(R.id.LobbyTxtStartHint).setVisibility(View.GONE);
        }
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