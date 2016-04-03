package codemsit.weekender;

import android.app.Application;
import android.content.Context;

import com.onesignal.OneSignal;

import codemsit.weekender.utils.SharedPrefs;

/**
 * Created by aman on 2/10/15.
 */
public class App extends Application {

    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        OneSignal.startInit(this).init();

        OneSignal oneSignal = new OneSignal();

        oneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {

                if (registrationId != null) {

                    SharedPrefs.setPrefs("onesignal",registrationId);
                }

            }
        });

    }

    public static App getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}
