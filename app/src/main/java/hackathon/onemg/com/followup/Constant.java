package hackathon.onemg.com.followup;

/**
 * Created by sahiljain on 14/5/15.
 */
public class Constant {

    public static enum Result{
        NO_NETWORK,SERVER_ERROR,SUCCESS,TASK_CANCELLED;
    }

    public static final String MAIN_URL = "http://192.168.80.154:3000/";
    public static final String FOLLOWERS_POST_URL = "api/start_trip";
    public static final String FOLLOWERS_TRIP_POST_URL = "api/follower_accepts_trip";
    public static final String FOLLOWERS_TRIP_TRACK_POST_URL = "api/get_follower_gps_and_share_driver_gps";
    public static final String DRIVER_TRIP_TRACK_POST_URL = "api/get_driver_gps_and_share_follower_gps";

    public static final int LOCATION_UPDATE_INTERVAL = 25*1000; //5mins
    public static final int LOCATION_UPDATE_FASTEST_INTERVAL = 15*1000;//2mins
    //public static final int MIN_DISTANCE = 500;//500m
}
