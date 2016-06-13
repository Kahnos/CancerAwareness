package com.example.josed_000.cancerawareness;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.antonyt.infiniteviewpager.InfiniteViewPager;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import hirondelle.date4j.DateTime;


public class TimelineActivity extends ActionBarActivity {
    private CaldroidCustomFragment caldroidFragment;
    private ArrayList<Reminder> reminders;
    private final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    private Context context;
    private StringBuilder time;
    private int type;

    public void infoBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(TimelineActivity.this);
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        builder.setCancelable(true);
        builder.setTitle(R.string.info_timeline_title);
        builder.setMessage(R.string.info_timeline);
        builder.create().show();
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

    public void createReminder(final Context context){
        final View dialogView = View.inflate(context, R.layout.create_reminder_date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        final SimpleDateFormat formatTime = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

        Calendar cal=Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);
        int hour=cal.get(Calendar.HOUR_OF_DAY);
        int min=cal.get(Calendar.MINUTE);

        datePicker.updateDate(year, month, day);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(min);

        Log.v("Inside Dialog", time.toString());

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time;
                String type;
                DatabaseHelper db = new DatabaseHelper(context);

                View parent = (View) view.getParent();
                RadioGroup typeGroup = (RadioGroup) parent.findViewById(R.id.reminder_type);
                EditText detailEditText = (EditText) parent.findViewById(R.id.reminder_detail);
                       String details;
                if( detailEditText.getText().toString().equals("") ) {
                    details = "No hay detalles.";
                }
                else{
                    details = detailEditText.getText().toString();
                }

                switch (typeGroup.getCheckedRadioButtonId()) {
                    case R.id.type_doctor:
                        type = "Doctor";
                        break;
                    case R.id.type_exam:
                        type = "Examen";
                        break;
                    case R.id.type_other:
                        type = "Otro";
                        break;
                    default:
                        type = "Otro";
                        break;
                }

                Calendar today=Calendar.getInstance();
                today.setTimeInMillis(System.currentTimeMillis());

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

                if( calendar.after(today) ) {

                    time = formatTime.format(calendar.getTime());
                    Log.v("Inside Dialog Click", time.toString());

                    Reminder rem = new Reminder("Pendiente", time, "000000", details, type);     // (String st, String dt, String rt, String details, String type)

                    if (db.update(time, type, rem.status, rem.result, rem.details) == 0) {   // (String date, String type, String new_status, String new_results, String new_details)
                        db.insert(rem);
                    }

                    createScheduledNotification(calendar, rem);

                    alertDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Recordatorio creado.", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                }
                else{
                    Toast.makeText(getApplicationContext(), "No se puede crear un recordatorio antes de la fecha actual.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    public void createReminder(final Context context, String date){
        final View dialogView = View.inflate(context, R.layout.create_reminder_date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        final SimpleDateFormat formatTime = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

        Calendar cal=Calendar.getInstance();
        try {
            cal.setTime(format.parse(date));

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int min = cal.get(Calendar.MINUTE);

            datePicker.updateDate(year, month, day);
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(min);
        }catch(ParseException e){
            Log.v("Inside Dialog", "Parse exception.");
        }

        Log.v("Inside Dialog", time.toString());

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time;
                String type;
                DatabaseHelper db = new DatabaseHelper(context);

                View parent = (View) view.getParent();
                RadioGroup typeGroup = (RadioGroup) parent.findViewById(R.id.reminder_type);
                EditText detailEditText = (EditText) parent.findViewById(R.id.reminder_detail);
                String details;
                if (detailEditText.getText().toString().equals("")) {
                    details = "No hay detalles.";
                } else {
                    details = detailEditText.getText().toString();
                }


                switch (typeGroup.getCheckedRadioButtonId()) {
                    case R.id.type_doctor:
                        type = "Doctor";
                        break;
                    case R.id.type_exam:
                        type = "Examen";
                        break;
                    case R.id.type_other:
                        type = "Otro";
                        break;
                    default:
                        type = "Otro";
                        break;
                }

                Calendar today = Calendar.getInstance();
                today.setTimeInMillis(System.currentTimeMillis());

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

                if (calendar.after(today)) {

                    time = formatTime.format(calendar.getTime());
                    Log.v("Inside Dialog Click", time.toString());

                    Reminder rem = new Reminder("Pendiente", time, "000000", details, type);     // (String st, String dt, String rt, String details, String type)

                    if (db.update(time, type, rem.status, rem.result, rem.details) == 0) {   // (String date, String type, String new_status, String new_results, String new_details)
                        db.insert(rem);
                    }

                    createScheduledNotification(calendar, rem);

                    alertDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Recordatorio creado.", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                } else {
                    Toast.makeText(getApplicationContext(), "No se puede crear un recordatorio antes de la fecha actual.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    // Se eliminan los recordatorios ya han pasado dos días desde su fecha de notificación.
    public void deleteOldReminders(){
        DatabaseHelper db = new DatabaseHelper(this);

        Calendar todayMinus2 = Calendar.getInstance();
        todayMinus2.setTimeInMillis(System.currentTimeMillis());
        todayMinus2.add(Calendar.DAY_OF_YEAR, -2);

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        SimpleDateFormat formatExam = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Reminder rem;
            Date remDate;
            for (int i = 0; i < reminders.size(); i++) {
                rem = reminders.get(i);
                // Si son recordatorios, se verificarán.
                if( rem.status.equals("Pendiente") ) {
                    if ( rem.type.equals("Examen") ) {
                        remDate = formatExam.parse(rem.date);
                        if (remDate.before(todayMinus2.getTime())) {
                            db.delete(rem.date, rem.type);
                        }
                    } else {
                        remDate = format.parse(rem.date);
                        if (remDate.before(todayMinus2.getTime())) {
                            db.delete(rem.date, rem.type);
                        }
                    }
                }
            }

            reminders = db.retrieveAllReminders();
        }catch(ParseException e){
            Log.v("Erasing old reminders", "Parse exception");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        context = this;
        DatabaseHelper db = new DatabaseHelper(this);
        reminders = db.retrieveAllReminders();

        deleteOldReminders();

        time = new StringBuilder();
        type = 0;

        if (savedInstanceState == null) {
            //Creating the CaldroidFragment and its arguments for the Calendar inside Timeline screen.
            caldroidFragment = new CaldroidCustomFragment();
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            caldroidFragment.setArguments(args);

            //  Draw event dates on calendar.
            if(reminders != null) {
                Collections.sort(reminders);
                setCustomResourceForDates(reminders);
            }

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.FrameLayout_Timeline, new TimelineFragment())
                    .add(R.id.FrameLayout_Calendar, caldroidFragment)
                    .commit();

            // Setup listener.
            final CaldroidListener listener = new CaldroidListener() {

                @Override
                public void onSelectDate(Date date, View view) {
                    //Toast.makeText(getApplicationContext(), format.format(date), Toast.LENGTH_SHORT).show();
                    DatabaseHelper db = new DatabaseHelper( context );
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(System.currentTimeMillis());
                    String dateStr = format.format(date);

                    if( date.after( cal.getTime() ) ){
                        //Toast.makeText(getApplicationContext(), "Open reminder dialog", Toast.LENGTH_SHORT).show();
                        createReminder( context, dateStr );
                    }
                    else if( date.before( cal.getTime()) ){
                        Reminder rem = db.retrieve( dateStr, "Examen");

                        if( rem != null ) {
                            if (rem.status.equals("Anomalia")) {
                                Intent startIntent = new Intent(context, ResultActivityGet.class);
                                startIntent.putExtra("Details", rem.details);
                                startIntent.putExtra("Date", rem.date);
                                startIntent.putExtra("Result", rem.result);

                                startActivity(startIntent);
                            }
                        }
                    }

                }

                /*
                @Override
                public void onChangeMonth(int month, int year) {
                    String text = "month: " + month + " year: " + year;
                    Toast.makeText(getApplicationContext(), text,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLongClickDate(Date date, View view) {
                    Toast.makeText(getApplicationContext(),
                            "Long click " + format.format(date),
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCaldroidViewCreated() {
                    if (caldroidFragment.getLeftArrowButton() != null) {
                        Toast.makeText(getApplicationContext(),
                                "Caldroid view is created", Toast.LENGTH_SHORT)
                                .show();
                    }
                }*/

            };

            // Setup Caldroid
            caldroidFragment.setCaldroidListener(listener);
        }

    }

    public void setCustomResourceForDates(ArrayList<Reminder> reminders) {

        ArrayList<Date> dates = new ArrayList<Date>();
        ArrayList<String> dateStrings = new ArrayList<String>();
        ArrayList<StringBuilder> calendarColors = new ArrayList<StringBuilder>();

        StringBuilder dateStr = new StringBuilder("dd-MM-yyyy");
        StringBuilder calDate = new StringBuilder("dd-MM-yyyy");
        int cont = 0;
        int resultFlag = 0; // Determina si la fecha contiene un resultado.

        if(caldroidFragment != null) {

            dateStrings.add(0, reminders.get(0).date.substring(0, 10));
            try {
                for (int i = 0; i < reminders.size(); i++) {
                    // Se inician los auxiliares a utilizar en cada ciclo.
                    if( i == 0 )
                        dateStrings.add(reminders.get(0).date.substring(0, 10));
                    else
                        dateStrings.add(i, reminders.get(i).date.substring(0, 10));
                    dateStr.replace(0, 10, dateStrings.get(i));

                    if( i == 0 ) {
                        // Se coloca el primer dato de calendarColors y dates.
                        calendarColors.add(new StringBuilder(dateStrings.get(0) + "00000"));
                        dates.add(format.parse(reminders.get(i).date));
                    }

                    calDate.replace(0, 10, calendarColors.get(cont).substring(0, 10));

                    // Se aumenta el contador para una fecha diferente si es necesario.
                    if (!(dateStr.toString().equals(calDate.toString()))) {
                        cont++;
                        dates.add(cont, format.parse(reminders.get(i).date));
                        calendarColors.add(cont, new StringBuilder(dateStr + "00000"));
                        resultFlag = 0;
                    }

                    // Se modifica calendarColors dependiendo del tipo de recordatorios que contenga cada fecha.
                    /**
                     * Nota: La codificación es la siguiente:
                     * Quinto bit(10000): Saludable
                     * Cuarto bit(01000): Anomalia
                     * Tercer bit(00100): Pendiente - Examen
                     * Segundo bit(00010}: Pendiente - Doctor
                     * Primer bit(00001): Pendiente - Otro
                     *
                     * Se permitirán las combinaciones entre recordatorios pendientes.
                     */
                    switch (reminders.get(i).type) {
                        case "Examen":
                            if( reminders.get(i).status.equals("Saludable") ) {
                                calendarColors.get(cont).setCharAt(10, '1');
                                resultFlag = 1;
                            }
                            else if( reminders.get(i).status.equals("Anomalia") ) {
                                calendarColors.get(cont).setCharAt(11, '1');
                                resultFlag = 1;
                            }
                            else if( reminders.get(i).status.equals("Pendiente") )
                                if( resultFlag == 0 )
                                    calendarColors.get(cont).setCharAt(12, '1');
                            break;

                        case "Doctor":
                            if( resultFlag == 0 )
                                calendarColors.get(cont).setCharAt(13, '1');
                            break;

                        case "Otro":
                            if( resultFlag == 0 )
                                calendarColors.get(cont).setCharAt(14, '1');
                            break;
                    }
                }
            }
            catch (ParseException e){
                Log.v("setCustomResources", "Parse exception.");
            }

            // Modificando el calendario en base a calendarColors.
            for(int i=0; i<calendarColors.size(); i++){
                switch ( calendarColors.get(i).toString().substring(10) ){
                    case "10000":
                        caldroidFragment.setBackgroundResourceForDate(R.color.green, dates.get(i));
                        caldroidFragment.setTextColorForDate(R.color.white, dates.get(i));
                        break;

                    case "01000":
                        caldroidFragment.setBackgroundResourceForDate(R.color.red, dates.get(i));
                        caldroidFragment.setTextColorForDate(R.color.white, dates.get(i));
                        break;

                    case "00001":
                        caldroidFragment.setBackgroundResourceForDate(R.color.purple, dates.get(i));
                        caldroidFragment.setTextColorForDate(R.color.white, dates.get(i));
                        break;

                    case "00010":
                        caldroidFragment.setBackgroundResourceForDate(R.color.blue, dates.get(i));
                        caldroidFragment.setTextColorForDate(R.color.white, dates.get(i));
                        break;

                    case "00011":
                        caldroidFragment.setBackgroundResourceForDate(R.drawable.cal_purple_blue, dates.get(i));
                        caldroidFragment.setTextColorForDate(R.color.white, dates.get(i));
                        break;

                    case "00100":
                        caldroidFragment.setBackgroundResourceForDate(R.color.pink_reminder, dates.get(i));
                        caldroidFragment.setTextColorForDate(R.color.white, dates.get(i));
                        break;

                    case "00101":
                        caldroidFragment.setBackgroundResourceForDate(R.drawable.cal_purple_pink, dates.get(i));
                        caldroidFragment.setTextColorForDate(R.color.white, dates.get(i));
                        break;

                    case "00110":
                        caldroidFragment.setBackgroundResourceForDate(R.drawable.cal_blue_pink, dates.get(i));
                        caldroidFragment.setTextColorForDate(R.color.white, dates.get(i));
                        break;

                    case "00111":
                        caldroidFragment.setBackgroundResourceForDate(R.drawable.cal_all, dates.get(i));
                        caldroidFragment.setTextColorForDate(R.color.white, dates.get(i));
                        break;

                    default:
                        Log.v("CALENDARCOLORS", "string:" + calendarColors.get(i).toString() + "substring: " + calendarColors.get(i).toString().substring(10));
                        caldroidFragment.setBackgroundResourceForDate(R.color.black, dates.get(i));
                        caldroidFragment.setTextColorForDate(R.color.white, dates.get(i));
                        break;
                }
            }
        }

/*        if (caldroidFragment != null) {
            for (int i = 0; i < reminders.size(); i++) {
                // Change icon according to status.
                if (reminders.get(i).status.equals("Saludable")) {

                    caldroidFragment.setBackgroundResourceForDate(R.color.green, dates.get(i));
                    caldroidFragment.setTextColorForDate(R.color.white, dates.get(i));

                } else if (reminders.get(i).status.equals("Anomalia")) {

                    caldroidFragment.setBackgroundResourceForDate(R.color.red, dates.get(i));
                    caldroidFragment.setTextColorForDate(R.color.white, dates.get(i));

                } else if (reminders.get(i).status.equals("Pendiente")) {

                    switch (reminders.get(i).type) {
                        case "Examen":
                            caldroidFragment.setBackgroundResourceForDate(R.color.pink_reminder, dates.get(i));
                            caldroidFragment.setTextColorForDate(R.color.white, dates.get(i));
                            break;

                        case "Doctor":
                            caldroidFragment.setBackgroundResourceForDate(R.color.blue, dates.get(i));
                            caldroidFragment.setTextColorForDate(R.color.white, dates.get(i));
                            break;

                        case "Otro":
                            caldroidFragment.setBackgroundResourceForDate(R.color.purple, dates.get(i));
                            caldroidFragment.setTextColorForDate(R.color.white, dates.get(i));
                            break;
                    }

                }
            }
        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
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
        if (id == R.id.info_timeline) {
            infoBox();
            return true;
        }
        if (id == R.id.add_timeline) {
            createReminder(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class TimelineFragment extends Fragment {

        public TimelineFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

            //Fetching the listview layout and setting an adapter to it.
            ListView timeline_ListView = (ListView) rootView.findViewById(R.id.Timeline_ListView);

            DatabaseHelper db = new DatabaseHelper(getActivity());

            ArrayList<Reminder> reminders = db.retrieveAllReminders();

            if(reminders != null) {
                // Sorting the remiders by date in ascending order.
                Collections.sort(reminders);

                TimelineAdapter timeline_Adapter = new TimelineAdapter(getActivity(), reminders);

                timeline_ListView.setAdapter(timeline_Adapter);
            }

            timeline_ListView.setItemsCanFocus(true);

            return rootView;
        }

        public class TimelineAdapter extends ArrayAdapter<Reminder> {
            private final Context context;
            private final ArrayList<Reminder> values;

            public TimelineAdapter(Context context, ArrayList<Reminder> values) {
                super(context, R.layout.list_item_timeline, values);
                this.context = context;
                this.values = values;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                String status = values.get(position).status;
                String date = values.get(position).date;
                String details = values.get(position).details;
                String type = values.get(position).type;

                View rowView;

                if( status.equals("Pendiente") ){
                    rowView = inflater.inflate(R.layout.list_item_timeline_future, parent, false);
                    TextView textView_detail = (TextView) rowView.findViewById(R.id.list_item_timeline_detail);

                    //Setting the details.
                    textView_detail.setText( "Detalles: " + details );
                }
                else{
                    rowView = inflater.inflate(R.layout.list_item_timeline, parent, false);
                }

                TextView textView_status = (TextView) rowView.findViewById(R.id.list_item_timeline_status);
                TextView textView_date = (TextView) rowView.findViewById(R.id.list_item_timeline_date);
                ImageView imageView = (ImageView) rowView.findViewById(R.id.list_item_timeline_thumbnail);

                // Setting the date.
                textView_date.setText( date );

                // Change icon according to status.
                if ( status.equals("Saludable") ) {

                    textView_status.setText( status );
                    imageView.setImageResource(R.drawable.green_check);

                } else if ( status.equals("Anomalia") ) {

                    textView_status.setText( getString(R.string.anomalia) );
                    imageView.setImageResource(R.drawable.red_exclamation);

                } else if ( status.equals("Pendiente") ) {

                    switch(type){
                        case "Examen":
                            // Setting the status.
                            textView_status.setText( "Recordatorio" + ": Auto-Examen" );

                            // Setting the icon.
                            imageView.setImageResource(R.drawable.calendar_pink_examen);
                            break;

                        case "Doctor":
                            // Setting the status.
                            textView_status.setText( "Recordatorio" + getString(R.string.cita_medica) );

                            // Setting the icon.
                            imageView.setImageResource(R.drawable.calendar_blue_doctor);
                            break;

                        case "Otro":
                            // Setting the status.
                            textView_status.setText( "Recordatorio"  );

                            // Setting the icon.
                            imageView.setImageResource(R.drawable.calendar_purple_otro);
                            break;
                    }

                }

                ImageButton deleteButton = (ImageButton) rowView.findViewById(R.id.list_item_timeline_delete);

                // CAMBIAR no dejar borrar si es el recordatorio default - Eliminar recordatorios antes de momento actual + 2
                //  Setting the delete button onClick to erase reminder from database and list.
                deleteButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        DatabaseHelper db = new DatabaseHelper(getActivity());

                        // Start shared preferences and editor.
                        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                                getString(R.string.preferences), Context.MODE_PRIVATE);

                        String defaultDate = sharedPref.getString( getString(R.string.date_pref), "");

                        String dateString = values.get(position).date;
                        Log.v("inside On Delete", "Date = " + dateString);

                        String type = values.get(position).type;
                        Log.v("inside On Delete", "Type = " + type);

                        // Forbid erasing the default monthly reminder.
                        if( dateString.equals(defaultDate) && type.equals("Examen") ){
                            Toast.makeText(context, "El recordatorio mensual no puede ser eliminado.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // Updating the adapter (deleting by position).
                            values.remove(position);
                            notifyDataSetChanged();

                            // Delete entry from database.
                            db.delete(dateString, type);

                            Toast.makeText(context, "Recordatorio eliminado.", Toast.LENGTH_SHORT).show();

                            getActivity().finish();
                            startActivity(getActivity().getIntent());
                        }

                    }
                });

                // Setting the view ClickListener.
                rowView.setClickable(true);
                rowView.setFocusable(true);
                rowView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Reminder reminder = values.get(position);
                        String date = reminder.date;
                        String result = reminder.result;
                        String details = reminder.details;
                        String status = reminder.status;

                        if( status.equals("Anomalia") ){

                            Intent startIntent = new Intent(getActivity(), ResultActivityGet.class);
                            startIntent.putExtra("Details", details);
                            startIntent.putExtra("Date", date);
                            startIntent.putExtra("Result", result);

                            startActivity(startIntent);

                        }
                    }

                });


                return rowView;
            }

        }

    }

    // Caldroid custom set-up.

    // Caldroid Custom Adapter
    public class CaldroidCustomAdapter extends CaldroidGridAdapter {

        public CaldroidCustomAdapter(Context context, int month, int year,
                                     HashMap<String, Object> caldroidData,
                                     HashMap<String, Object> extraData) {
            super(context, month, year, caldroidData, extraData);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View cellView = convertView;

            // For reuse
            if (convertView == null) {
                cellView = inflater.inflate(R.layout.normal_date_cell, null);
            }

            int topPadding = cellView.getPaddingTop();
            int leftPadding = cellView.getPaddingLeft();
            int bottomPadding = cellView.getPaddingBottom();
            int rightPadding = cellView.getPaddingRight();

            TextView tv1 = (TextView) cellView.findViewById(R.id.calendar_tv);

            tv1.setTextColor(Color.BLACK);

            // Get dateTime of this cell
            DateTime dateTime = this.datetimeList.get(position);
            Resources resources = context.getResources();

            // Set color of the dates in previous / next month
            if (dateTime.getMonth() != month) {
                tv1.setTextColor(resources
                        .getColor(com.caldroid.R.color.caldroid_darker_gray));
            }

            boolean shouldResetDiabledView = false;
            boolean shouldResetSelectedView = false;

            // Customize for disabled dates and date outside min/max dates
            if ((minDateTime != null && dateTime.lt(minDateTime))
                    || (maxDateTime != null && dateTime.gt(maxDateTime))
                    || (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

                tv1.setTextColor(CaldroidFragment.disabledTextColor);
                if (CaldroidFragment.disabledBackgroundDrawable == -1) {
                    cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
                } else {
                    cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
                }

                if (dateTime.equals(getToday())) {
                    cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
                }

            } else {
                shouldResetDiabledView = true;
            }

            // Customize for selected dates
            if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
                cellView.setBackgroundColor(resources
                        .getColor(com.caldroid.R.color.caldroid_sky_blue));

                tv1.setTextColor(Color.BLACK);

            } else {
                shouldResetSelectedView = true;
            }

            if (shouldResetDiabledView && shouldResetSelectedView) {
                // Customize for today
                if (dateTime.equals(getToday())) {
                    cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
                } else {
                    cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
                }
            }

            tv1.setText("" + dateTime.getDay());

            // Somehow after setBackgroundResource, the padding collapse.
            // This is to recover the padding
            cellView.setPadding(leftPadding, topPadding, rightPadding,
                    bottomPadding);

            // Set custom color if required
            setCustomResources(dateTime, cellView, tv1);

            InfiniteViewPager pager = (InfiniteViewPager) findViewById(R.id.months_infinite_pager);
            int height = pager.getHeight() / 6;

            cellView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, height));

            return cellView;
        }
    }

    @SuppressLint("ValidFragment")
    public class CaldroidCustomFragment extends CaldroidFragment {

        @Override
        public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
            // TODO Auto-generated method stub
            return new CaldroidCustomAdapter(getActivity(), month, year,
                    getCaldroidData(), extraData);
        }

    }

}