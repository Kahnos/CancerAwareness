package com.example.josed_000.cancerawareness;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by JD on 01/05/2015.
 */

// The class has to extend the BroadcastReceiver to get the notification from the system
public class NotificationAlarm extends BroadcastReceiver {

    private static final int DOCTOR_NOTIFICATION_ID = 1;
    private static final int EXAM_NOTIFICATION_ID = 2;
    private static final int OTHER_NOTIFICATION_ID = 3;
    private Context actContext;

    @Override
    public void onReceive(Context context, Intent paramIntent) {
        actContext = context;

        //Log.d("onReceiveAfterBoot", "Outside if." + paramIntent.getAction());

        if( ("android.intent.action.BOOT_COMPLETED").equals( paramIntent.getAction() ) ) {
            //Log.d("onReceiveAfterBoot", "Inside if.");

            // Retrieves the next pending notification from the local database.
            DatabaseHelper db = new DatabaseHelper(context);

            try {
                // Start of process to reset the default check-up on the next month.
                Calendar today = Calendar.getInstance().getInstance();

                // Start shared preferences and editor.
                SharedPreferences sharedPref = context.getSharedPreferences(
                        context.getString(R.string.preferences), Context.MODE_PRIVATE);

                String defaultDateStr = sharedPref.getString(context.getString(R.string.date_pref),
                        new SimpleDateFormat("dd-MM-yyyy HH:mm").format(today.getTime()));

                Date defaultDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(defaultDateStr);

                // Si la fecha del autoexamen ya pasó, se aumenta 1 mes.
                if( defaultDate.before( today.getTime() ) ){
                    Calendar nextDate = Calendar.getInstance();
                    nextDate.setTime(defaultDate);
                    nextDate.add(Calendar.MONTH, 1);

                    Log.v("At boot enters reset", "Before: " + defaultDateStr + " After: "
                            + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(nextDate.getTime()) );

                    String nextReminder = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(nextDate.getTime());

                    db.delete(defaultDateStr, "Examen");

                    Reminder rem = new Reminder("Pendiente", nextReminder, "000000", "Auto-Examen mensual.", "Examen");

                    db.insert(rem);
                }

                // Retrieve alarm manager from the system
                AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(context.ALARM_SERVICE);

                // Creacion del proximo recordatorio de tipo doctor.
                ArrayList<Reminder> arrayPendingDoctor;
                arrayPendingDoctor = db.retrieveByStatusAndType("Pendiente", "Doctor");
                if(arrayPendingDoctor != null) {
                    Date doctorDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(arrayPendingDoctor.get(0).date);

                    Calendar calendarDoctor = Calendar.getInstance();
                    calendarDoctor.setTime(doctorDate);

                    // Prepare the doctor intent which should be launched at the date
                    Intent doctorIntent = new Intent(context, NotificationAlarm.class);
                    doctorIntent.putExtra("Type", "Doctor");

                    // Every scheduled intent needs a different ID, else it is just executed once
                    int idDoctor = (int) System.currentTimeMillis();

                    // Prepare the pending intent
                    PendingIntent pendingDoctor = PendingIntent.getBroadcast(context.getApplicationContext(), idDoctor, doctorIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    // Register the alert in the system. You have the option to define if the device has to wake up on the alert or not
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendarDoctor.getTimeInMillis(), pendingDoctor);
                }

                // Creacion del proximo recordatorio de tipo examen.
                ArrayList<Reminder> arrayPendingExamen;
                arrayPendingExamen = db.retrieveByStatusAndType("Pendiente", "Examen");
                if(arrayPendingExamen != null) {
                    Date examenDate = new SimpleDateFormat("dd-MM-yyyy").parse(arrayPendingExamen.get(0).date);

                    Calendar calendarExamen = Calendar.getInstance();
                    calendarExamen.setTime(examenDate);

                    // Prepare the examen intent which should be launched at the date
                    Intent examenIntent = new Intent(context, NotificationAlarm.class);
                    examenIntent.putExtra("Type", "Examen");

                    // Every scheduled intent needs a different ID, else it is just executed once
                    int idExamen = (int) System.currentTimeMillis() + 5;

                    // Prepare the pending intent
                    PendingIntent pendingExamen = PendingIntent.getBroadcast(context.getApplicationContext(), idExamen, examenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    // Register the alert in the system. You have the option to define if the device has to wake up on the alert or not
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendarExamen.getTimeInMillis(), pendingExamen);
                }

                // Creacion del proximo recordatorio de tipo otro.
                ArrayList<Reminder> arrayPendingOtro;
                arrayPendingOtro = db.retrieveByStatusAndType("Pendiente", "Otro");
                if(arrayPendingOtro != null) {
                    Date otroDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(arrayPendingOtro.get(0).date);

                    Calendar calendarOtro = Calendar.getInstance();
                    calendarOtro.setTime(otroDate);

                    // Prepare the otro intent which should be launched at the date
                    Intent otroIntent = new Intent(context, NotificationAlarm.class);
                    otroIntent.putExtra("Type", "Otro");

                    // Every scheduled intent needs a different ID, else it is just executed once
                    int idOtro = (int) System.currentTimeMillis() + 10;

                    // Prepare the pending intent
                    PendingIntent pendingOtro = PendingIntent.getBroadcast(context.getApplicationContext(), idOtro, otroIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    // Register the alert in the system. You have the option to define if the device has to wake up on the alert or not
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendarOtro.getTimeInMillis(), pendingOtro);
                }
            }
            catch (ParseException e){
                Log.v("Notification Alarm", "Parse exception");
            }

            return;
        }
        // Request the notification manager.
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String type = paramIntent.getStringExtra("Type");
        String details = paramIntent.getStringExtra("Details");
        if(details == null){
            details = "No hay detalles.";
        }
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder notificationBuilder;
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        Bitmap largeIcon;

        switch(type){
            case "Doctor":
                // Create a new intent which will be fired if you click on the notification
                intent = new Intent(context, TimelineActivity.class);

                // Attach the intent to a pending intent
                pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // Create the notification
                largeIcon = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.calendar_blue_doctor);

                /*bigTextStyle.setBigContentTitle( context.getString(R.string.cita_medica_reminder) );
                bigTextStyle.bigText("Detalles: " + details);*/

                notificationBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.calendarwhite)
                                .setLargeIcon( Bitmap.createScaledBitmap(largeIcon, 64, 64, false) )
                                //.setStyle(bigTextStyle)
                                .setContentIntent(pendingIntent)
                                .setContentTitle( context.getString(R.string.cita_medica_reminder) )
                                .setContentText( "Detalles: " + details );

                // Fire the notification
                notificationManager.notify(DOCTOR_NOTIFICATION_ID, notificationBuilder.build());
                break;

            case "Examen":
                // Create a new intent which will be fired if you click on the notification
                intent = new Intent(context, StepActivity.class);

                // Attach the intent to a pending intent
                pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // Create the notification
                largeIcon = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.calendar_pink_examen);

                /*bigTextStyle.setBigContentTitle( "Recordatorio de auto-examen" );
                bigTextStyle.bigText("Detalles: " + details);*/

                notificationBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.calendarwhite)
                                .setLargeIcon( Bitmap.createScaledBitmap(largeIcon, 64, 64, false) )
                                //.setStyle(bigTextStyle)
                                .setContentIntent(pendingIntent)
                                .setContentTitle("Recordatorio de auto-examen")
                                .setContentText("Detalles: " + details);

                // Fire the notification
                notificationManager.notify(EXAM_NOTIFICATION_ID, notificationBuilder.build());
                break;

            case "Otro":
                // Create a new intent which will be fired if you click on the notification
                intent = new Intent(context, TimelineActivity.class);

                // Attach the intent to a pending intent
                pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // Create the notification
                largeIcon = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.calendar_purple_otro);

                /*bigTextStyle.setBigContentTitle( "Recordatorio personalizado" );
                bigTextStyle.bigText("Detalles: " + details);*/

                notificationBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.calendarwhite)
                                .setLargeIcon( Bitmap.createScaledBitmap(largeIcon, 64, 64, false) )
                                //.setStyle(bigTextStyle)
                                .setContentIntent(pendingIntent)
                                .setContentTitle("Recordatorio personalizado")
                                .setContentText("Detalles: " + details);

                // Fire the notification
                notificationManager.notify(OTHER_NOTIFICATION_ID, notificationBuilder.build());
                break;
        }
    }

}
