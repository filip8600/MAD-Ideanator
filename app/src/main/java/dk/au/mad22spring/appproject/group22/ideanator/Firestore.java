package dk.au.mad22spring.appproject.group22.ideanator;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Firestore {

    public Firestore(){}

    public static FirebaseFirestore getInstance(){
        return FirebaseFirestore.getInstance();
    }

}
