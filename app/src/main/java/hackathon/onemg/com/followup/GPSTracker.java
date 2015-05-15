package  hackathon.onemg.com.followup;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

//Singleton class,aquire the instance through getInstance() method.
//TODO:stop the continous updates
public class GPSTracker implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static GoogleApiClient mGoogleApiClient;
    private final String TAG = GPSTracker.class.getSimpleName();

    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(Constant.LOCATION_UPDATE_INTERVAL)
            .setFastestInterval(Constant.LOCATION_UPDATE_FASTEST_INTERVAL)
            //.setSmallestDisplacement(Constant.MIN_DISTANCE)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    private Context mContext;
    private static GPSTracker sGPSTracker;

    public static final String LOCATION_UPDATE = "locationChanged";

    private GPSTracker(Context context) {
        Log.d("Sahil", TAG + ": " + "GPSTracker()");
        mContext = context;
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
    }

    public static GPSTracker getInstance(Context context) {
        if (sGPSTracker == null) {

            synchronized (GPSTracker.class) {
                if (sGPSTracker == null) {
                    sGPSTracker = new GPSTracker(context);
                    return sGPSTracker;
                }
            }

        }
        return sGPSTracker;
    }

    public void onStop(){
        mGoogleApiClient.disconnect();
    }

    public void onResume(){
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Sahil",
                "onLocationChanged triggered by :  " + location.getProvider());
        Log.d("Sahil", TAG + ": " + "latitude = " + location.getLatitude()
                + ",longitude = " + location.getLongitude());


        Intent intent = new Intent(LOCATION_UPDATE);
        intent.putExtra("Location",location);
        /*intent.putExtra("lat",location.getLatitude());
        intent.putExtra("lng",location.getLongitude());*/
        mContext.sendBroadcast(intent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d("Sahil", TAG + ": " + "Location Service ConnectionFailed()");

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d("Sahil", TAG + ": " + "Location Service Connected()");
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, REQUEST, this);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d("Sahil", TAG + ": " + "Location Service ConnectionSuspended()");

    }

    public Location getLastKnownLocation() {
        return LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
    }

}
