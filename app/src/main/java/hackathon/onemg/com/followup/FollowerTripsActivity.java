package hackathon.onemg.com.followup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sahiljain on 14/5/15.
 */
public class FollowerTripsActivity extends ActionBarActivity implements ListView.OnItemClickListener{

    private ListView mListView;
    private FollowerTripsListAdapter mListAdapter;
    private ArrayList<Trip> mTripList;
    private String mPhoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_trips);
        init();
    }

    private void init(){

        mListView = (ListView) findViewById(R.id.trip_lists);

        mListView.setEmptyView(findViewById(R.id.place_holder));
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mListAdapter);

        mListAdapter = new FollowerTripsListAdapter(this,mTripList);
        inputPhoneNo();
    }

    private void inputPhoneNo(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout f1 = (FrameLayout)findViewById(android.R.id.content);
        View view = inflater.inflate(R.layout.phone_no_dialog, f1, false);
        mBuilder.setTitle("Enter Your Phone No");
        final EditText editText = (EditText) view.findViewById(R.id.phone_no);
        mBuilder.setView(view);
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPhoneNo = editText.getText().toString();
                getTripFromServer();
            }
        });

        Dialog dialog = mBuilder.create();
        dialog.show();


    }

    private void getTripFromServer(){

        AsyncTaskHelper helper = new AsyncTaskHelper(this, new IAsyncTaskHelper() {

            ArrayList<Trip> tempList;

            ProgressDialog pd;
            @Override
            public void onPreExecute(int Identifier) {
                pd = new ProgressDialog(FollowerTripsActivity.this);
                pd.setMessage("Registering");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            public Constant.Result jsonParser(String jsonString) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonString);

                    tempList = new ArrayList<Trip>();
                    JSONArray tripsArray = jsonObj.getJSONArray("trips");
                    for(int i=0; i< tripsArray.length(); i++){
                        JSONObject tripObj = tripsArray.getJSONObject(i);
                        String tripId = tripObj.getString("trip_id");
                        String tripName = tripObj.getString("trip_name");
                        String driverPhoneNo = tripObj.getString("driver_phone_no");

                        Trip trip = new Trip(tripId,tripName,driverPhoneNo);
                        tempList.add(trip);
                    }

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

                                Toast.makeText(FollowerTripsActivity.this,
                                        "Network Error", Toast.LENGTH_LONG).show();
                            break;

                        case SERVER_ERROR:

                                Toast.makeText(FollowerTripsActivity.this,
                                        "Server Error", Toast.LENGTH_LONG).show();

                            break;

                        case SUCCESS:
                            mTripList = tempList;
                            mListView.setAdapter(new FollowerTripsListAdapter(FollowerTripsActivity.this,mTripList));
                            mListAdapter.notifyDataSetChanged();
                            break;

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    pd.dismiss();
                }
            }

        });

        JSONObject jsonobj = new JSONObject();
        try {
            jsonobj.put("phone_no",mPhoneNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        helper.setResourceUrl(Constant.FOLLOWERS_TRIP_POST_URL);
        helper.execute("POST", jsonobj.toString());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent = new Intent(this,FollowerTripTrackActivity.class);
        intent.putExtra("tripId",mTripList.get(i).getTripId());
        intent.putExtra("phoneNo",mPhoneNo);
        startActivity(intent);
    }
}
