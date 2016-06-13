package com.example.josed_000.cancerawareness;

import android.app.AlertDialog;
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
import android.widget.CheckBox;

import java.util.ArrayList;


public class SurveyActivity extends ActionBarActivity {

    public void infoBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SurveyActivity.this);
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        builder.setCancelable(true);
        builder.setTitle(R.string.info_survey_title);
        builder.setMessage(R.string.info_survey);
        builder.create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SurveyFragment())
                    .commit();
        }
        setTitle("SurveyActivity");

    }

    @Override
    public void onBackPressed() {
        // Write your code here
        ArrayList<CheckBox> boxes = new ArrayList<CheckBox>();
        boxes.add(0, (CheckBox) findViewById(R.id.checkBox1));
        boxes.add(1, (CheckBox) findViewById(R.id.checkBox2));
        boxes.add(2, (CheckBox) findViewById(R.id.checkBox3));
        boxes.add(3, (CheckBox) findViewById(R.id.checkBox4));
        boxes.add(4, (CheckBox) findViewById(R.id.checkBox5));
        boxes.add(5, (CheckBox) findViewById(R.id.checkBox6));

        //String result = "";
        StringBuffer result = new StringBuffer();

        for(int i=0; i<6; i++){
            if( boxes.get(i).isChecked() ) {
                //result += "1";
                result.append('1');
            }
            else {
                //result += "0";
                result.append('0');
            }
        }

        // Start shared preferences and editor.
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString( getString(R.string.temp_result_pref), result.toString() );
        editor.commit();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_survey, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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
        if (id == R.id.info_survey) {
            infoBox();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
        ArrayList<CheckBox> boxes = new ArrayList<CheckBox>();
        boxes.add(0, (CheckBox) findViewById(R.id.checkBox1));
        boxes.add(1, (CheckBox) findViewById(R.id.checkBox2));
        boxes.add(2, (CheckBox) findViewById(R.id.checkBox3));
        boxes.add(3, (CheckBox) findViewById(R.id.checkBox4));
        boxes.add(4, (CheckBox) findViewById(R.id.checkBox5));
        boxes.add(5, (CheckBox) findViewById(R.id.checkBox6));

        //String result = "";
        StringBuffer result = new StringBuffer();

        for(int i=0; i<6; i++){
            if( boxes.get(i).isChecked() ) {
                //result += "1";
                result.append('1');
            }
            else {
                //result += "0";
                result.append('0');
            }
        }

        //Log.v("Results", result);
        Intent startIntent = new Intent(this, ResultActivity.class);
        startIntent.putExtra("Result", result.toString());
        startActivity(startIntent);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class SurveyFragment extends Fragment {

        public SurveyFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_survey, container, false);

            // Start shared preferences and editor.
            SharedPreferences sharedPref = getActivity().getSharedPreferences(
                    getString(R.string.preferences), Context.MODE_PRIVATE);

            String startResults = sharedPref.getString( getString(R.string.temp_result_pref), "000000");
            Log.v("Start results", startResults);

            ArrayList<CheckBox> boxes = new ArrayList<CheckBox>();
            boxes.add(0, (CheckBox) rootView.findViewById(R.id.checkBox1));
            boxes.add(1, (CheckBox) rootView.findViewById(R.id.checkBox2));
            boxes.add(2, (CheckBox) rootView.findViewById(R.id.checkBox3));
            boxes.add(3, (CheckBox) rootView.findViewById(R.id.checkBox4));
            boxes.add(4, (CheckBox) rootView.findViewById(R.id.checkBox5));
            boxes.add(5, (CheckBox) rootView.findViewById(R.id.checkBox6));

            for(int i=0; i<6; i++){
                CheckBox box = boxes.get(i);
                assert startResults != null;
                if( startResults.charAt(i) == '1' )
                    box.setChecked(true);
            }

            return rootView;
        }
    }
}