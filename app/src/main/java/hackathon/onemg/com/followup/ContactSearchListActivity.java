package hackathon.onemg.com.followup;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sahiljain on 14/5/15.
 */
public class ContactSearchListActivity extends FragmentActivity implements
        ContactsListFragment.OnContactsInteractionListener, View.OnClickListener,AddDialogFragment.onClickOk{

    // Defines a tag for identifying log entries
    private static final String TAG = "ContactsListActivity";

    // True if this activity instance is a search result view (used on pre-HC devices that load
    // search results in a separate instance of the activity rather than loading results in-line
    // as the query is typed.
    private boolean isSearchResultView = false;

    private ArrayList<Uri> mContactList = new ArrayList<Uri>();
    private TextView mAddFollowers;
    private TextView mStartTrip;

    public String getmTripId() {
        return mTripId;
    }

    private String mTripId;

    private String mDriverPohoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set main content view. On smaller screen devices this is a single pane view with one
        // fragment. One larger screen devices this is a two pane view with two fragments.
        setContentView(R.layout.activity_contact_search_list);

        mAddFollowers = (TextView) findViewById(R.id.add_followers);
        mStartTrip = (TextView) findViewById(R.id.start_trip);
        mAddFollowers.setOnClickListener(this);
        mStartTrip.setOnClickListener(this);

        // Check if this activity instance has been triggered as a result of a search query. This
        // will only happen on pre-HC OS versions as from HC onward search is carried out using
        // an ActionBar SearchView which carries out the search in-line without loading a new
        // Activity.
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {

            // Fetch query from intent and notify the fragment that it should display search
            // results instead of all contacts.
            String searchQuery = getIntent().getStringExtra(SearchManager.QUERY);
            ContactsListFragment mContactsListFragment = (ContactsListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.contact_list);

            // This flag notes that the Activity is doing a search, and so the result will be
            // search results rather than all contacts. This prevents the Activity and Fragment
            // from trying to a search on search results.
            isSearchResultView = true;
            mContactsListFragment.setSearchQuery(searchQuery);

            // Set special title for search results
            String title = getString(R.string.contacts_list_search_results_title, searchQuery);
            setTitle(title);
        }
    }

    /**
     * This interface callback lets the main contacts list fragment notify
     * this activity that a contact has been selected.
     *
     * @param contactUri The contact Uri to the selected contact.
     */
    @Override
    public void onContactSelected(Uri contactUri) {
        /*if (isTwoPaneLayout && mContactDetailFragment != null) {
            // If two pane layout then update the detail fragment to show the selected contact
            mContactDetailFragment.setContact(contactUri);
        } else {
            // Otherwise single pane layout, start a new ContactDetailActivity with
            // the contact Uri
            Intent intent = new Intent(this, ContactDetailActivity.class);
            intent.setData(contactUri);
            startActivity(intent);
        }*/

        Log.d("Sahil", "onContactSelected,contactUri = " +contactUri);
        uriContact = contactUri;
       // String contactName = retrieveContactName();
        //String phoneNo = retrieveContactNumber();
        Toast.makeText(this,"Follower added to list",Toast.LENGTH_SHORT).show();
        mContactList.add(contactUri);
       /* AddDialogFragment fragment = AddDialogFragment.newInstance(contactName,phoneNo);

        fragment.show(
                getSupportFragmentManager(), "");*/
    }

    /**
     * This interface callback lets the main contacts list fragment notify
     * this activity that a contact is no longer selected.
     */
    @Override
    public void onSelectionCleared() {
       /* if (isTwoPaneLayout && mContactDetailFragment != null) {
            mContactDetailFragment.setContact(null);
        }*/
    }

    @Override
    public boolean onSearchRequested() {
        // Don't allow another search if this activity instance is already showing
        // search results. Only used pre-HC.
        return !isSearchResultView && super.onSearchRequested();
    }

    private Uri uriContact;
    private String contactID;

    private String retrieveContactNumber(Uri uri) {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uri,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? " /*AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE*/,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        Log.d("Sahil", "Contact Phone Number: " + contactNumber);
        return contactNumber;
    }

    private String retrieveContactName(Uri uri) {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        Log.d("Sahil", "Contact Name: " + contactName);
        return contactName;
    }

    ArrayList<String> mnameList = new ArrayList<String>();
    ArrayList<String> mNoList = new ArrayList<String>();

    private HashMap<String,String> mNameMap = new HashMap<String,String>();
    private HashMap<String,String> mNoMap = new HashMap<String,String>();

    @Override
    public void onClick(View view) {

        if(view == mAddFollowers){

            for(int i=0; i < mContactList.size(); i++){
                //mnameList.add(retrieveContactName(mContactList.get(i)));
                //mNoList.add(retrieveContactNumber(mContactList.get(i)));
                mNameMap.put(retrieveContactName(mContactList.get(i)), "");
                mNoMap.put(retrieveContactNumber(mContactList.get(i)),"");
            }

            //AddDialogFragment fragment = AddDialogFragment.newInstance(mnameList,mNoList);
            AddDialogFragment fragment = AddDialogFragment.newInstance(new ArrayList<String>(mNameMap.keySet()),new ArrayList<String>(mNoMap.keySet()));

            fragment.show(
                    getSupportFragmentManager(), "");

        }else if(view == mStartTrip){

                if(mTripId == null){
                    Toast.makeText(ContactSearchListActivity.this,
                            "Dude pls add followers first", Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(this,DriverTripTrackActivity.class);
                    intent.putExtra("tripId",mTripId);
                    intent.putExtra("driverPhoneNo",mDriverPohoneNo);
                    startActivity(intent);
                }

        }
    }

    @Override
    public void sendFollowerListToserver(final String TripName, final String DriverPhoneNo) {

        AsyncTaskHelper helper = new AsyncTaskHelper(this, new IAsyncTaskHelper() {

            ProgressDialog pd;

            @Override
            public void onPreExecute(int Identifier) {

                pd = new ProgressDialog(ContactSearchListActivity.this);
                pd.setMessage("Setting the trip");
                pd.show();
                pd.setCancelable(false);
            }

            @Override
            public Constant.Result jsonParser(String jsonString) {

                try {
                    JSONObject jsonObject = new JSONObject(jsonString);

                    if(!jsonObject.has("success")){
                        return Constant.Result.SERVER_ERROR;
                    }
                    mTripId = jsonObject.getString("trip_id");
                    mDriverPohoneNo = jsonObject.getString("driver_phone_no");
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

                            Toast.makeText(ContactSearchListActivity.this,
                                    "Network Error", Toast.LENGTH_LONG).show();
                            break;

                        case SERVER_ERROR:

                            Toast.makeText(ContactSearchListActivity.this,
                                    "Server Error", Toast.LENGTH_LONG).show();

                            break;

                        case SUCCESS:

                            break;

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    pd.dismiss();
                }
            }
        });

        String[] noArray = new String[mnameList.size()];

        for(int i =0; i < mnameList.size(); i++){
            noArray[i] = mNoList.get(i);

        }
        Log.d("Sahil","Array = " + noArray.toString());

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("trip_name",TripName);
            jsonObj.put("driver_phone_no",DriverPhoneNo);
            jsonObj.put("followers_phone_nos",new ArrayList<String>(mNoMap.keySet()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        helper.setResourceUrl(Constant.FOLLOWERS_POST_URL);
        helper.execute("POST", jsonObj.toString());
    }


    public String getDriverPohoneNo() {
        return mDriverPohoneNo;
    }
}
