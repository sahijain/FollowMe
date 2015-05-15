package hackathon.onemg.com.followup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sahiljain on 14/5/15.
 */
public class AddDialogFragment  extends DialogFragment{

    private Activity mActivity;
    private String mName;
    private String mPhoneNo;

    private EditText mTripName;
    private EditText mDriverNo;
    private EditText mFolloweName;
    private EditText mFollwerNo;

    ArrayList<String> mFollwerNameList;
    ArrayList<String> mFollwerNoList;


    public interface onClickOk{

        public void sendFollowerListToserver(String TripName, String DriverPhoneNo);
    }

    public static AddDialogFragment newInstance( ArrayList<String> nameList, ArrayList<String> phoneNoList){

        AddDialogFragment fragment = new AddDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("NameList", nameList);
        bundle.putSerializable("PhoneNoList", phoneNoList);
        //bundle.putSerializable("timingType", timing);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mFollwerNameList = (ArrayList<String>) bundle.getSerializable("NameList");
        mFollwerNoList = (ArrayList<String>) bundle.getSerializable("PhoneNoList");
        //mTempTimingType = mSelectedTimingType = (TIMING_FILTER) bundle.getSerializable("timingType");
    }

    AlertDialog dialog = null;
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(savedInstanceState != null){

        }
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout f1 = (FrameLayout)getActivity().findViewById(android.R.id.content);
        View view = inflater.inflate(R.layout.add_follower_dialog, f1, false);
        mBuilder.setView(view);

        mTripName = (EditText) view.findViewById(R.id.trip_name);
        mDriverNo = (EditText) view.findViewById(R.id.driver_phone_no);
        mFolloweName = (EditText) view.findViewById(R.id.follwer_name);
        mFollwerNo = (EditText) view.findViewById(R.id.follower_no);

        for(int i =0; i < mFollwerNameList.size(); i++){

            String followerName = mFollwerNameList.get(i);
            if(i == 0){
                mFolloweName.append(followerName);
            }else{
                mFolloweName.append(", " + followerName);
            }
        }

        int m = 0;
        for(int i =0; i < mFollwerNoList.size(); i++){
            String followerNo = mFollwerNoList.get(i);
            if(followerNo != null){

                if(m == 0 ){
                    mFollwerNo.append(followerNo);
                }else{
                    mFollwerNo.append(", " + followerNo);
                }
                m = m + 1;
            }

        }

        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                ((ContactSearchListActivity)mActivity).sendFollowerListToserver(mTripName.getText().toString(),mDriverNo.getText().toString());

            }
        });

        dialog = mBuilder.create();
        return dialog;
    }

}
