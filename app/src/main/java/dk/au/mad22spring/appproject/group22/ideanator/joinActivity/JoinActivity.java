package dk.au.mad22spring.appproject.group22.ideanator.joinActivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.lobbyActivity.LobbyActivity;

public class JoinActivity extends AppCompatActivity {
    private Button joinButton;
    private EditText textBox;
    private ActivityResultLauncher<Intent> launcher;
    private JoinActivityViewModel viewmodel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        setupUI();
        setupListeners();
        setupLauncher();

        viewmodel = new ViewModelProvider(this).get(JoinActivityViewModel.class);


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
        joinButton=findViewById(R.id.JoinBtnStart);
        textBox=findViewById(R.id.JoinInputField);
    }

    private void setupListeners() {
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGame();
            }
        });

    }

    private void joinGame() {
        //Todo: Check validity
        Intent intent = new Intent(this, LobbyActivity.class);
        intent.putExtra(getString(R.string.joincode),textBox.getText());

        viewmodel.JoinGame(textBox.getText().toString(),this,intent,launcher);

        /*viewmodel.JoinGame.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == true){
                    launcher.launch(intent);
                    viewmodel.JoinGame.removeObserver(this);
                }
            }
        });*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewmodel.removeListener();
    }
}