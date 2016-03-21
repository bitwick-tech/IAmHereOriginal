package tutorialspoint.example.com.iamhereoriginal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Neeraj on 19/03/16.
 */


public class SensorDBHandler extends SQLiteOpenHelper {
    private static int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "sensorsDB.db";
    private String TABLE_SENSOR;
    public static final String COLUMN_ID = "_id";



    public SensorDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.TABLE_SENSOR = name;
    }
    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db){
        String query;
        for(int i = 0; i < MotionSensorEnum.sensorList.length; i++){
            if(!MotionSensorEnum.sensorRequired[i])return;
            query = "CREATE TABLE " +
                    MotionSensorEnum.sensorList[i] + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,";
            for(int j = 0; j < MotionSensorEnum.values()[i].value; i++){
                query += MotionSensorEnum.sensorList[i]+"_"+Integer.toString(i) + " REAL ";
                if(j != MotionSensorEnum.values()[i].value-1){
                    query +=  ",";
                }
                else{
                    query += ")";
                }
            }
            db.execSQL(query);
        }
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(int i = 0; i < MotionSensorEnum.sensorList.length; i++) {
            db.execSQL("DROP TABLE IF EXISTS " + MotionSensorEnum.sensorList[i]);
        }
        onCreate(db);
    }
}
