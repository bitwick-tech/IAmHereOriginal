package tutorialspoint.example.com.iamhereoriginal;

/**
 * Created by Neeraj on 22/03/16.
 */
public class MySensorEvent {
    private int _id;
    private double _lat;
    private double _long;
    private double _alt;
    private long _timestamp;
    private float _accuracy;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public double get_lat() {
        return _lat;
    }

    public void set_lat(double _lat) {
        this._lat = _lat;
    }

    public double get_long() {
        return _long;
    }

    public void set_long(double _long) {
        this._long = _long;
    }

    public double get_alt() {
        return _alt;
    }

    public void set_alt(double _alt) {
        this._alt = _alt;
    }

    public long get_timestamp() {
        return _timestamp;
    }

    public void set_timestamp(long _timestamp) {
        this._timestamp = _timestamp;
    }

    public float get_accuracy() {
        return _accuracy;
    }

    public void set_accuracy(float _accuracy) {
        this._accuracy = _accuracy;
    }

    public MySensorEvent(){

    }



}
