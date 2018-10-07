package placementproject.studentapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBAdapter {
    private static final String KEY_ROWID = "_id";
    private static final String KEY_SERVER_ID = "server_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_BODY = "body";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_COLUMN = "column";
    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "faculty";
    private static final String DATABASE_TABLE = "newsfeed";
    private static final String DATABASE_TABLE_DELETED = "deleted";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_CLICK = "click";

    private static final String DATABASE_CREATE_TABLE = "create table "
            + DATABASE_TABLE + " (_id integer primary key autoincrement, "  //0
            + KEY_SERVER_ID + " integer not null, "                         //1
            + KEY_TITLE + " text not null, "                                 //2
            + KEY_BODY + " text not null, "                                  //3
            + KEY_DATE + " text not null, "                                  //4
            + KEY_TIME + " text not null, "                                  //5
            + KEY_COLUMN + " text not null, "                                //6
            + KEY_CLICK + " text not null, "                                //7
            + "UNIQUE(" + KEY_SERVER_ID + ") ON CONFLICT IGNORE);";
    private static final String DATABASE_CREATE_TABLE_DELETED = "create table "
            + DATABASE_TABLE_DELETED + " (_id integer primary key autoincrement, "  //0
            + KEY_SERVER_ID + " integer not null, "                                 //1
            + "UNIQUE(" + KEY_SERVER_ID + ") ON CONFLICT IGNORE);";

    final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE_TABLE);
                db.execSQL(DATABASE_CREATE_TABLE_DELETED);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "upgrading database from version " + oldVersion + " to " + newVersion + " which will destroy all old data");
            onCreate(db);
        }
    }

    //opens the database
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //close the database
    public void close() {
        DBHelper.close();
    }

    //insert a values into the database...
    long insertData(Integer server_id, String title, String body, String date, String time, String column,String click) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SERVER_ID, server_id);
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_BODY, body);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_TIME, time);
        initialValues.put(KEY_COLUMN, column);
        initialValues.put(KEY_CLICK, click);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //insert a values into the database...
    long insertDataDeleted(Integer server_id) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SERVER_ID, server_id);
        return db.insert(DATABASE_TABLE_DELETED, null, initialValues);
    }

    // query is used for data fetching***** that CURSOR class is used***
    //retrieve all data
    Cursor getAllData() {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_SERVER_ID, KEY_TITLE, KEY_BODY, KEY_DATE, KEY_TIME, KEY_COLUMN,KEY_CLICK}
                , null, null, null, null, null);
    }

    Cursor getAllDeletedData() {
        return db.query(DATABASE_TABLE_DELETED, new String[]{KEY_ROWID, KEY_SERVER_ID}
                , null, null, null, null, null);
    }


    //delete a particular data
    boolean deleteData(long rowId) {
        return db.delete(DATABASE_TABLE, KEY_SERVER_ID + "=" + rowId, null) > 0;
    }
    //updates a particular data
    boolean updateData(long rowid) {
        String click="true";
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CLICK, click);
        return db.update(DATABASE_TABLE, initialValues, KEY_ROWID + "=" + rowid, null) > 0;
    }
}

    /*//retrieve particular data
    public Cursor getData(long rowId) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[]{KEY_ROWID, KEY_SERVER_ID, KEY_TITLE, KEY_BODY, KEY_DATE,
                        KEY_TIME,KEY_COLUMN},
                KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //delete all data
    public boolean deleteAllData() {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }

    //updates a particular data
    public boolean updateContact(long rowId, Integer server_id, String title, String body, String date, String time,String column) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SERVER_ID, server_id);
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_BODY, body);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_TIME, time);
        initialValues.put(KEY_COLUMN, column);
        return db.update(DATABASE_TABLE, initialValues, KEY_ROWID + "=" + rowId, null) > 0;
    }*/
