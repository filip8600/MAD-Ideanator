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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.ForegroundService;
import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.model.Player;
import dk.au.mad22spring.appproject.group22.ideanator.roundActivity.RoundActivity;

public class LobbyActivity extends AppCompatActivity implements LobbyActivityViewModel.canHandleNewPlayers {
    private Button startButton, shareButton;
    private TextView txtJoinCode;
    private PlayerAdapter adapter;
    private RecyclerView rc;


    private ActivityResultLauncher<Intent> launcher;
    private LobbyActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        viewModel = new ViewModelProvider(this).get(LobbyActivityViewModel.class);

        setupRecyclerView();
        setupUI();
        setupListeners();
        setupLauncher();
        startGameService();
    }

    private void startGameService() {
        Intent foregroundServiceIntent = new Intent(this, ForegroundService.class);
        startService(foregroundServiceIntent);
    }

    private void setupRecyclerView() {
        adapter = new PlayerAdapter();
        //adapter.updatePlayerList(viewModel.getPlayerList());
        rc = findViewById(R.id.lobby_recycleView);
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(adapter);
        viewModel.ObservePlayers(this,this);
    }

    private void setupUI() {
        startButton=findViewById(R.id.LobbyBtnStart);
        txtJoinCode=findViewById(R.id.LobbyTxtCode);
        shareButton = findViewById(R.id.lobbyBtnShare);


        txtJoinCode.setText(viewModel.joinCode);


        // Only show button if admin
        if (!viewModel.isAdmin()){
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
        shareButton.setOnClickListener(view -> shareGameCode());


    }

    private void shareGameCode() {//Sharing from https://developer.android.com/training/sharing/send
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.ShareCodeText)+ viewModel.joinCode);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);

    }

    private void startGame() {

        Intent intent = new Intent(this, RoundActivity.class);
        viewModel.StartGame(launcher,intent);
        //launcher.launch(intent);

    }

    @Override
    public void newPlayerArrived(ArrayList<Player> players) {
        adapter.updatePlayerList(players);
    }
}