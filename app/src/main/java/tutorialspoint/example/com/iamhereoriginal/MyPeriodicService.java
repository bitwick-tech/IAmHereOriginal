package tutorialspoint.example.com.iamhereoriginal;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Neeraj on 15/03/16.
 */

public class MyPeriodicService extends IntentService {
    public MyPeriodicService() {
        super("MyPeriodicService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        sendToServer();
        Log.i("MyPeriodicService", "Service running");
    }
    private void sendToServer(){
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 0);
        AccelerometerDBHandler accelerometerDBHandler = new AccelerometerDBHandler(this,null);
        ServerInteraction serverInteraction = new ServerInteraction(dbHandler,accelerometerDBHandler);
        String[] apps = null;//listOfRunningTasks();
        String email = loadEmail();
        serverInteraction.sendDataToServer(getUniqueIdentifier(), apps, email);
    }
    public String getUniqueIdentifier() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
    public String loadEmail() {
        SharedPreferences sp =
                getSharedPreferences("MyPrefs",
                        Context.MODE_PRIVATE);
        String email = sp.getString("email", null);
        return email;
    }
}
