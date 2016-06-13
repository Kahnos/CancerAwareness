package com.example.josed_000.cancerawareness;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ResultActivity extends ActionBarActivity {
    private ArrayList<String> resultStrings;
    private ArrayList<String> resultDetails;
    private ListView result_ListView;
    private int cont;
    private String results;
    private int anomaly;

    public void infoBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        builder.setCancelable(true);
        builder.setTitle(R.string.info_result_title);
        builder.setMessage(R.string.info_result);
        builder.create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        anomaly = 0;
        resultStrings = new ArrayList<String>();
        resultDetails = new ArrayList<String>();
        results = getIntent().getStringExtra("Result");

        int cont=0;
        for(int i=0; i<6; i++){
            if(results.charAt(i) == '1'){
                switch(i){
                    case 0:
                        resultStrings.add(cont, "Resultado anormal: " + getString(R.string.symptom1));
                        resultDetails.add(cont, "No hay detalles.");
                        break;
                    case 1:
                        resultStrings.add(cont, "Resultado anormal: " + getString(R.string.symptom2));
                        resultDetails.add(cont, "No hay detalles.");
                        break;
                    case 2:
                        resultStrings.add(cont, "Resultado anormal: " + getString(R.string.symptom3));
                        resultDetails.add(cont, "No hay detalles.");
                        break;
                    case 3:
                        resultStrings.add(cont, "Resultado anormal: " + getString(R.string.symptom4));
                        resultDetails.add(cont, "No hay detalles.");
                        break;
                    case 4:
                        resultStrings.add(cont, "Resultado anormal: " + getString(R.string.symptom5));
                        resultDetails.add(cont, "No hay detalles.");
                        break;
                    case 5:
                        resultStrings.add(cont, "Resultado anormal: " + getString(R.string.symptom6));
                        resultDetails.add(cont, "No hay detalles.");
                        break;
                    default:
                        break;
                }
                cont++;
                anomaly = 1;
            }
        }

        if(anomaly == 1) {
            setContentView(R.layout.activity_result_save_bad);

            //Fetching the listview layout and setting an adapter to it.
            result_ListView = (ListView) this.findViewById(R.id.result_listView);

            ResultAdapter result_Adapter = new ResultAdapter(this, resultStrings);

            result_ListView.setAdapter(result_Adapter);
        }
        else{
            setContentView(R.layout.activity_result_save_good);
        }

        SharedPreferences sp=getSharedPreferences("AVATAR", Context.MODE_PRIVATE);
        ((ImageView)findViewById(R.id.wig_view5)).setImageResource(sp.getInt("AVATAR_WIG", R.drawable.pelo1));
        ((ImageView)findViewById(R.id.clothes_view5)).setImageResource(sp.getInt("AVATAR_CLOTHES",R.drawable.blusa1));
        ((ImageView)findViewById(R.id.shoes_view5)).setImageResource(sp.getInt("AVATAR_SHOES",R.drawable.zapatos1));
        ((ImageView)findViewById(R.id.pants_view5)).setImageResource(sp.getInt("AVATAR_PANTS", R.drawable.short1));
        ((ImageView)findViewById(R.id.body_view5)).setImageResource(sp.getInt("AVATAR_SKIN", R.drawable.munequita1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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
        if (id == R.id.info_result) {
            infoBox();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Metodo para obtener una vista de un listview por su posicion.
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    // Agregar if = dia de reminder mensual, crear recordatorio
    public void onClickSave(View v){
        DatabaseHelper db = new DatabaseHelper(this);
        Reminder rem;
        int anomaly = 0;
        int cont = 0;
        StringBuilder details = new StringBuilder();

        for(int i=0; i<6; i++){
            if( results.charAt(i) == '1' ) {
                anomaly = 1;
                cont++;
            }
        }

        // Adding the results to the database.
        if (anomaly == 1) {
            //EditText detailEditText;

            Log.v("Result Inside", "Result = " + results);

            for (int i = 0; i < cont; i++) {
                /*View listItem = (View) getViewByPosition(i, result_ListView);//result_ListView.getChildAt(i);
                detailEditText = (EditText) listItem.findViewById(R.id.list_item_result_editText);

                if ( (detailEditText.getText().toString()).equals("") ) {
                    details.append("No hay detalles.");
                } else {
                    details.append( detailEditText.getText().toString() );
                }
                details.append("%&%");*/

                if ( resultDetails.get(i).equals("") || resultDetails.get(i).equals("No hay detalles.") ) {
                    details.append("No hay detalles.");
                } else {
                    details.append( resultDetails.get(i) );
                }
                details.append("%&%");
            }
        }
        else{
            details.append("No hay detalles.");
        }

        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(System.currentTimeMillis());
        String date = new SimpleDateFormat("dd-MM-yyyy").format(today.getTime());

        Log.v("Result Details String", details.toString());

        if(anomaly == 1){
            rem = new Reminder("Anomalia", date, results, details.toString(), "Examen");    //(String st, String dt, String rt, String details, String type)
        }
        else{
            rem = new Reminder("Saludable", date, results, details.toString(), "Examen");
        }

        if(db.insert(rem) == 0){
            db.update(rem.date, rem.type, rem.status, rem.result, rem.details);
        }

        Toast.makeText(this, "Resultado guardado.", Toast.LENGTH_LONG).show();
        Log.v("Result Reminder", rem.toString());

        // Start shared preferences and editor.
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(getString(R.string.temp_result_pref), "000000");
        editor.commit();

        Intent startIntent = new Intent(this, TimelineActivity.class);
        startActivity(startIntent);
    }

    public class ResultAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList<String> values;
        private EditText detailEditText;

        public ResultAdapter(Context context, ArrayList<String> values) {
            super(context, R.layout.list_item_result_save, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_item_result_save, parent, false);
            TextView textView_result = (TextView) rowView.findViewById(R.id.list_item_result_textView);

            //Setting the status.
            textView_result.setText(values.get(position));

            // Set EditText to store details in resultDetails when modified.
            detailEditText = (EditText) rowView.findViewById(R.id.list_item_result_editText);

            detailEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    resultDetails.remove(position);
                    resultDetails.add(position, s.toString());
                }
            });

            if( !resultDetails.get(position).equals("") && !resultDetails.get(position).equals("No hay detalles.") )
                detailEditText.setText( resultDetails.get(position) );

            return rowView;
        }
    }
}
