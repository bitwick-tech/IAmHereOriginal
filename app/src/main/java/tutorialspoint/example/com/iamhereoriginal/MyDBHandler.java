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


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "locationDB.db";
    private static final String TABLE_LOCATIONS = "locations";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_lat = "lat";
    public static final String COLUMN_long = "long";
    public static final String COLUMN_timestamp = "timestamp";

    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATIONS_TABLE = "CREATE TABLE " +
                TABLE_LOCATIONS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + COLUMN_lat
                + " REAL," + COLUMN_long + " REAL, " + COLUMN_timestamp + " INTEGER" + ")";
        db.execSQL(CREATE_LOCATIONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {

    }

    public void addLocation(MyLocation myLocation) {
        ArrayList<MyLocation> location1 = findLocations();
        ContentValues values = new ContentValues();
        values.put(COLUMN_lat, myLocation.getLat());
        values.put(COLUMN_long, myLocation.getLong());
        values.put(COLUMN_timestamp, myLocation.getTimestamp());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_LOCATIONS, null, values);
        db.close();
        ArrayList<MyLocation> location = findLocations();
        return;
    }

    public ArrayList<MyLocation> findLocations(){//long timestamp) {
        String query = "Select * FROM " + TABLE_LOCATIONS;// + " WHERE " + COLUMN_timestamp + " <= " + Long.toString(timestamp);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<MyLocation> location = new ArrayList<MyLocation>();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false){//(cursor.moveToNext()) {
            MyLocation tmp = new MyLocation();
            tmp.setID(Integer.parseInt(cursor.getString(0)));
            tmp.setTimestamp(cursor.getLong(3));
            tmp.setLat(cursor.getDouble(1));
            tmp.setLong(cursor.getDouble(2));
            location.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return location;
    }

    public int deleteLocations(long timeStamp) {

        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        int count = db.delete(TABLE_LOCATIONS, COLUMN_timestamp + " >= ?", new String[]{Long.toString(timeStamp)});
        db.close();
        return count;
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

