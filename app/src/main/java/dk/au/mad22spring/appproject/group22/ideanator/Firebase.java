package dk.au.mad22spring.appproject.group22.ideanator;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Firebase {

    //TODO MAKE THIS SINGLETON
    public Game theGame;

    public Firebase(){

    }

    public static FirebaseDatabase getInstance(){
        return FirebaseDatabase.getInstance();
    }


}
