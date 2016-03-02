package tutorialspoint.example.com.iamhereoriginal;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Neeraj on 18/02/16.
 */
public class ServerInteraction {
    private MyDBHandler dbHandler;
    private String deviceId;

    public ServerInteraction(MyDBHandler dbHandler){
        this.dbHandler = dbHandler;
    }

    public String sendDataToServer(String deviceId) {
        //MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        this.deviceId = deviceId;
        ArrayList<MyLocation> locationList = dbHandler.findLocations(System.currentTimeMillis() / 1000L);
        if (locationList.size() > 0) {
            JSONObject finalPostData = formatJsonFromList(locationList);
            if (finalPostData.length() > 0) {
                //return SendJsonDataToServerFunction(String.valueOf(finalPostData));
                new SendJsonDataToServer().execute(String.valueOf(finalPostData));
                //call to async class
            }
        }
        return null;
    }
    public String getTimeZone(){
//        Calendar mCalendar = new GregorianCalendar();
//        TimeZone mTimeZone = mCalendar.getTimeZone();
//        int mGMTOffset = mTimeZone.getRawOffset();
//        long ret = TimeUnit.HOURS.convert(mGMTOffset, TimeUnit.MILLISECONDS);
//        return String.valueOf(ret);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("Z");
        String localTime = date.format(currentLocalTime);
        return localTime;
    }
    private JSONObject formatJsonFromList(ArrayList<MyLocation> locationList) {
        int size = locationList.size();
        if(size > 0) {
            MyLocation tmp;// = new MyLocation();

            JSONArray _timestamp = new JSONArray();
            JSONArray _lat = new JSONArray();
            JSONArray _long = new JSONArray();
            String timeZone = getTimeZone();
            for (int i = 0; i < size; i++) {
                tmp = locationList.get(i);
                _timestamp.put(Long.toString(tmp.getTimestamp()));
                _lat.put(Double.toString(tmp.getLat()));
                _long.put(Double.toString(tmp.getLong()));
            }
            JSONObject resp = new JSONObject();
            try {
                resp.put("timestamp", _timestamp);
                resp.put("latitude", _lat);
                resp.put("longitude", _long);
                resp.put("deviceId", deviceId);
                if(!timeZone.isEmpty()){
                    resp.put("tz",timeZone);
                }
                String myEmail = MyService.email;
                if(!myEmail.isEmpty()){
                    resp.put("email",myEmail);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resp;
        }
        return null;
    }

    class SendJsonDataToServer extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            String JsonResponse = null;
            String JsonDATA = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://ec2-52-29-17-146.eu-central-1.compute.amazonaws.com:8000/ping");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writer
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                //set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
                // json data
                writer.close();
                InputStream inputStream = urlConnection.getInputStream();
                //input stream
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine).append("\n");
                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                JsonResponse = buffer.toString();
                //response data
                //send to post execute
                return JsonResponse;

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }

    }
}