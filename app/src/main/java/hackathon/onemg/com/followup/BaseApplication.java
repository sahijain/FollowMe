package hackathon.onemg.com.followup;

import android.app.AlertDialog;
import android.app.Application;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

/**
 * Created by sahil.jain on 3/17/2015.
 */
public class BaseApplication extends Application {

    private final String TAG = BaseApplication.class.getSimpleName();

    // Google Analytics

    // The following line should be changed to include the correct property id.
    //private static final String PROPERTY_ID = "UA-60830888-1";
    private static final String PROPERTY_ID = "UA-61983660-1";

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     *
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    private ShakeListener mShaker;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Sahil", TAG + ": " + "onCreate()");
        GPSTracker.getInstance(this);

        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                /*new AlertDialog.Builder(this)
                        .setPositiveButton(android.R.string.ok, null)
                        .setMessage("Shooken!")
                        .show();*/
                Log.d("Sahil","Device Shaked...");
            }
        });
    }

}
