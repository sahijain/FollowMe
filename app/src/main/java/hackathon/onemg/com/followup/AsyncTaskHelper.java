package hackathon.onemg.com.followup;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class AsyncTaskHelper extends AsyncTask<String, String, Constant.Result> {

    private CallNetwork callNetwork;

    private IAsyncTaskHelper mContext;
    private Context ctx;
    private Object mJsonWrapperObject;
    private String mResourceUrl;
    private int DATA_IDENTIFIER;

    private final String TAG = AsyncTaskHelper.class.getSimpleName();

    public AsyncTaskHelper(Context ctx,IAsyncTaskHelper context) {
        mContext = context;
        this.ctx=ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mContext.onPreExecute(DATA_IDENTIFIER);
    }

    @Override
    protected void onCancelled(Constant.Result result) {
        super.onCancelled(result);
    }

    @Override
    protected Constant.Result doInBackground(String... params) {

        callNetwork = new CallNetwork(ctx);
        callNetwork.makeNetworkCall(mResourceUrl, params[0], params[1]);

        if (isCancelled()) {
            return Constant.Result.TASK_CANCELLED;
        }

        if (callNetwork.responseResult) {
            if (callNetwork.responseString != null) {
                Log.v("Sahil" + ": " + TAG,
                        callNetwork.responseString);
                Constant.Result result = mContext.jsonParser(callNetwork.responseString);
                return result;
            }
        } else {
            Log.v("Sahil", ": " + TAG + ": No Network");
        }
        return Constant.Result.NO_NETWORK;
    }

    @Override
    protected void onPostExecute(Constant.Result result) {
        super.onPostExecute(result);
        mContext.onPostExecute(result);
    }


    public void execute(String CallType, String jsonString) {
        this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, CallType, jsonString);
    }

    public void setJsonWrapperObject(Object JsonWrapperObject) {
        this.mJsonWrapperObject = JsonWrapperObject;
    }

    public void setResourceUrl(String ResourceUrl) {
        this.mResourceUrl = ResourceUrl;
    }

    public void setDATA_IDENTIFIER(int dATA_IDENTIFIER) {
        DATA_IDENTIFIER = dATA_IDENTIFIER;
    }

}
