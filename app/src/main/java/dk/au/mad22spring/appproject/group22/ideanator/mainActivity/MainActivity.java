package dk.au.mad22spring.appproject.group22.ideanator.mainActivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dk.au.mad22spring.appproject.group22.ideanator.Firebase;
import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.finalActivity.FinalActivity;
import dk.au.mad22spring.appproject.group22.ideanator.joinActivity.JoinActivity;
import dk.au.mad22spring.appproject.group22.ideanator.lobbyActivity.LobbyActivity;

public class MainActivity extends AppCompatActivity {
    private Button joinButton, createButton, debugFinalShortcut;
    private ActivityResultLauncher<Intent> launcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
        setupListeners();
        setupLauncher();
    }

    private void setupLauncher() {
        //Launcher - Based on code from MAD lecture 2
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

            }
        });
    }


    private void setupUI() {
        joinButton=findViewById(R.id.mainBtnJoin);
        createButton=findViewById(R.id.mainBtnCreate);
        debugFinalShortcut=findViewById(R.id.MainBtnFinal);
    }

    private void setupListeners() {
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGame();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGame();
            }
        });
        debugFinalShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFinal();
            }
        });
    }

    private void goToFinal() {
        Intent intent = new Intent(this, FinalActivity.class);
        launcher.launch(intent);
    }

    private void createGame() {
        Intent intent = new Intent(this, LobbyActivity.class);
        launcher.launch(intent);
    }

    private void joinGame() {
        Intent intent = new Intent(this, JoinActivity.class);
        launcher.launch(intent);
    }
}