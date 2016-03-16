package tutorialspoint.example.com.iamhereoriginal;

/**
 * Created by Neeraj on 17/02/16.
 */
public class MyLocation {
    private int _id;
    private double _lat;
    private double _long;
    private double _alt;
    private long _timestamp;
    private float _speed;
    private float _bearing;
    private float _accuracy;


    public MyLocation() {
    }
    public MyLocation(double _lat, double _long, double _alt, long _timestamp, float _speed, float _bearing, float _accuracy) {
        this._timestamp = _timestamp;
        this._lat = _lat;
        this._long = _long;
        this._alt = _alt;
        this._speed = _speed;
        this._bearing = _bearing;
        this._accuracy = _accuracy;
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

    public float getSpeed() {
        return _speed;
    }

    public void setSpeed(float _speed) {
        this._speed = _speed;
    }

    public float getBearing() {
        return _bearing;
    }

    public void setBearing(float _bearing) {
        this._bearing = _bearing;
    }

    public double getAlt() {
        return _alt;
    }

    public void setAlt(double _alt) {
        this._alt = _alt;
    }

    public float getAccuracy() {
        return _accuracy;
    }

    public void setAccuracy(float _accuracy) {
        this._accuracy = _accuracy;
    }
}

