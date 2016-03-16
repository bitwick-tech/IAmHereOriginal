package tutorialspoint.example.com.iamhereoriginal;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Neeraj on 15/03/16.
 */

public class MyPeriodicService extends IntentService {
    private int DATABASE_VERSION = 3;
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
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, DATABASE_VERSION);
        ServerInteraction serverInteraction = new ServerInteraction(dbHandler);
        String[] apps = null;//listOfRunningTasks();
        serverInteraction.sendDataToServer(getUniqueIdentifier(), apps);
    }
    public String getUniqueIdentifier() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}
