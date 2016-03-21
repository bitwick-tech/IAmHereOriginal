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
import java.util.Arrays;
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

    public String sendDataToServer(String deviceId, String[] apps, String email) {
        this.deviceId = deviceId;
        ArrayList<MyLocation> locationList = dbHandler.findLocations(System.currentTimeMillis() / 1000L);
        dbHandler.deleteLocations();
        if (locationList.size() > 0 && email != null) {
            JSONObject finalPostData = formatJsonFromList(locationList,apps,email);
            if (finalPostData.length() > 0) {
                //return SendJsonDataToServerFunction(String.valueOf(finalPostData));
                new SendJsonDataToServer().execute(String.valueOf(finalPostData));
                //call to async class
            }
        }
        return null;
    }
    public String getTimeZone(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("Z");
        String localTime = date.format(currentLocalTime);
        return localTime;
    }

    private JSONObject formatJsonFromList(ArrayList<MyLocation> locationList,String[] apps, String myEmail) {
        int size = locationList.size();
        if(size > 0) {
            MyLocation tmp;// = new MyLocation();

//            JSONArray _timestamp = new JSONArray();
//            JSONArray _lat = new JSONArray();
//            JSONArray _long = new JSONArray();
//            JSONArray _alt = new JSONArray();
//            JSONArray _speed = new JSONArray();
//            JSONArray _bearing = new JSONArray();
//            JSONArray _accuracy = new JSONArray();
            JSONObject _locations = new JSONObject();
            //String[] location;
            JSONArray location;
            String timeZone = getTimeZone();
            for (int i = 0; i < size; i++) {
                tmp = locationList.get(i);
                //location = new String[6];
                location = new JSONArray();
//                _timestamp.put(Long.toString(tmp.getTimestamp()));
//                location[0]=(Double.toString(tmp.getLat()));
//                location[1]=(Double.toString(tmp.getLong()));
//                location[2]=(Double.toString(tmp.getAlt()));
//                location[3]=(Float.toString(tmp.getSpeed()));
//                location[4]=(Float.toString(tmp.getBearing()));
//                location[5]=(Float.toString(tmp.getAccuracy()));
                location.put(Double.toString(tmp.getLat()));
                location.put(Double.toString(tmp.getLong()));
                location.put(Double.toString(tmp.getAlt()));
                location.put(Float.toString(tmp.getSpeed()));
                location.put(Float.toString(tmp.getBearing()));
                location.put(Float.toString(tmp.getAccuracy()));

                try {
                    _locations.put(Long.toString(tmp.getTimestamp()), location);//Arrays.asList(location));//Arrays.toString(location));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            JSONObject resp = new JSONObject();
            try {
                //JSONArray appsJson = new JSONArray(Arrays.asList(apps));
                //String locs = _locations.toString();
                resp.put("locations", _locations);//.replace("\\", ""));
//                resp.put("timestamp", _timestamp);
//                resp.put("latitude", _lat);
//                resp.put("longitude", _long);
//                resp.put("altitude", _alt);
//                resp.put("speed", _speed);
//                resp.put("direction", _bearing);
//                resp.put("accuracy", _accuracy);
                resp.put("deviceId", deviceId);
//                if(appsJson.length() > 0){
//                    resp.put("apps",appsJson);
//                }
                if(timeZone!=null && !timeZone.isEmpty()){
                    resp.put("tz",timeZone);
                }
                //String myEmail = MyService.email;
                if(myEmail!=null && !myEmail.isEmpty()){
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