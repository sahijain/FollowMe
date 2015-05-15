package hackathon.onemg.com.followup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by sahiljain on 14/5/15.
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private Button mDriverBtn;
    private Button mFollwerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDriverBtn = (Button) findViewById(R.id.driver_btn);
        mFollwerBtn = (Button) findViewById(R.id.follower_btn);

        mDriverBtn.setOnClickListener(this);
        mFollwerBtn.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.logo);
        //actionBar.setLogo(R.drawable.logo);
    }

    @Override
    public void onClick(View view) {

        if(view == mDriverBtn){

            Intent intent = new Intent(this,ContactSearchListActivity.class);
            startActivity(intent);
        }else if(view == mFollwerBtn){

            Intent intent = new Intent(this,FollowerTripsActivity.class);
            startActivity(intent);

        }
    }
}
