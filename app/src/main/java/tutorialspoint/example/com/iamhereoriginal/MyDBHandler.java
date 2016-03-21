package tutorialspoint.example.com.iamhereoriginal;

/**
 * Created by Neeraj on 18/02/16.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by Neeraj on 07/02/16.
 */
public class MyDBHandler extends SQLiteOpenHelper {

    private static int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "locationDB.db";
    private static final String TABLE_LOCATIONS = "locations";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_lat = "lat";
    public static final String COLUMN_long = "long";
    public static final String COLUMN_alt = "alt";
    public static final String COLUMN_timestamp = "timestamp";
    public static final String COLUMN_speed = "speed";
    public static final String COLUMN_bearing = "bearing";
    public static final String COLUMN_accuracy = "accuracy";

    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATIONS_TABLE = "CREATE TABLE " +
                TABLE_LOCATIONS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + COLUMN_lat
                + " REAL, " + COLUMN_long + " REAL, " + COLUMN_alt + " REAL, " + COLUMN_timestamp + " INTEGER, "
                + COLUMN_speed + " REAL, " + COLUMN_bearing + " REAL, " + COLUMN_accuracy + " REAL" +")";

        db.execSQL(CREATE_LOCATIONS_TABLE);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        onCreate(db);
    }

    public void addLocation(MyLocation myLocation) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_lat, myLocation.getLat());
        values.put(COLUMN_long, myLocation.getLong());
        values.put(COLUMN_alt, myLocation.getAlt());
        values.put(COLUMN_timestamp, myLocation.getTimestamp());
        values.put(COLUMN_speed, myLocation.getSpeed());
        values.put(COLUMN_bearing, myLocation.getBearing());
        values.put(COLUMN_accuracy, myLocation.getAccuracy());
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        db.insert(TABLE_LOCATIONS, null, values);
        db.close();
        return;
    }

    public ArrayList<MyLocation> findLocations(long timestamp) {
        //String query = "Select * FROM " + TABLE_LOCATIONS + " WHERE " + COLUMN_timestamp + " <= '" + timestamp+"'";//Long.toString(timestamp);
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery(query, null);
        Cursor cursor = db.query(true,TABLE_LOCATIONS, null, null, null,null,null,COLUMN_timestamp+" ASC",null);
        //new String[] { COLUMN_timestamp,Long.toString(timestamp) }
        ArrayList<MyLocation> location = new ArrayList<MyLocation>();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false){
            MyLocation tmp = new MyLocation();
            tmp.setID(Integer.parseInt(cursor.getString(0)));
            tmp.setLat(cursor.getDouble(1));
            tmp.setLong(cursor.getDouble(2));
            tmp.setAlt(cursor.getDouble(3));
            tmp.setTimestamp(cursor.getLong(4));
            tmp.setSpeed(cursor.getFloat(5));
            tmp.setBearing(cursor.getFloat(6));
            tmp.setAccuracy(cursor.getFloat(7));
            location.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return location;
    }

    public void deleteLocations(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCATIONS,null,null);
        db.close();
    }

    public boolean isTableExists(String tableName) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

}

