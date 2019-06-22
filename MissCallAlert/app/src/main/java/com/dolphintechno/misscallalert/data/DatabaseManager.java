package com.dolphintechno.misscallalert.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseManager extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "MissCallInfoDatabase";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "miss_call";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_NUMBER = "number";
    private static final String COLUMN_DATE_TIME = "date_time";
    private static final String COLUMN_MSG = "msg";

    Context context;



    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


//        ,\n
        String sql = new StringBuilder().append("CREATE TABLE ").append(TABLE_NAME).append(" (\n").append("    ").append(COLUMN_ID).append(" INTEGER NOT NULL CONSTRAINT miss_call_pk PRIMARY KEY AUTOINCREMENT,\n").append("    ").append(COLUMN_NAME).append(" varchar(200),\n").append("    ").append(COLUMN_NUMBER).append(" varchar(20),\n").append("    ").append(COLUMN_DATE_TIME).append(" datetime,\n").append("    ").append(COLUMN_MSG).append(" varchar(20)").append(");").toString();


        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }



    public boolean addMissCallInfo(String name, String number, String date_time, String msg) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_NUMBER, number);
        contentValues.put(COLUMN_DATE_TIME, date_time);
        contentValues.put(COLUMN_MSG, msg);
        SQLiteDatabase db = this.getWritableDatabase();
        int dbr = (int) db.insert(TABLE_NAME, null, contentValues);
//        Toast.makeText(context, "DBR  :  "+dbr, Toast.LENGTH_LONG).show();
        return dbr != -1;
    }



    public Cursor getAllMissCallInfo() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }


    boolean updateMissCallInfo(int id, String name, String number, String msg) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_NUMBER, number);
        contentValues.put(COLUMN_MSG, msg);
        return db.update(TABLE_NAME, contentValues, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }



    boolean deleteMissCallInfo(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }

}
