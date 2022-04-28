package dk.au.mad22spring.appproject.group22.ideanator;

import com.google.firebase.database.FirebaseDatabase;

import dk.au.mad22spring.appproject.group22.ideanator.model.Game;
import dk.au.mad22spring.appproject.group22.ideanator.model.Player;

public class Firebase {

    //TODO MAKE THIS SINGLETON
    public Game theGame;
    public Player thePlayer;
    private FirebaseDatabase Firebase;
    private static Firebase staticFirebase;

    public Firebase(){
        Firebase = FirebaseDatabase.getInstance();
    }

    public static FirebaseDatabase getInstance()
    {
        if (staticFirebase == null) staticFirebase = new Firebase();
        return staticFirebase.Firebase;

    }


}
