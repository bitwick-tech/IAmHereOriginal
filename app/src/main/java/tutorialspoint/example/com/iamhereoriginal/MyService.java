package tutorialspoint.example.com.iamhereoriginal;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Neeraj on 19/02/16.
 */
public class MyService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        SensorEventListener {

    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private static MyService instance = null;
    static Location lastLocation = null;
    private SensorManager senSensorManager;
    private ArrayList<Sensor> sensors = new ArrayList<Sensor>();
    public static float[] gravity = {0,0,0};

    public static boolean isInstanceCreated() {
        return instance != null;
    }

    /**
     * Return the communication channel to the service.  May return null if
     * clients can not bind to the service.  The returned
     * {@link IBinder} is usually for a complex interface
     * that has been <a href="{@docRoot}guide/components/aidl.html">described using
     * aidl</a>.
     * <p>
     * <p><em>Note that unlike other application components, calls on to the
     * IBinder interface returned here may not happen on the main thread
     * of the process</em>.  More information about the main thread can be found in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html">Processes and
     * Threads</a>.</p>
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1*1000)        // 30 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 3 seconds, in milliseconds

        mGoogleApiClient.connect();
        handleSensorRegistration();
        instance = this;

        return START_STICKY;
    }

    public String getUniqueIdentifier() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    @Override
    public void onDestroy()
    {
        instance = null;
    }
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }

        //if (location == null) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            handleNewLocation(location);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        //}
        //else {

        // }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
//            try {
//                // Start an Activity that tries to resolve the error
//                //connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
//            } catch (IntentSender.SendIntentException e) {
//                e.printStackTrace();
//            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void handleNewLocation(Location location) {
        if(location == null) return;
        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        double currentAltitude = location.getAltitude();
        long lastUpdateTime = location.getTime();
        float currentSpeed = location.getSpeed();;
        float currentBearing = location.getBearing();
        float currentAccuracy = location.getAccuracy();

        if(lastLocation == null || location.distanceTo(lastLocation) > 0.0){
            MyDBHandler dbHandler = new MyDBHandler(this, null, null, 0);
            MyLocation myLocation = new MyLocation(currentLatitude, currentLongitude, currentAltitude,lastUpdateTime, currentSpeed,currentBearing,currentAccuracy);
            dbHandler.addLocation(myLocation);
            lastLocation = location;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

//    private void sendToServer(){
//        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 0);
//        ServerInteraction serverInteraction = new ServerInteraction(dbHandler);
//        String[] apps = null;//listOfRunningTasks();
//        serverInteraction.sendDataToServer(getUniqueIdentifier(),apps);
//    }

    /*
     * Called when sensor values have changed.
     * <p>See {@link SensorManager SensorManager}
     * for details on possible sensor types.
     * <p>See also {@link SensorEvent SensorEvent}.
     * <p/>
     * <p><b>NOTE:</b> The application doesn't own the
     * {@link SensorEvent event}
     * object passed as a parameter and therefore cannot hold on to it.
     * The object may be part of an internal pool and may be reused by
     * the framework.
     *
     * @param event the {@link SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

//        if (mySensor.getName() == MotionSensorEnum.TYPE_ACCELEROMETER.name()) {
//
//        }

        int type = mySensor.getType();
        int sensorModes = MotionSensorEnum.sSensorReportingModes[mySensor.getType()];
        float[] data = null;
        String name = MotionSensorEnum.sensorList[type-1];
        switch(type){
            case Sensor.TYPE_ACCELEROMETER:
                data = removeGravity(sensorEvent,sensorModes);
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                break;
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                break;
//            case Sensor.TYPE_GLANCE_GESTURE:
//                break;
            case Sensor.TYPE_GRAVITY:
                break;
            case Sensor.TYPE_GYROSCOPE:
                break;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                break;
            case Sensor.TYPE_HEART_RATE:
                break;
            case Sensor.TYPE_LIGHT:
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                break;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                break;
//            case Sensor.TYPE_PICK_UP_GESTURE:
//                break;
            case Sensor.TYPE_PRESSURE:
                break;
            case Sensor.TYPE_PROXIMITY:
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                break;
            case Sensor.TYPE_SIGNIFICANT_MOTION:
                break;
            case Sensor.TYPE_STEP_COUNTER:
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                break;
//            case Sensor.TYPE_TILT_DETECTOR:
//                break;
//            case Sensor.TYPE_WAKE_GESTURE:
//                break;
//            case Sensor.TYPE_ORIENTATION:
//                break;
//            case Sensor.TYPE_TEMPERATURE:
//                break;
            default:
                break;
        }
        if(data == null)
            return;
        String[] formatData = new String[data.length+2];
        formatData[0] = Integer.toString(sensorModes);
        formatData[1] = name;
        for(int i = 0; i < sensorModes; i++){
            formatData[i+2] = Float.toString(data[i]);
        }
        AddSensorDataToDB addSensorDataToDB = new AddSensorDataToDB(this);
        addSensorDataToDB.execute(formatData);

    }

    /**
     * Called when the accuracy of the registered sensor has changed.
     * <p/>
     * <p>See the SENSOR_STATUS_* constants in
     * {@link SensorManager SensorManager} for details.
     *
     * @param sensor
     * @param accuracy The new accuracy of this sensor, one of
     *                 {@code SensorManager.SENSOR_STATUS_*}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


//    class MyTask implements Runnable {
//        @Override
//        public void run() {
//            //mLocationRequest.setFastestInterval(300000);
//            sendToServer();
//            //delete data that has been send to the server
//            //deleteLocations();
//            //mLocationRequest.setFastestInterval(300000);
//        }
//    }

//    public String[] listOfRunningTasks()
//    {
//        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> runningTaskInfo = activityManager.getRunningAppProcesses();//getRunningTasks(Integer.MAX_VALUE);
//        String[] apps = new String[runningTaskInfo.size()];
//        int i=0;
//        for (ActivityManager.RunningAppProcessInfo processInfo : runningTaskInfo) {
//            PackageManager packageManager = getPackageManager();
//            ApplicationInfo applicationInfo = null;
//            for (String activeProcess : processInfo.pkgList) {
//                try
//                {
//                    applicationInfo = packageManager.getApplicationInfo(activeProcess, 0);
//                }
//                catch (final PackageManager.NameNotFoundException e) {}
//                final String title = (String)((applicationInfo != null) ? packageManager.getApplicationLabel(applicationInfo) : "???");
//                apps[i] = title;
//                i++;
//            }
//        }
//        return apps;
//    }

    private void handleSensorRegistration(){
        //private ArrayList<Sensor> sensors = new ArrayList<Sensor>();
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor tmpSensor;
        for(int i = 0; i < MotionSensorEnum.values().length; i++){
            tmpSensor = senSensorManager.getDefaultSensor(i+1);
            sensors.add(tmpSensor); //adding null values also, to maintain index mapping
        }
        //if linear_accelaration is present use it and make type_accelaration null
        if(Sensor.TYPE_LINEAR_ACCELERATION <= MotionSensorEnum.values().length && sensors.get(Sensor.TYPE_LINEAR_ACCELERATION) != null){
            sensors.set(Sensor.TYPE_ACCELEROMETER,null);
        }
        for(int i = 0; i < sensors.size(); i++){
            tmpSensor = sensors.get(i);
            if(tmpSensor != null){
                senSensorManager.registerListener(this, tmpSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
        //List<Sensor> deviceSensors = senSensorManager.getSensorList(Sensor.TYPE_ALL);
    }

    class AddSensorDataToDB extends AsyncTask<String,String,String> {
        private Context mContext;
        public AddSensorDataToDB (Context context){
            mContext = context;
        }
        @Override
        protected String doInBackground(String... params) {
            int size = Integer.parseInt(params[0]);
            float[] values = new float[size];
            String name = params[1];
            for(int i = 0; i < size; i++){
                values[i] = Float.parseFloat(params[i+2]);
            }
            SensorDBHandler db = new SensorDBHandler(mContext,name,null);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }

    }

    private float[] removeGravity(SensorEvent event, int modes){
        // alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

        final float alpha = (float)0.8;
        float[] linear_acceleration = new float[modes];

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];
        return linear_acceleration;
    }
}
