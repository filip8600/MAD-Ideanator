package dk.au.mad22spring.appproject.group22.ideanator;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.model.Player;

public class Repository {

    //TODO MAKE THIS SINGLETON
    public MutableLiveData<Game> theGame;
    public Player thePlayer;
    public String joinCode;
    private FirebaseDatabase FirebaseRealtime;
    private FirebaseFirestore FirebaseStatic;
    private static Repository staticFirebase;

    private Repository(){
        FirebaseRealtime = FirebaseDatabase.getInstance();
        FirebaseStatic = FirebaseFirestore.getInstance();
        theGame = new MutableLiveData<>();
    }

    public static Repository getInstance(){
        if (staticFirebase == null) staticFirebase = new Repository();
        return staticFirebase;
    }

    public static FirebaseDatabase getRealtimeInstance()
    {
        if (staticFirebase == null) staticFirebase = new Repository();
        return staticFirebase.FirebaseRealtime;

    }

    public static FirebaseFirestore getStaticInstance()
    {
        if (staticFirebase == null) staticFirebase = new Repository();
        return staticFirebase.FirebaseStatic;

    }


}
