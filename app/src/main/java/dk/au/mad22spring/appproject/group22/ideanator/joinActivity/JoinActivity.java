package dk.au.mad22spring.appproject.group22.ideanator.joinActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import dk.au.mad22spring.appproject.group22.ideanator.GameManager;
import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.lobbyActivity.LobbyActivity;

public class JoinActivity extends AppCompatActivity implements JoinActivityViewModel.canHandleResult {
    private Button joinButton;
    private EditText textBox;
    private ActivityResultLauncher<Intent> launcher;
    private JoinActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        setupUI();
        setupListeners();
        setupLauncher();

        viewModel = new ViewModelProvider(this).get(JoinActivityViewModel.class);


    }

    private void setupLauncher() {
        //Launcher - Based on code from MAD lecture 2
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        });
    }


    private void setupUI() {
        joinButton=findViewById(R.id.JoinBtnStart);
        textBox=findViewById(R.id.JoinInputField);
    }

    private void setupListeners() {
        joinButton.setOnClickListener(view -> joinGame());

    }

    private void joinGame() {
        viewModel.isCodeValid(this,String.valueOf(textBox.getText()));
    }
    public void isCodeValidResult(boolean isValid){
        if(isValid) {
            GameManager.getInstance().joinExistingGame(String.valueOf(textBox.getText()));
            Intent intent = new Intent(this, LobbyActivity.class);
            startActivity(intent);
        }
        else Toast.makeText(this, getString(R.string.RoomNotFound), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.removeListener();
    }
}