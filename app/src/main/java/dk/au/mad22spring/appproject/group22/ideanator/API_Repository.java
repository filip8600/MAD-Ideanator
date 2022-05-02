package dk.au.mad22spring.appproject.group22.ideanator;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class API_Repository {

    /* GENERAL */
    private static API_Repository instance;            //for Singleton pattern
    private static ExecutorService executor;           //for async processing

    public static API_Repository getInstance(){
        if(instance==null){
            instance = new API_Repository();
        }
        return instance;
    }

    //Constructor
    private API_Repository(){
        executor = Executors.newSingleThreadExecutor();                //executor for background processing
    }


    /* API CONNECTIONS */

    //API CONNECTION - Inspireret af eksempel med Rick and Morty-eksemplet fra undervisningen af Kasper Løvborg Jensen
    static RequestQueue queue;
    private static final String TAG = "API_REPOSITORY";
    private static final String USERNAME_URL = "http://names.drycodes.com/1?format=text";
    //https://avatars.dicebear.com/styles/bottts
    private static final String AVATAR_URL = "https://avatars.dicebear.com/api/bottts/:";


    /* USERNAMES */
    public static void getRandomName(){
        getRandomNameRequest(USERNAME_URL);
    }

    private static void getRandomNameRequest(String url){
        if(queue==null){
            queue = Volley.newRequestQueue(IdeainatorApplication.getAppContext());
            Log.d(TAG,"Username - Create queue");
        }
        Log.d(TAG,url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d(TAG, "onResponse: " + response);
                    try {
                        String responseName = response.replace("_"," ");
                        Log.d(TAG, "getRandomNameRequest: "+responseName);

                        Repository.getInstance().thePlayer.setName(responseName);
                        Repository.getInstance().thePlayer.setImgUrl(AVATAR_URL+responseName+".png");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e(TAG, "That did not work! Seed", error));

        queue.add(stringRequest);
    }


}
