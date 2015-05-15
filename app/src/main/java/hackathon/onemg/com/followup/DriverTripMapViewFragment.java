package hackathon.onemg.com.followup;

/**
 * Created by sahiljain on 14/5/15.
 */

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sahil.jain on 2/18/2015.
 */
public class DriverTripMapViewFragment extends Fragment implements
        OnMapReadyCallback{

    private final String TAG = FollowerTripMapViewFragment.class.getSimpleName();

    private final String SCREEN_NAME = "HomePage";

    private Activity mActivity;
    private View mView;

    private MapView mMapView;
    private Marker mMarker;
    private ArrayList<Marker> mFollowerMarkerList;
    private Circle mCircle;
    private GoogleMap mGoogleMap;

    ArrayList<LatLng> points = new ArrayList<LatLng>();

    private Handler mHandler;
    private int mGpsRecheckCount = 1;
    private final int MAX_GPS_RECHECK_COUNT = 2;
    private final int DELAY = 2000;

    private boolean isDefaultLocationData = true;

    private int count = 1;

    private boolean isFirstTime = true;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("Sahil", TAG + ": " + "onCreate()");
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        /*if(!AppUtils.isConnectedToInternet(mActivity)){
            AppUtils.showNoNetworkDialogue(mActivity);
        }*/
        //checkIfCanGetTopDoctors();
        GPSTracker gpsTracker = GPSTracker.getInstance(mActivity.getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Sahil",TAG + ": " + "onCreateView()");

        mView = inflater.inflate(R.layout.map_view, container, false);

        mMapView = ((MapView) mView.findViewById(R.id.mapView));
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        return mView;
    }

    private void showDoctorsOnMapIfReady(){

        /*if (mGoogleMap != null && mDoctorList != null) {

        }*/

        postAndGetLatLng();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //googleMap.setMyLocationEnabled(true); //ToDo: crash is observed on enabling this.
        Log.d("Sahil","Map is ready");
        mGoogleMap = googleMap;
        configureMap(googleMap);
        initCurLocation();
        showDoctorsOnMapIfReady();
    }

    private void configureMap(GoogleMap map) {
        if (map == null)
            return; // Google Maps not available
        try {
            MapsInitializer.initialize(mActivity);
        } catch (Exception e) {
            Log.e("Sahil", "Have GoogleMap but then error", e);
            e.printStackTrace();
            return;
        }
		/*
		 * map.setMyLocationEnabled(true); LatLng latLng = new LatLng(lat, lon);
		 * CameraUpdate camera = CameraUpdateFactory.newLatLng(latLng);
		 * map.animateCamera(camera);
		 */
    }

    @Override
    public void onPause() {
        Log.d("Sahil", TAG + ": " + "onPause()");
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d("Sahil", TAG + ": " + "onResume()");
        mMapView.onResume();
        super.onResume();

        /*if(mDoctorList == null ){
            resetCount();
            fetchDoctors();
        }*/
    }

    @Override
    public void onStop() {
        Log.d("Sahil", TAG + ": " + "onStop()");

        super.onStop();
        try {
        } catch (Exception e) {

        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void onSaveInstanceState(Bundle paramBundle) {
        Log.d("Sahil", TAG + ": " + "onSaveInstanceState()");

        mMapView.onSaveInstanceState(paramBundle);
        super.onSaveInstanceState(paramBundle);
    }


    @Override
    public void onDestroy() {
        Log.d("Sahil",TAG + ": " + "onDestroy()");
        mMapView.onDestroy();
        if(mMarker != null){
            mMarker.remove();
            mMarker = null;
        }
        if(mCircle != null){
            mCircle.remove();
            mCircle = null;
        }

        mGoogleMap.clear();
        mGoogleMap = null;
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.d("Sahil",TAG + ": " + "onDestroyView()");

        super.onDestroyView();
    }

    /*private void checkIfCanGetTopDoctors(){

        GPSTracker gpsTracker = GPSTracker.getInstance(mActivity.getApplicationContext());
        Location location = gpsTracker.getLastKnownLocation();

        if(location != null){
            initCurLocation(mGoogleMap,GPSTracker.getInstance(mActivity.getApplicationContext())
                    .getLastKnownLocation());
            getTopDoctors();
        }else{
            IntentFilter filter = new IntentFilter(GPSTracker.LOCATION_UPDATE);
            mActivity.registerReceiver(mReciever,filter);
        }

    }*/

    private void resetCount(){
        mGpsRecheckCount = 1;
    }


    private void initCurLocation(){
        if (mMarker != null) {
            mMarker.remove();

        }

        GPSTracker gpsTracker = GPSTracker.getInstance(mActivity.getApplicationContext());
        Location currentLocation = gpsTracker.getLastKnownLocation();

        if(currentLocation != null){
            // Getting latitude of the current location
            double latitude = currentLocation.getLatitude();

            // Getting longitude of the current location
            double longitude = currentLocation.getLongitude();

            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);
            // GoogleMap googleMap = mMapView.getMap();

            if (mGoogleMap != null) {
                Log.d("Sahil",TAG + ": " + "Adding marker");
                mMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng)
                );
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));
                //mMarker.showInfoWindow();
            }
        }/*else{
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getDefaultLatLng(),13));
        }*/
    }

    private void initDriverLocation(LatLng latlng){
        if (mMarker != null) {
            mMarker.remove();

        }

        if(latlng != null){

            if (mGoogleMap != null) {
                Log.d("Sahil",TAG + ": " + "Adding marker for driver, " +latlng.toString());
                mMarker = mGoogleMap.addMarker(new MarkerOptions().position(latlng));

                //if(count == 1)
                //mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,20));
                //mMarker.showInfoWindow();
                if(isFirstTime){
                    mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(20));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
                }else{
                    float currentZoom  = mGoogleMap.getCameraPosition().zoom;
                    Log.d("Sahil","current zoom = " + currentZoom);
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
                }
                isFirstTime = false;

            }
        }/*else{
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getDefaultLatLng(),13));
        }*/
    }

    private void initFollowersLocation( ArrayList<LatLng> follwersLatlngList){
        if (mFollowerMarkerList != null) {
            for(int i =0; i < mFollowerMarkerList.size(); i++){

                Marker marker = mFollowerMarkerList.get(i);
                marker.remove();
            }
        }

        if(follwersLatlngList != null){

            if (mGoogleMap != null) {

                for (int i = 0; i < follwersLatlngList.size(); i++){
                    Log.d("Sahil", TAG + ": " + "Adding marker");

                    Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(follwersLatlngList.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    if(mFollowerMarkerList == null){
                        mFollowerMarkerList = new ArrayList<Marker>();
                    }
                    mFollowerMarkerList.add(marker);
                    //if(count == 1)
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(follwersLatlngList.get(i), 20));

                    if(isFirstTime){
                        mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(20));
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(follwersLatlngList.get(i)));
                    }else{
                        float currentZoom  = mGoogleMap.getCameraPosition().zoom;
                        Log.d("Sahil","current zoom = " + currentZoom);
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(follwersLatlngList.get(i)));
                    }
                    isFirstTime = false;

                }



                //mMarker.showInfoWindow();
            }
        }/*else{
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getDefaultLatLng(),13));
        }*/
    }

    private void postAndGetLatLng(){

        AsyncTaskHelper helper = new AsyncTaskHelper(mActivity, new IAsyncTaskHelper() {

            LatLng mDriverlatlng;
            ArrayList<LatLng> mFollwersLatlngList = new ArrayList<LatLng>();

            @Override
            public void onPreExecute(int Identifier) {

            }

            @Override
            public Constant.Result jsonParser(String jsonString) {
                //LatLng defaultLatLng = new LatLng(Double.parseDouble(latLngArray[0]),Double.parseDouble(latLngArray[1]));
                try {


                    JSONObject jsonObj = new JSONObject(jsonString);
                    JSONArray followersjsonArray= jsonObj.getJSONArray("lat_long");

                    for(int i =0; i< followersjsonArray.length(); i++){
                        JSONObject jsonObj1 = followersjsonArray.getJSONObject(i);
                        String latitude = jsonObj1.getString("latitude");
                        String longitude = jsonObj1.getString("longitude");
                        LatLng latlng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                        mFollwersLatlngList.add(latlng);

                    }

                    /*JSONObject driverjsonObj = jsonObj.getJSONObject("driver_lat");
                    String latitude = driverjsonObj.getString("latitude");
                    String longitude = driverjsonObj.getString("longitude");
                    LatLng latlng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                    mDriverlatlng = latlng;*/

                    return Constant.Result.SUCCESS;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return Constant.Result.SERVER_ERROR;
            }

            @Override
            public void onPostExecute(Constant.Result result) {

                try {

                    switch (result) {

                        case NO_NETWORK:

                            Toast.makeText(mActivity,
                                    "Network Error", Toast.LENGTH_LONG).show();
                            break;

                        case SERVER_ERROR:

                            Toast.makeText(mActivity,
                                    "Server Error", Toast.LENGTH_LONG).show();

                            break;

                        case SUCCESS:

                            //initDriverLocation(mDriverlatlng);
                            initCurLocation();
                            initFollowersLocation(mFollwersLatlngList);
                            // }

                            //if(count <= 10){
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //count = count + 1;
                                        postAndGetLatLng();
                                    }
                                },5000);
                            //}


                            break;

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        GPSTracker gpsTracker = GPSTracker.getInstance(mActivity.getApplicationContext());
        Location currentLocation = gpsTracker.getLastKnownLocation();

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("trip_id",((DriverTripTrackActivity)mActivity).getTripId());
            jsonObj.put("phone_no",((DriverTripTrackActivity)mActivity).getmDriverPhoneNo());
            jsonObj.put("latitude",currentLocation.getLatitude() + "");
            jsonObj.put("longitude",currentLocation.getLongitude() + "");
            jsonObj.put("time",getTimeInSec() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        helper.setResourceUrl(Constant.DRIVER_TRIP_TRACK_POST_URL);
        helper.execute("POST", jsonObj.toString());
    }


    public static long getTimeInSec(){
/*        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);*/

        return System.currentTimeMillis()/1000;
    }

}

