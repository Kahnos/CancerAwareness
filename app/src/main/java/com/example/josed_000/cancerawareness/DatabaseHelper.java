package com.example.josed_000.cancerawareness;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//Status fecha y resultado para los recordatorios #NOT
/**
 * Created by Jesus Castro on 03-05-2015.
 * Modified by Jose Diaz
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;
    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "cancerapp_database";

    //---TABLA DE RECORDATORIO
    private static final String NUMBER_SYMPTOMS="6";

    //Table name
    static final String TABLE_REMINDERS="reminders";

    //Column names
    private static final String COLUMN_STATUS="status";
    private static final String COLUMN_DATE="date";
    private static final String COLUMN_RESULT="result";
    private static final String COLUMN_RESULTSPEC="resultspecification";
    private static final String COLUMN_TYPE="type";

    public DatabaseHelper(Context context1) {
        super(context1, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context1;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_REMINDERS + "("
                + COLUMN_STATUS + " VARCHAR(9) CHECK (" + COLUMN_STATUS + " IN ('Pendiente','Saludable','Anomalia')) NOT NULL,"
                + COLUMN_DATE + " VARCHAR NOT NULL,"            //"dd-MM-yyyy HH:mm"
                + COLUMN_RESULT + " VARCHAR(" + NUMBER_SYMPTOMS + ") NOT NULL,"
                + COLUMN_RESULTSPEC + " VARCHAR(400) DEFAULT 'No hay detalles.',"
                + COLUMN_TYPE + " VARCHAR CHECK(" + COLUMN_TYPE + " IN ('Examen','Doctor','Otro')) NOT NULL,"
                + "PRIMARY KEY (" + COLUMN_TYPE + ", " + COLUMN_DATE + ")"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // simple database upgrade operation:
        // 1) drop the old table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);

        // 2) create a new database
        onCreate(db);
    }

    //===INSERT METHOD

    public int insert(Reminder row) {
        //Database object to manipulate
        SQLiteDatabase db = getWritableDatabase();

        //Values to be added
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, row.status);
        values.put(COLUMN_DATE, row.date);
        values.put(COLUMN_RESULT, row.result);
        values.put(COLUMN_RESULTSPEC, row.details);
        values.put(COLUMN_TYPE, row.type);

        //Add row
        long result1=db.insert(TABLE_REMINDERS, null, values);
        if(result1>=0) {
            //Toast.makeText(context, "Recordatorio registrado", Toast.LENGTH_SHORT).show();
            return 1;
        }
        else {
            //Toast.makeText(context, "Registro fallido", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    //===RETRIEVE
    // Retrieve reminder by date and type.
    public Reminder retrieve(String date, String type) {
        //Database object to manipulate
        SQLiteDatabase db = getReadableDatabase();

        Reminder rem = new Reminder();

        //Query
        Cursor cursor = db.query(TABLE_REMINDERS, new String[]{
                        COLUMN_STATUS,
                        COLUMN_DATE,
                        COLUMN_RESULT,
                        COLUMN_RESULTSPEC,
                        COLUMN_TYPE},
                COLUMN_DATE + "=?" + " AND " + COLUMN_TYPE + "=?",new String[]{date, type}, null, null, null, null);

        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            rem.setReminder(        //(String st, String dt, String rt, String details, String type)
                    cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_RESULT)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_RESULTSPEC)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TYPE))
            );
        }else {
            //Toast.makeText(context,"Sin resultado.",Toast.LENGTH_LONG).show();
            assert cursor != null;
            cursor.close();
            db.close();

            return null;
        }

        //Toast.makeText(context,rem.toString(),Toast.LENGTH_LONG).show();
        // close the database connection and cursor
        cursor.close();
        db.close();

        // return the row as a String
        return rem;
    }

    // Retrieve reminders by status and type.
    public ArrayList<Reminder> retrieveByStatusAndType(String status, String type) {
        //Database object to manipulate
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<Reminder> remArray = new ArrayList<Reminder>();
        //Query
        Cursor cursor = db.query(TABLE_REMINDERS, new String[]{
                        COLUMN_STATUS,
                        COLUMN_DATE,
                        COLUMN_RESULT,
                        COLUMN_RESULTSPEC,
                        COLUMN_TYPE},
                COLUMN_STATUS + "=?" + " AND " + COLUMN_TYPE + "=?", new String[]{status, type}, null, null, COLUMN_DATE, null);

        if (cursor != null && cursor.getCount() > 0) {
            for (cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {
                remArray.add(new Reminder(  cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)), //(String st, String dt, String rt, String details, String type)
                        cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_RESULT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_RESULTSPEC)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TYPE))
                ));
            }
        } else {
            //buffer.append("Sin resultado");
            //Toast.makeText(context, "Sin resultado.", Toast.LENGTH_LONG).show();
            db.close();
            assert cursor != null;
            cursor.close();

            return null;
        }

        //Toast.makeText(context,"Se encontraron " + Integer.toString(remArray.size()) + " ocurrencias.",Toast.LENGTH_LONG).show();
        // close the database connection
        db.close();
        cursor.close();

        // return the row as a String
        return remArray;
    }

    // Retrieve ALL reminders.
    public ArrayList<Reminder> retrieveAllReminders() {
        //Database object to manipulate
        SQLiteDatabase db = getReadableDatabase();

        String column;
        column = COLUMN_DATE;

        ArrayList<Reminder> remArray = new ArrayList<Reminder>();
        //Query
        Cursor cursor = db.query(TABLE_REMINDERS, new String[]{
                        COLUMN_STATUS,
                        COLUMN_DATE,
                        COLUMN_RESULT,
                        COLUMN_RESULTSPEC,
                        COLUMN_TYPE},
                null, null, null, null, COLUMN_DATE);

        if (cursor != null && cursor.getCount() > 0) {
            for (cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {
                remArray.add(new Reminder(  cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)), //(String st, String dt, String rt, String details, String type)
                        cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_RESULT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_RESULTSPEC)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TYPE))
                ));
            }
        } else {
            //buffer.append("Sin resultado");
            //Toast.makeText(context, "Sin resultado.", Toast.LENGTH_LONG).show();
            db.close();
            assert cursor != null;
            cursor.close();

            return null;
        }

        //Toast.makeText(context,"Se encontraron " + Integer.toString(remArray.size()) + " ocurrencias.",Toast.LENGTH_LONG).show();
        // close the database connection
        db.close();
        cursor.close();

        // return the row as a String
        return remArray;
    }

    //===UPDATE
    public int update(String date, String type, String new_status, String new_results, String new_details) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, new_status);
        contentValues.put(COLUMN_RESULT, new_results);
        contentValues.put(COLUMN_RESULTSPEC, new_details);
        String[] whereArgs = {date, type};

        return db.update(TABLE_REMINDERS, contentValues, COLUMN_DATE + "=?" + " AND " + COLUMN_TYPE + "=?", whereArgs);
    }

    //===DELETE
    public void delete(String date, String type) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = {date, type};
        db.delete(TABLE_REMINDERS, COLUMN_DATE + "=?" + " AND " + COLUMN_TYPE + "=?", whereArgs);
        Log.v("SQLite Delete", "Deleted reminder with date "+ date + " and type " + type);
    }

}

//com.example.josed_000.cancerawareness.Reminder object
class Reminder implements Comparable<Reminder>{
    String status;
    String date;
    String result;
    String details;
    String type;
    Date dateCompare;

    public Reminder() {
        status = "Null";
        result = "Null";
        details = "No hay detalles.";
        type = "Other";
    }

    public void setDateCompare(String dt){
        try {
            if( status.equals("Pendiente") ) {
                dateCompare = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date);
            }
            else{
                dateCompare = new SimpleDateFormat("dd-MM-yyyy").parse(date);
            }
        }
        catch (ParseException e){
            Log.v("Reminder constructor", "Parse exception");
        }
    }

    public Date getDateCompare(){
        return dateCompare;
    }

    Reminder(String st, String dt, String type){
        status=st;
        date=dt;
        setDateCompare(dt);
        result="000000";
        details = "No hay detalles.";
        this.type = type;
    }

    Reminder(String st, String dt, String rt, String type){
        status=st;
        date=dt;
        setDateCompare(dt);
        result=rt;
        details = "No hay detalles.";
        this.type = type;
    }

    Reminder(String st, String dt, String rt, String details, String type){
        status=st;
        date=dt;
        setDateCompare(dt);
        result=rt;
        this.details = details;
        this.type = type;
    }

    public void setReminder(String st, String dt, String rt){
        status=st;
        date=dt;
        setDateCompare(dt);
        result=rt;
    }

    public void setReminder(String st, String dt, String rt, String type){
        status=st;
        date=dt;
        setDateCompare(dt);
        result=rt;
        this.type = type;
    }

    public void setReminder(String st, String dt, String rt, String details, String type){
        status=st;
        date=dt;
        setDateCompare(dt);
        result=rt;
        this.details = details;
        this.type = type;
    }

    @Override
    public int compareTo(Reminder another) {
        if (getDateCompare() == null || another.getDateCompare() == null)
            return 0;
        return getDateCompare().compareTo(another.getDateCompare());
    }

    @Override
    public String toString() {
        return "Reminder - Date = " + date +
                " - Tipo = " + type +
                " - Status = " + status +
                " - Result = " + result +
                "- Details = " + details;
    }
}