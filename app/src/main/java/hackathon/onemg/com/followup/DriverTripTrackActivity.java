package hackathon.onemg.com.followup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by sahiljain on 15/5/15.
 */
public class DriverTripTrackActivity extends ActionBarActivity {

    private String mTripId;

    public String getmDriverPhoneNo() {
        return mDriverPhoneNo;
    }

    private String mDriverPhoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_trip_track);

        Intent intent = getIntent();
        mTripId = intent.getStringExtra("tripId");
        mDriverPhoneNo = intent.getStringExtra("driverPhoneNo");
    }

    public String getTripId() {
        return mTripId;
    }
}
