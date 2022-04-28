package dk.au.mad22spring.appproject.group22.ideanator;

import android.app.Application;
import android.content.Context;

public class IdeainatorApplication extends Application {
    private static IdeainatorApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getAppContext(){
        return instance.getApplicationContext();
    }
}