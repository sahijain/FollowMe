package hackathon.onemg.com.followup;

/**
 * Created by sahiljain on 14/5/15.
 */
public class Trip {

    private String tripId;
    private String tripName;
    private String driverPhoneNo;

    public Trip(String tripId,String tripName,String driverPhoneNo){

        this.tripId = tripId;
        this.tripName = tripName;
        this.driverPhoneNo = driverPhoneNo;
    }

    public String getTripId() {
        return tripId;
    }

    public String getTripName() {
        return tripName;
    }

    public String getDriverPhoneNo() {
        return driverPhoneNo;
    }
}
