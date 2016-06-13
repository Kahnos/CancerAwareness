package com.example.josed_000.cancerawareness;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class DoctorsActivityFragment extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    public DoctorsActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_doctors, container, false);

        // get the listview
        expListView = (ExpandableListView) rootView.findViewById(R.id.Doctors_expandableListView);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        //setClickListeners(expListView);

        return rootView;
    }

    /*
     * Setting up the listeners for the expandable list items.
     */
    private void setClickListeners(ExpandableListView expListView){

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getActivity(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getActivity(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
    }

    /*
    * Preparing the list data
    */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Doctor 1");
        listDataHeader.add("Doctor 2");
        listDataHeader.add("Doctor 3");
        listDataHeader.add("Doctor 4");
        listDataHeader.add("Doctor 5");

        List<String> childData = new ArrayList<String>();
        childData.add("Número de teléfono: 0424-9090336"); //subString(20)
        childData.add("Ubicación: UCAB Guayana");   //subString(11)
        childData.add(getString(R.string.test_text_survey));

        listDataChild.put(listDataHeader.get(0), childData );
        listDataChild.put(listDataHeader.get(1), childData );
        listDataChild.put(listDataHeader.get(2), childData );
        listDataChild.put(listDataHeader.get(3), childData );
        listDataChild.put(listDataHeader.get(4), childData );
    }

    /*
    * Implementation of the custom expandable list adapter.
    */
    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<String>> _listDataChild;
        private StringBuilder time;


        public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                     HashMap<String, List<String>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
            time = new StringBuilder();
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

                    Calendar today=Calendar.getInstance();
                    today.setTimeInMillis(System.currentTimeMillis());

                    Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                            datePicker.getMonth(),
                            datePicker.getDayOfMonth(),
                            timePicker.getCurrentHour(),
                            timePicker.getCurrentMinute());

                    if( calendar.after( today ) ) {

                        time = formatTime.format(calendar.getTime());
                        Log.v("Inside Dialog Click", time);

                        Reminder rem = new Reminder("Pendiente", time, "000000", details, type);     // (String st, String dt, String rt, String details, String type)

                        if (db.update(time, type, rem.status, rem.result, rem.details) == 0) {   // (String date, String type, String new_status, String new_results, String new_details)
                            db.insert(rem);
                        }

                        createScheduledNotification(calendar, rem);

                        alertDialog.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), "Recordatorio creado.", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(), "No se puede crear un recordatorio antes de la fecha actual.", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            alertDialog.setView(dialogView);
            alertDialog.show();
        }

        private void createScheduledNotification(Calendar cal, Reminder rem)
        {
            // Retrieve alarm manager from the system
            AlarmManager alarmManager = (AlarmManager) getActivity().getApplicationContext().getSystemService(getActivity().getBaseContext().ALARM_SERVICE);

            // Every scheduled intent needs a different ID, else it is just executed once
            int id = (int) System.currentTimeMillis();

            // Prepare the intent which should be launched at the date
            Intent intent = new Intent(getActivity(), NotificationAlarm.class);
            intent.putExtra("Type", rem.type);
            intent.putExtra("Details", rem.details);

            // Prepare the pending intent
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Register the alert in the system. You have the option to define if the device has to wake up on the alert or not
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }

        private void openMapLocation(String address){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri locationUri = Uri.parse("geo:0,0?").buildUpon()
                    .appendQueryParameter("q",address)
                    .build();
            intent.setData(locationUri);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "Falla al inicial el mapa. ¿Tal vez no hay una aplicación de mapa instalada?", Toast.LENGTH_SHORT).show();
            }
        }

        public void callPhone(String phone){

            //String posted_by = "111-333-222-4";

            String uri = "tel:" + phone;//.trim() ;
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final String childText = (String) getChild(groupPosition, childPosition);

            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(!isLastChild) {
                convertView = infalInflater.inflate(R.layout.expandablelist_child_item, null);

                TextView txtListChild = (TextView) convertView
                        .findViewById(R.id.doctor_childitem_text);

                txtListChild.setText(childText);
            }
            else if(isLastChild) {

                convertView = infalInflater.inflate(R.layout.expandablelist_child_buttonsitem, null);

                Button address = (Button) convertView.findViewById(R.id.expandablelist_lastitem_address);
                address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getActivity(), "Check doctor location.", Toast.LENGTH_SHORT).show();
                        String address = (String) getChild(groupPosition, 1);
                        Log.v("Phone ", address.substring(11));
                        openMapLocation(address.substring(11));
                    }
                });

                ImageButton phone = (ImageButton) convertView.findViewById(R.id.expandablelist_lastitem_phone);
                phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getActivity(), "Make phone call.", Toast.LENGTH_SHORT).show();
                        String phone = (String) getChild(groupPosition, 0);
                        Log.v("Phone ", phone.substring(20));
                        callPhone(phone.substring(20));
                    }
                });

                ImageButton reminder = (ImageButton) convertView.findViewById(R.id.expandablelist_lastitem_reminder);
                reminder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getActivity(), "Make phone call.", Toast.LENGTH_SHORT).show();
                        createReminder(v.getContext());
                    }
                });

            }

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandablelist_group_header, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.doctor_groupheader_text);
            TextView listHeaderType= (TextView) convertView
                    .findViewById(R.id.doctor_groupheader_type);

            String headerTitle = (String) getGroup(groupPosition);

            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            listHeaderType.setText("Tipo de doctor");

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
