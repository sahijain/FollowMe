package hackathon.onemg.com.followup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * NetworkCaller helps in making the Network call in the APP All the activities
 * will use the NetworkCaller class for making Network call to get the results.
 * 
 */

public class CallNetwork {

	// To check if network call is successful
	public boolean responseResult = false;

	public int statusCode;

	// Map used For POST requests
	public Map<String, String> postFields1;
	public Map<String, String> postFields2;

	// To store the response obtained from NetworkCall
	public String responseString;

	public Bitmap bitmap_image;
	public int width,height;
	public InputStream inputStream;
	private Context ctx;

	public CallNetwork(Context ctx) {
        this.ctx = ctx;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int s) {
		this.statusCode = s;
	}

	/**
	 * GET request
	 * 
	 * @param url
	 *            - URL to which HTTPGET request is made
	 * @return inputStream of response obtained
	 */
	public InputStream getInputStream(String url,boolean isImage) {
		Log.v("GET URL", url);
		/**
		 * "is" the InputStream for storing the result obtained as the result of
		 * GET NetworkCall.
		 */

		InputStream is = null;
		// try {
		try {
			responseResult = false;
			HttpClient httpClient = new DefaultHttpClient();
            url = url.replace(" ", "%20");

			// httpGet to initialize the GET request
			HttpGet httpGet = new HttpGet(url);
            //if(!isImage)
            //httpGet.setHeader("Authorization", "Token token=" + "\"" + Constants.AUTHORIZATION_TOKEN + "\"");
			//Log.v(AppConstants.URL, url);

			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			Log.e("Status Code CN", response.getStatusLine().getStatusCode()
					+ "");

			// Get the Status code of the N/W call made
			int responseCode = response.getStatusLine().getStatusCode();

			statusCode = responseCode;

			responseResult = true;

		} catch (Exception e) {
			responseResult = false;
			e.printStackTrace();
		}

		return is;
	}

	public InputStream postInputStream(String url,String JSonString) {

		InputStream is = null;

		try {

			Log.v("Post URL ", url);

			responseResult = false;

			HttpClient httpClient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(url);

			httpPost.setHeader("Content-Type", "application/json");
			// convert parameters into JSON object

			// passes the results to a string builder/entity
							
				StringEntity se = new StringEntity(JSonString);
				httpPost.setEntity(se);

			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			int responseCode = response.getStatusLine().getStatusCode();

			statusCode = responseCode;

			responseResult = true;
		} catch (Exception e) {
			responseResult = false;
			e.printStackTrace();
		}
		return is;
	}
	
	

	public String makeString(InputStream is) {
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"), 1024);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	// return response string after GET-request
	public String getString(String url) {
		return makeString(getInputStream(url,false));
	}

	// return response string after POST-request
	public String post(String url,String JSonString) {
		return makeString(postInputStream(url,JSonString));
	}

	// returns the Bitmap of the image obtained from the URL
	public Bitmap getBitmap(String url) {

		Bitmap temp_bitmap = null;
		try {
			BitmapFactory.Options Options = new BitmapFactory.Options();
			Options.inJustDecodeBounds = true;

			temp_bitmap = BitmapFactory.decodeStream(getInputStream(url, true), null, Options);
			
			Options.inSampleSize = calculateInSampleSize(Options,
					width, height);
			Options.inJustDecodeBounds = false;
			
			temp_bitmap = BitmapFactory.decodeStream(getInputStream(url, true), null, Options);
			
			Bitmap bm = temp_bitmap;
			if (temp_bitmap != null) {
				return bm;

			} else {
				Log.e("Bitmap : ", "Downloaded Bitmap is null");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return temp_bitmap;
	}
	
	public int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) { // Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2; // Calculate the largest
												// inSampleSize value that
												// is a power of 2 and keeps
												// both // height and width
												// larger than the requested
												// height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;}

	public String makeNetworkCall(String resource, String method,String JSonString) {

		try {
			if (method.equals("GET")) {
				String url = " ";
				try {
					if (!resource.contains("http")) {
						url = Constant.MAIN_URL + resource;
					} else {
						url = resource;
					}
					responseString = "";
					responseString = getString(url);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("POST")) {
				try {
					String url;

					url = Constant.MAIN_URL + resource;
					if (resource.contains("http")) {
						url = resource;
					}
					responseString = "";
					responseString = post(url,JSonString);
					return responseString;
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (method.equals("Image")) {
				// Ad's Bitmap retrieved here
				String url = "";
				if (!resource.contains("http")) {
					url = /*Constants.MAIN_URL +*/ resource;
				} else {
					url = resource;
				}
				bitmap_image = getBitmap(url);

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return method;
	}

}
