package dk.au.mad22spring.appproject.group22.ideanator.roundActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

import dk.au.mad22spring.appproject.group22.ideanator.R;

//https://www.journaldev.com/9896/android-countdowntimer-example TODO: Count down bar on round view + vote view

public class RoundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);


    }
}