package hackathon.onemg.com.followup;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;


/**
 * Created by sahiljain on 14/5/15.
 */
public class FollowerTripTrackActivity extends ActionBarActivity implements ShakeListener.OnShakeListener{

    private String mTripId;
    private String mPhoneNo;

    private ShakeListener mShaker;
    private Handler mHandler;

    MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_trip_track);

        Intent intent = getIntent();
        mTripId = intent.getStringExtra("tripId");
        mPhoneNo = intent.getStringExtra("phoneNo");

        mHandler = new Handler();

        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(this);

        initPollingForShakeEvent();
    }

    @Override
    public void onShake() {

        AsyncTaskHelper helper = new AsyncTaskHelper(this, new IAsyncTaskHelper() {
            @Override
            public void onPreExecute(int Identifier) {

            }

            @Override
            public Constant.Result jsonParser(String jsonString) {
                return null;
            }

            @Override
            public void onPostExecute(Constant.Result result) {

            }

        });

    }

    private void initPollingForShakeEvent(){

        AsyncTaskHelper helper = new AsyncTaskHelper(this, new IAsyncTaskHelper() {

            @Override
            public void onPreExecute(int Identifier) {

            }

            @Override
            public Constant.Result jsonParser(String jsonString) {
                return null;
            }

            @Override
            public void onPostExecute(Constant.Result result) {

                try{

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            initPollingForShakeEvent();
                        }
                    },5000);

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    initPollingForShakeEvent();
                }

            }

        });

        helper.setResourceUrl(Constant.MAIN_URL);
        helper.execute("GET","");
    }

    public String getTripId() {
        return mTripId;
    }

    public String getPhoneNo() {
        return mPhoneNo;
    }
}
