package dk.au.mad22spring.appproject.group22.ideanator;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Foreground Service derived from Mad solution "Demoservices" (Kasper Løvborg)
public class ForegroundService extends Service {

    private static final String TAG = "IdeaForegroundService";          //tag for logging
    public static final String SERVICE_CHANNEL = "ideaServiceChannel";  //Notification channel name
    public static final int NOTIFICATION_ID = 42;                   //Notification id

    ExecutorService execService;    //ExecutorService for running things off the main thread
    boolean started = false;        //indicating if Service is startet

    //empty constructor
    public ForegroundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //check for Android version - whether we need to create a notification channel (from Android 0 and up, API 26)
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(SERVICE_CHANNEL, "Foreground Service", NotificationManager.IMPORTANCE_LOW);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        //Configure launching the app:
        PackageManager pm = getPackageManager();
        Intent notificationIntent = pm.getLaunchIntentForPackage("dk.au.mad22spring.appproject.group22.ideanator");
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        //build the notification
        Notification notification = new NotificationCompat.Builder(this, SERVICE_CHANNEL)
                .setContentTitle(getString(R.string.gameIsRunning))
                .setContentText(getString(R.string.tapHereToResume))
                .setSmallIcon(R.mipmap.ic_smiley_round)
                .setTicker(getString(R.string.longPressToMute))
                .setContentIntent(contentIntent)
                .build();

        //call to startForeground will promote this Service and launch notification
        startForeground(NOTIFICATION_ID, notification);

        //this method starts recursive background work
        doBackgroundStuff();

        //returning START_STICKY will make the Service restart again eventually if it gets killed off (e.g. due to resources)
        return START_STICKY;
    }

    //initate the background work - only start if not already started
    private void doBackgroundStuff() {
        if(!started) {
            started = true;
            waitAndKill();
        }
    }
    private void waitAndKill(){
        //lazy creation of ExecutorService running as a single threaded executor
        if(execService == null) {
            execService = Executors.newSingleThreadExecutor();
        }

        //submit a new Runnable (implement onRun() ) to the executor - code will run on other thread
        execService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000*60*5);     //sleep 5 minutes
                } catch (InterruptedException e) {
                    Log.e(TAG, "run: EROOR", e);
                }
                if(started) {
                    stopSelf();
                }
            }
        });
    }

    //This is not a bound service, so we return null
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //when Service is destroyed
    @Override
    public void onDestroy() {
        started = false;
        super.onDestroy();
    }
}
