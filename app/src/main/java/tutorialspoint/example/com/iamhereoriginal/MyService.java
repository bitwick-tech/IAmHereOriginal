package tutorialspoint.example.com.iamhereoriginal;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
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

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Neeraj on 19/02/16.
 */
public class MyService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{//,
        //SensorEventListener {

    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    public static String email;
    private int DATABASE_VERSION = 3;
    private static MyService instance = null;
    static Location lastLocation = null;
//    private SensorManager senSensorManager;
//    private Sensor senAccelerometer;

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

//        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        //List<Sensor> deviceSensors = senSensorManager.getSensorList(Sensor.TYPE_ALL);



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
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            handleNewLocation(location);
        }
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
//        if (connectionResult.hasResolution()) {
//            try {
//                // Start an Activity that tries to resolve the error
//                //connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
//            } catch (IntentSender.SendIntentException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
//        }
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
            MyDBHandler dbHandler = new MyDBHandler(this, null, null, DATABASE_VERSION);
            MyLocation myLocation = new MyLocation(currentLatitude, currentLongitude, currentAltitude,lastUpdateTime, currentSpeed,currentBearing,currentAccuracy);
            dbHandler.addLocation(myLocation);
            lastLocation = location;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    private void sendToServer(){
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, DATABASE_VERSION);
        ServerInteraction serverInteraction = new ServerInteraction(dbHandler);
        String[] apps = null;//listOfRunningTasks();
        serverInteraction.sendDataToServer(getUniqueIdentifier(),apps);
    }

    /**
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
//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        Sensor mySensor = sensorEvent.sensor;
//
//        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//
//        }
//    }
//
//    /**
//     * Called when the accuracy of the registered sensor has changed.
//     * <p/>
//     * <p>See the SENSOR_STATUS_* constants in
//     * {@link SensorManager SensorManager} for details.
//     *
//     * @param sensor
//     * @param accuracy The new accuracy of this sensor, one of
//     *                 {@code SensorManager.SENSOR_STATUS_*}
//     */
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }

//    public void deleteLocations(){
//        MyDBHandler dbHandler = new MyDBHandler(this, null, null, DATABASE_VERSION);
//        dbHandler.deleteLocations();
//    }

    class MyTask implements Runnable {
        @Override
        public void run() {
            //mLocationRequest.setFastestInterval(300000);
            sendToServer();
            //delete data that has been send to the server
            //deleteLocations();
            //mLocationRequest.setFastestInterval(300000);
        }
    }

    public String[] listOfRunningTasks()
    {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningTaskInfo = activityManager.getRunningAppProcesses();//getRunningTasks(Integer.MAX_VALUE);
        String[] apps = new String[runningTaskInfo.size()];
        int i=0;
        for (ActivityManager.RunningAppProcessInfo processInfo : runningTaskInfo) {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = null;
            for (String activeProcess : processInfo.pkgList) {
                try
                {
                    applicationInfo = packageManager.getApplicationInfo(activeProcess, 0);
                }
                catch (final PackageManager.NameNotFoundException e) {}
                final String title = (String)((applicationInfo != null) ? packageManager.getApplicationLabel(applicationInfo) : "???");
                apps[i] = title;
                i++;
            }
        }
        return apps;
    }
}
