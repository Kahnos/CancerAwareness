package com.example.josed_000.cancerawareness;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class FirstTimeActivity extends ActionBarActivity {
    private StringBuilder dateStr;
    public String previousDate;
    public Context context;

    public void infoBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(FirstTimeActivity.this);
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        builder.setCancelable(true);
        builder.setTitle(R.string.info_first_title);
        builder.setMessage(R.string.info_first);
        builder.create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preferences), Context.MODE_PRIVATE);

        dateStr = new StringBuilder();
        dateStr.append( sharedPref.getString( getString(R.string.date_pref), "dd-MM-yyyy HH:mm" ));

        if( sharedPref.getInt( getString(R.string.first_pref), 1 ) == 0 ){
            Intent startIntent = new Intent(this, MainActivity.class);
            startActivity(startIntent);
        }
        else if( sharedPref.getInt( getString(R.string.first_pref), 1 ) == 2 ){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_first_time);

            // Start shared preferences and editor.
            sharedPref = getSharedPreferences(
                    getString(R.string.preferences), Context.MODE_PRIVATE);

            String name = sharedPref.getString( getString(R.string.name_pref), "" );

            previousDate = sharedPref.getString( getString(R.string.date_pref), "" );

            EditText nameEditText = (EditText) findViewById(R.id.name_editText);
            nameEditText.setText(name);

            return;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);

        previousDate = "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first_time, menu);
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
        if (id == R.id.info_first) {
            infoBox();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void chooseDate(View v){
        final View dialogView = View.inflate(this, R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final SimpleDateFormat formatTime = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

        Calendar cal=Calendar.getInstance();
        cal.setTimeInMillis( System.currentTimeMillis() );

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        datePicker.updateDate(year, month, day);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(min);

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time;

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

                Calendar today=Calendar.getInstance();
                today.setTimeInMillis(System.currentTimeMillis());

                if( calendar.after(today) ){
                    dateStr.replace( 0, 16, formatTime.format(calendar.getTime()) );
                    alertDialog.dismiss();
                }
                else{
                    Toast.makeText(context, "No se puede crear un recordatorio antes de la fecha actual.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    public void firstOnClick(View v){

        // Start shared preferences and editor.
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Find views.
        EditText nameEditText = (EditText) findViewById(R.id.name_editText);

        String name = nameEditText.getText().toString();

        if( (!dateStr.toString().equals("dd-MM-yyyy HH:mm")) && (!name.equals("")) ){
            int setting = sharedPref.getInt(getString(R.string.first_pref), 1);
            if(setting == 1) {
                // Edit the shared preferences.
                editor.putString( getString(R.string.date_pref), dateStr.toString() );
                editor.putString( getString(R.string.name_pref), name );
                editor.commit();

                // Start the avatar activity.
                Intent startIntent = new Intent(this, AvatarCreation.class);
                startActivity(startIntent);
            }
            else if(setting == 2){
                Intent startIntent = new Intent(this, MainActivity.class);
                startIntent.putExtra("Previous Date", previousDate);

                editor.putString( getString(R.string.date_pref), dateStr.toString() );
                editor.putString( getString(R.string.name_pref), name );
                editor.putInt( getString(R.string.first_pref), 1 );
                editor.commit();

                startActivity(startIntent);
            }
        }
        else{
            Toast.makeText(this, "Por favor escoja un nombre y una fecha para su auto-examen mensual.", Toast.LENGTH_LONG).show();
        }
    }
}
