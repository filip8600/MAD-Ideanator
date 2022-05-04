package dk.au.mad22spring.appproject.group22.ideanator.joinActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import dk.au.mad22spring.appproject.group22.ideanator.GameManager;
import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.lobbyActivity.LobbyActivity;

public class JoinActivity extends AppCompatActivity implements JoinActivityViewModel.canHandleResult {
    private Button joinButton;
    private EditText textBox;
    private JoinActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        setupUI();
        setupListeners();
        viewModel = new ViewModelProvider(this).get(JoinActivityViewModel.class);
    }


    private void setupUI() {
        joinButton = findViewById(R.id.JoinBtnStart);
        textBox = findViewById(R.id.JoinInputField);
    }

    private void setupListeners() {
        joinButton.setOnClickListener(view -> joinGame());

    }

    private void joinGame() {
        viewModel.isCodeValid(this, String.valueOf(textBox.getText()));
    }

    public void isCodeValidResult(boolean isValid, String errorMessage) {
        if (isValid) {
            GameManager.getInstance().joinExistingGame(String.valueOf(textBox.getText()));
            Intent intent = new Intent(this, LobbyActivity.class);
            startActivity(intent);
        } else {
            if (errorMessage == null) errorMessage = getString(R.string.RoomNotFound);
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

}