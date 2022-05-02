package dk.au.mad22spring.appproject.group22.ideanator.joinActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.lobbyActivity.LobbyActivity;

public class JoinActivity extends AppCompatActivity {
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
        //Todo: Check validity
        Intent intent = new Intent(this, LobbyActivity.class);
        intent.putExtra(getString(R.string.joincode),textBox.getText());

        viewModel.JoinGame(textBox.getText().toString(),this,intent,launcher);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.removeListener();
    }
}