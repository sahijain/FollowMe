package hackathon.onemg.com.followup;


public interface IAsyncTaskHelper {

	public void onPreExecute(int Identifier);
	
	public void onPostExecute(Constant.Result result);
	
	public Constant.Result jsonParser(String jsonString);
}
