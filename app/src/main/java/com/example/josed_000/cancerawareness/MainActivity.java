package com.example.josed_000.cancerawareness;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class MainActivity extends ActionBarActivity {

    public void infoBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        builder.setCancelable(true);
        builder.setTitle(R.string.info_main_title);
        builder.setMessage(R.string.info_main);
        builder.create().show();
    }

    public void didYouKnow(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        builder.setCancelable(true);
        builder.setTitle(R.string.didyouknow_title);
        int youKnows[]={
                R.string.sabias_que_1,R.string.sabias_que_2,R.string.sabias_que_3,
                R.string.sabias_que_4,R.string.sabias_que_5,R.string.sabias_que_6,
                R.string.sabias_que_7,R.string.sabias_que_8,R.string.sabias_que_9,
                R.string.sabias_que_10,R.string.sabias_que_12,R.string.sabias_que_13,
                R.string.sabias_que_14,R.string.sabias_que_15,R.string.sabias_que_16,
                R.string.sabias_que_17,R.string.sabias_que_18,R.string.sabias_que_19,
                R.string.sabias_que_11,R.string.sabias_que_20
        };
        builder.setMessage(youKnows[new Random().nextInt(20)]);
        builder.create().show();
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainscreenFragment())
                    .commit();
        }
        this.openOrCreateDatabase("cancerapp_database", MODE_PRIVATE, null);

        // Start shared preferences and editor.
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if( sharedPref.getInt( getString(R.string.first_pref), 1 ) == 1){
            // This will only run once, after this IF, the first screen will be MainActivity.

            String previousDate = getIntent().getStringExtra("Previous Date");

            // Setting up de reminder calculated from the user's period.
            if(previousDate != null)
                erasePreviousReminder(previousDate);
            setDefaultReminder();
            editor.putInt(getString(R.string.first_pref), 0);
            editor.commit();
        }
    }

    @Override
    protected void onResume() {
        System.gc();

        super.onResume();

        // Start shared preferences and editor.
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString( getString(R.string.temp_result_pref), "000000");
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // Start shared preferences and editor.
            SharedPreferences sharedPref = getSharedPreferences(
                    getString(R.string.preferences), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putInt(getString(R.string.first_pref), 2);
            editor.commit();

            Intent startIntent = new Intent(this, FirstTimeActivity.class);
            startActivity(startIntent);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.info_main) {
            infoBox();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openActivity(View v){
        int id = v.getId();
        Intent startIntent;

        switch(id){
            case R.id.imageView_steps:
                //Toast.makeText(this, "Steps screen.", Toast.LENGTH_SHORT).show();
                //insertTestItems();
                //resetDB();
                startIntent = new Intent(this, StepActivity.class);
                startActivity(startIntent);
                break;
            case R.id.imageView_library:
                //Toast.makeText(this, "Library screen.", Toast.LENGTH_SHORT).show();
                //insertTestItems();
                //printTestItems();
                //testCalendar();
                //resetApp();
                startIntent = new Intent(this, LibraryActivity.class);
                startActivity(startIntent);
                break;
            case R.id.imageView_doctors:
                //Toast.makeText(this, "Doctors screen.", Toast.LENGTH_SHORT).show();
                startIntent = new Intent(this, DoctorsActivity.class);
                startActivity(startIntent);
                break;
            case R.id.imageView_timeline:
                //Toast.makeText(this, "Timeline screen.", Toast.LENGTH_SHORT).show();
                startIntent = new Intent(this, TimelineActivity.class);
                startActivity(startIntent);
                break;
            case R.id.wig_view2:
                startIntent = new Intent(this, AvatarCreation.class);
                startActivity(startIntent);
                break;
            default:
                break;
        }
    }

    /**
     *  Makes the FirstTimeActivity show again on next app launch and resets the default reminder.
     */
    private void resetApp(){
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(getString(R.string.first_pref), 1);
        editor.commit();
    }

    private void erasePreviousReminder(String previousDate){
        // Start shared preferences.
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preferences), Context.MODE_PRIVATE);

        // Calculating the ideal day of the reminder.
        String reminderDay = sharedPref.getString( getString(R.string.date_pref), "");

        DatabaseHelper db = new DatabaseHelper(this);

        Log.v("Main Previous date", "PreviousDate = " + previousDate);
        Log.v("Main Current date", sharedPref.getString(getString(R.string.date_pref), ""));


        db.delete(previousDate, "Examen");
    }

    /**
     * This method calculated the ideal date to perform the self test and sets a reminder for that day.
     */
    private void setDefaultReminder(){
        // Start shared preferences.
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preferences), Context.MODE_PRIVATE);

        // Calculating the ideal day of the reminder.
        String reminderDay = sharedPref.getString( getString(R.string.date_pref), "");
        try {

            Date reminderDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(reminderDay);
            Calendar reminderCal = Calendar.getInstance();
            reminderCal.setTime(reminderDate);

            // Adding the reminder to the database.
            DatabaseHelper db = new DatabaseHelper(this);
            Reminder rem = new Reminder("Pendiente", reminderDay, "000000", "Auto-Examen mensual.", "Examen");
            // (String st, String dt, String rt, String details, String type)

            db.insert(rem);

            // Scheduling the notification.
            createScheduledNotification(reminderCal, rem);

        } catch(ParseException e){
            Log.v("Default Reminder", "Parse exception");
        }

    }

    private void testCalendar(){
        DatabaseHelper db = new DatabaseHelper(this);

        ArrayList<Reminder> arrayAll;
        arrayAll = db.retrieveByStatusAndType("Pendiente", "Examen");

        if(arrayAll != null) {
            Calendar Fcal = Calendar.getInstance();
            try {
                Date FcalDate = new SimpleDateFormat("dd-MM-yyyy").parse(arrayAll.get(0).date);
                Fcal.setTime(FcalDate);
                Log.v("Future Cal Time", new SimpleDateFormat("dd-MM-yyyy").format(Fcal.getTime()) );

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                Log.v("Calendar Time", new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime()) );
            }
            catch(ParseException e){
                Log.v("Test Calendar", "Parse exception");
            }
        }
    }

    private void resetDB(){
        DatabaseHelper db = new DatabaseHelper(this);

        db.close();
        this.deleteDatabase("cancerapp_database");
    }

    private void insertTestItems(){
        DatabaseHelper db = new DatabaseHelper(this);

        Log.v("insetTestItems", "Before");
        printAllReminders();

        db.insert(new Reminder("Saludable", "20-02-2013", "000000", "Examen")); //(String st, String dt, String rt, String type) where dt="yyyy-MM-dd HH:mm"
        db.insert( new Reminder("Saludable", "13-05-2014", "000000", "Examen") );
        db.insert( new Reminder("Saludable", "01-01-2015", "000000", "Examen") );

        db.insert( new Reminder("Anomalia", "21-03-2015", "000001", "Detalle de sintoma 1%&%", "Examen") );
        db.insert( new Reminder("Anomalia", "25-01-2014", "001001", "No hay detalles.%&%Detalle de sintoma 2.%&%", "Examen") );
        db.insert( new Reminder("Anomalia", "01-05-2013", "101010", "No hay detalles.%&%No hay detalles.%&%No hay detalles.%&%", "Examen") );

        db.insert( new Reminder("Pendiente", "25-11-2015 17:59", "000000", "Examen") );
        db.insert( new Reminder("Pendiente", "12-01-2016 14:08", "000000", "Doctor") );
        db.insert( new Reminder("Pendiente", "31-05-2017 12:54", "000000", "Otro") );

        Log.v("insetTestItems", "After");
        printAllReminders();
    }

    private void printTestItems(){
        printAllReminders();
        printSingleReminder("2014-05-13", "Examen");
        printSingleReminder("2015-03-21 00:00", "Doctor");
        printSingleReminder("2017-05-31 12:54", "Otro");
        printFutureReminders("Doctor");
        printFutureReminders("Examen");
        printFutureReminders("Otro");
    }

    public void printAllReminders(){
        DatabaseHelper db = new DatabaseHelper(this);

        ArrayList<Reminder> arrayAll;
        arrayAll = db.retrieveAllReminders();

        if(arrayAll == null ){
            Log.v("All Reminders", "It's empty.");
            return;
        }
        for(int i=0; i<arrayAll.size(); i++){
            Log.v("All Reminders", arrayAll.get(i).toString());
        }
    }

    public void printFutureReminders(String type){
        DatabaseHelper db = new DatabaseHelper(this);

        ArrayList<Reminder> arrayAll;
        arrayAll = db.retrieveByStatusAndType("Pendiente", type);

        if(arrayAll == null ){
            Log.v("Future Reminders" , "It's empty.");
            return;
        }
        for(int i=0; i<arrayAll.size(); i++){
            Log.v("Future Reminders Type " + type, arrayAll.get(i).toString());
        }
    }

    public void printSingleReminder(String date, String type){
        DatabaseHelper db = new DatabaseHelper(this);

        Reminder rem;

        rem = db.retrieve(date, type);

        if(rem != null){
            Log.v("Reminder", rem.toString());
        }
        else{
            Log.v("Reminder", "Reminder for date " + date + " and type " + type + " not found.");
        }
    }

    private void createScheduledNotification(Calendar cal, Reminder rem)
    {
        // Retrieve alarm manager from the system
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(getBaseContext().ALARM_SERVICE);

        // Every scheduled intent needs a different ID, else it is just executed once
        int id = (int) System.currentTimeMillis();

        // Prepare the intent which should be launched at the date
        Intent intent = new Intent(this, NotificationAlarm.class);
        intent.putExtra("Type", rem.type);
        intent.putExtra("Details", rem.details);

        // Prepare the pending intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Register the alert in the system. You have the option to define if the device has to wake up on the alert or not
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class MainscreenFragment extends Fragment {

        public MainscreenFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            SharedPreferences sp=getActivity().getSharedPreferences("AVATAR", Context.MODE_PRIVATE);
            SharedPreferences.Editor spe=sp.edit();
            /*spe.remove("AVATAR_WIG");
            spe.remove("AVATAR_CLOTHES");
            spe.remove("AVATAR_SHOES");
            spe.remove("AVATAR_PANTS");
            spe.remove("AVATAR_SKIN");
            spe.commit();*/
            ((ImageView)rootView.findViewById(R.id.wig_view2)).setImageResource(sp.getInt("AVATAR_WIG", R.drawable.pelo1));
            ((ImageView)rootView.findViewById(R.id.clothes_view2)).setImageResource(sp.getInt("AVATAR_CLOTHES",R.drawable.blusa1));
            ((ImageView)rootView.findViewById(R.id.shoes_view2)).setImageResource(sp.getInt("AVATAR_SHOES",R.drawable.zapatos1));
            ((ImageView)rootView.findViewById(R.id.pants_view2)).setImageResource(sp.getInt("AVATAR_PANTS", R.drawable.short1));
            ((ImageView)rootView.findViewById(R.id.body_view2)).setImageResource(sp.getInt("AVATAR_SKIN", R.drawable.munequita1));
            //ImageView steps = (ImageView)rootView.findViewById(R.id.imageView_steps);

            return rootView;
        }
    }
}
