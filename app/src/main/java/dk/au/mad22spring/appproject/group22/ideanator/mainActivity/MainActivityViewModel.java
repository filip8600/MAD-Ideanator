package dk.au.mad22spring.appproject.group22.ideanator.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

import dk.au.mad22spring.appproject.group22.ideanator.IdeainatorApplication;
import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.Repository;
import dk.au.mad22spring.appproject.group22.ideanator.finalActivity.FinalActivity;
import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.model.Round;
import dk.au.mad22spring.appproject.group22.ideanator.roundActivity.RoundActivity;

public class MainActivityViewModel extends ViewModel {

    public Repository repository = Repository.getInstance();
    private ValueEventListener gameListener;


    public void CreateGame(ActivityResultLauncher<Intent> launcher, Intent intent, Context app) {
    }

    public void removeListener() {
        if (gameListener != null) {
            DatabaseReference myRef = Repository.getRealtimeInstance().getReference("Ideainator/Games/" + repository.joinCode);
            myRef.removeEventListener(gameListener);
        }
    }
}
