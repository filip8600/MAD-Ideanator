package dk.au.mad22spring.appproject.group22.ideanator.lobbyActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.ForegroundService;
import dk.au.mad22spring.appproject.group22.ideanator.GameManager;
import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.mainActivity.MainActivity;
import dk.au.mad22spring.appproject.group22.ideanator.model.Player;
import dk.au.mad22spring.appproject.group22.ideanator.roundActivity.RoundActivity;

public class LobbyActivity extends AppCompatActivity implements LobbyActivityViewModel.canHandleGameUpdates {
    private Button startButton, shareButton;
    private TextView txtJoinCode;
    private PlayerAdapter adapter;
    private RecyclerView rc;


    private LobbyActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        viewModel = new ViewModelProvider(this).get(LobbyActivityViewModel.class);

        setupRecyclerView();
        setupUI();
        setupListeners();
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
        viewModel.ObservePlayers(this, this);
    }

    private void setupUI() {
        startButton = findViewById(R.id.LobbyBtnStart);
        txtJoinCode = findViewById(R.id.LobbyTxtCode);
        shareButton = findViewById(R.id.lobbyBtnShare);


        if(viewModel.joinCodeReady) txtJoinCode.setText(viewModel.joinCode);
        else txtJoinCode.setText("Loading...");


        // Only show button if admin
        if(!viewModel.isAdmin()){
            startButton.setEnabled(false);
            startButton.setVisibility(View.GONE);
            findViewById(R.id.LobbyTxtStartHint).setVisibility(View.GONE);
        }

    }

    private void setupListeners() {
        startButton.setOnClickListener(view -> startGame());
        shareButton.setOnClickListener(view -> shareGameCode());

    }

    private void shareGameCode() {//Sharing from https://developer.android.com/training/sharing/send
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.ShareCodeText) + " " + viewModel.joinCode);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);


    }

    private void startGame() {
        viewModel.StartGame(this);
    }

    @Override
    public void newPlayerArrived(ArrayList<Player> players) {
        adapter.updatePlayerList(players);
    }

    @Override
    public void gameCodeReady(String gameCode, boolean isAdmin) {
        txtJoinCode.setText(gameCode);
        // Only show button if admin
        if (isAdmin){
            startButton.setEnabled(true);
            startButton.setVisibility(View.VISIBLE);
            findViewById(R.id.LobbyTxtStartHint).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void goToRoundActivity() {//Only used for admin
        Intent intent = new Intent(this, RoundActivity.class);
        startActivity(intent);
        finish();//Kill lobby activity

    }
    @Override
    public void onBackPressed() {
        GameManager.getInstance().LeaveGame();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void updateAdminButton(boolean isAdmin){
        if (isAdmin){
            startButton.setEnabled(true);
            startButton.setVisibility(View.VISIBLE);
            findViewById(R.id.LobbyTxtStartHint).setVisibility(View.VISIBLE);
        }
        else{
            startButton.setEnabled(false);
            startButton.setVisibility(View.INVISIBLE);
            findViewById(R.id.LobbyTxtStartHint).setVisibility(View.INVISIBLE);
        }
    }
}