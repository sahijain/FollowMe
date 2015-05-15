package hackathon.onemg.com.followup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sahiljain on 14/5/15.
 */
public class FollowerTripsListAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<Trip> mTripList;

    public FollowerTripsListAdapter(Activity activity,ArrayList<Trip> tripList){
        mActivity = activity;
        mTripList = tripList;
    }

    @Override
    public int getCount() {
        if(mTripList == null){
            return 0;
        }
        return mTripList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            view = mActivity.getLayoutInflater().inflate(
                    R.layout.follower_trip_list_item, null, false);
        }

        TextView TripName = (TextView) view.findViewById(R.id.title1);
        TextView DriverNo  = (TextView) view.findViewById(R.id.title2);

        Trip trip = mTripList.get(i);

        TripName.setText("Trip Name : " + trip.getTripName());
        DriverNo.setText("Driver No : " + trip.getDriverPhoneNo());

        return view;
    }
}
