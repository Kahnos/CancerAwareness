package com.example.josed_000.cancerawareness;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ResultActivityGet extends ActionBarActivity {
    protected String date;
    protected String results;
    protected String details;
    protected ArrayList<String> resultStrings;

    public void infoBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivityGet.this);
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resultStrings = new ArrayList<>();

        setContentView(R.layout.activity_result_get);

        date = getIntent().getStringExtra("Date");

        TextView result_Date = (TextView) this.findViewById(R.id.result_date);
        result_Date.setText(date);

        results = getIntent().getStringExtra("Result");
        details = getIntent().getStringExtra("Details");

        // Building the result to show.
        int cont=0;
        for(int i=0; i<6; i++){
            if(results.charAt(i) == '1'){
                switch(i){
                    case 0:
                        resultStrings.add(cont, "Resultado anormal: " + getString(R.string.symptom1));
                        break;
                    case 1:
                        resultStrings.add(cont, "Resultado anormal: " + getString(R.string.symptom2));
                        break;
                    case 2:
                        resultStrings.add(cont, "Resultado anormal: " + getString(R.string.symptom3));
                        break;
                    case 3:
                        resultStrings.add(cont, "Resultado anormal: " + getString(R.string.symptom4));
                        break;
                    case 4:
                        resultStrings.add(cont, "Resultado anormal: " + getString(R.string.symptom5));
                        break;
                    case 5:
                        resultStrings.add(cont, "Resultado anormal: " + getString(R.string.symptom6));
                        break;
                    default:
                        break;
                }
                cont++;
            }
        }

        //Fetching the listview layout and setting an adapter to it.
        ListView result_ListView = (ListView) this.findViewById(R.id.result_listView);

        ResultAdapter result_Adapter = new ResultAdapter(this, resultStrings);
        result_ListView.setAdapter(result_Adapter);

        SharedPreferences sp=getSharedPreferences("AVATAR", Context.MODE_PRIVATE);
        ((ImageView)findViewById(R.id.wig_view3)).setImageResource(sp.getInt("AVATAR_WIG", R.drawable.pelo1));
        ((ImageView)findViewById(R.id.clothes_view3)).setImageResource(sp.getInt("AVATAR_CLOTHES",R.drawable.blusa1));
        ((ImageView)findViewById(R.id.shoes_view3)).setImageResource(sp.getInt("AVATAR_SHOES",R.drawable.zapatos1));
        ((ImageView)findViewById(R.id.pants_view3)).setImageResource(sp.getInt("AVATAR_PANTS", R.drawable.short1));
        ((ImageView)findViewById(R.id.body_view3)).setImageResource(sp.getInt("AVATAR_SKIN", R.drawable.munequita1));

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.info_result) {
            infoBox();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ResultAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList<String> values;
        private ArrayList<String> detailsList;

        public ResultAdapter(Context context, ArrayList<String> values) {
            super(context, R.layout.list_item_result_get, values);
            this.context = context;
            this.values = values;
            detailsList = new ArrayList<>();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_item_result_get, parent, false);

            TextView textView_result = (TextView) rowView.findViewById(R.id.list_item_result_textView);
            TextView textView_result_detail = (TextView) rowView.findViewById(R.id.list_item_result_get_textView);

            //Setting the result.
            textView_result.setText(values.get(position));

            //Log.v("Details inside RGET", details);

            if( details.indexOf("%&%") != -1 ){
                detailsList = formatDetails(details);

                //Setting the result.
                textView_result_detail.setText( detailsList.get(position) );
            }
            else{
                textView_result_detail.setText( details );
            }

            return rowView;
        }

        public ArrayList<String> formatDetails(String details){
            ArrayList<String> formattedDetails = new ArrayList<>();
            int auxBegin = 0;
            int auxEnd = 0;

            for(int i=0; i<values.size(); i++){
                auxEnd = details.indexOf("%&%", auxBegin);

                formattedDetails.add( details.substring(auxBegin, auxEnd) );    //CHECK

                auxBegin = auxEnd + 3;
                if(auxBegin >= (details.length() - 1) )
                    break;
            }

            /*for(int i=0; i<formattedDetails.size(); i++)
                Log.v("Formatted Details", "FD" + Integer.toString(i) + "= " + formattedDetails);*/

            return formattedDetails;
        }
    }
}