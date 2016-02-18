package tutorialspoint.example.com.iamhereoriginal;

/**
 * Created by Neeraj on 17/02/16.
 */
public class MyLocation {
    private int _id;
    private double _lat;
    private double _long;
    private long _timestamp;


    public MyLocation() {
    }
    public MyLocation(long _timestamp, double _lat, double _long) {
        this._timestamp = _timestamp;
        this._lat = _lat;
        this._long = _long;
    }
    public int getID() {
        return this._id;
    }

    public double getLat() {
        return this._lat;
    }

    public double getLong() {
        return this._long;
    }

    public long getTimestamp() {
        return this._timestamp;
    }

    public void setTimestamp(long _timestamp) {
        this._timestamp = _timestamp;
    }

    public void setLat(double _lat) {
        this._lat = _lat;
    }

    public void setLong(double _long) {
        this._long = _long;
    }

    public void setID(int _id) {
        this._id = _id;
    }
}

