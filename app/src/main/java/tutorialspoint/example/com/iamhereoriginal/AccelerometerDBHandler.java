package tutorialspoint.example.com.iamhereoriginal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.SensorEvent;

import java.util.ArrayList;

/**
 * Created by Neeraj on 22/03/16.
 */


public class AccelerometerDBHandler extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "accelerometerDB.db";
    private static final String TABLE_LOCATIONS = "accelerometerData";
    private static int DATABASE_VERSION = 1;
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_lat = "x";
    public static final String COLUMN_long = "y";
    public static final String COLUMN_alt = "z";
    public static final String COLUMN_timestamp = "timestamp";
    public static final String COLUMN_accuracy = "accuracy";

    public AccelerometerDBHandler(Context context,
                                  SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATIONS_TABLE = "CREATE TABLE " +
                TABLE_LOCATIONS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + COLUMN_lat
                + " REAL, " + COLUMN_long + " REAL, " + COLUMN_alt + " REAL, " + COLUMN_timestamp + " INTEGER, "
                + COLUMN_accuracy + " REAL" + ")";

        db.execSQL(CREATE_LOCATIONS_TABLE);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        onCreate(db);
    }

    public void addEvent(MySensorEvent event) {
        long timestamp = event.get_timestamp();
        float accuracy = event.get_accuracy();
        ContentValues values = new ContentValues();
        values.put(COLUMN_lat, event.get_lat());
        values.put(COLUMN_long, event.get_long());
        values.put(COLUMN_alt, event.get_alt());
        values.put(COLUMN_timestamp, timestamp);

        values.put(COLUMN_accuracy, accuracy);
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        db.insert(TABLE_LOCATIONS, null, values);
        db.close();
        return;
    }

    public ArrayList<MySensorEvent> findEvents() {
        //String query = "Select * FROM " + TABLE_LOCATIONS + " WHERE " + COLUMN_timestamp + " <= '" + timestamp+"'";//Long.toString(timestamp);
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery(query, null);
        Cursor cursor = db.query(true, TABLE_LOCATIONS, null, null, null, null, null, COLUMN_timestamp + " ASC", null);
        //new String[] { COLUMN_timestamp,Long.toString(timestamp) }
        ArrayList<MySensorEvent> events = new ArrayList<MySensorEvent>();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            MySensorEvent tmp = new MySensorEvent();
            tmp.set_id(Integer.parseInt(cursor.getString(0)));
            tmp.set_lat(cursor.getDouble(1));
            tmp.set_long(cursor.getDouble(2));
            tmp.set_alt(cursor.getDouble(3));
            tmp.set_timestamp(cursor.getLong(4));
            tmp.set_accuracy(cursor.getFloat(5));
            events.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return events;
    }

    public void deleteLocations() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCATIONS, null, null);
        db.close();
    }
}