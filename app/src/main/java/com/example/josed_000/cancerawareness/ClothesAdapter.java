package com.example.josed_000.cancerawareness;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Jesús Castro on 01-05-2015.
 */
public class ClothesAdapter extends BaseAdapter {
    Context mContext;
    int selected;
    private int page;
    public ArrayList<Integer> Ropa;
    public ArrayList<Integer> trueRopa;

    ClothesAdapter(Context c, int page){
        this.page = page;
        mContext=c;
        Ropa = new ArrayList<Integer>();
        trueRopa = new ArrayList<Integer>();
        setRopa();
        Log.d("INFO", "LLego al constructor");
    }

    public int getRopaAt(int pos){
        return Ropa.get(pos);
    }
    public int gettrueRopaAt(int pos) { return trueRopa.get(pos);}

    public void setPage(int page){
        this.page = page;
    }

    @Override
    public int getCount() {
        return Ropa.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void SelectImage(int pos){
        selected=pos;
        return;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("INFO", "Llego a get View");
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        Log.v("Ropa inside view",Integer.toString(Ropa.get(position)));
        imageView.setImageResource(Ropa.get(position));
        return imageView;
    }

    private void setRopa(){
        switch(page){
            case 0:
                int cabelloID[]={
                        R.drawable.minioruga1,
                        R.drawable.minioruga2,
                        R.drawable.minioruga3,
                        R.drawable.minioruga4,
                        R.drawable.minipelo1,
                        R.drawable.minipelo2,
                        R.drawable.minipelo3,
                        R.drawable.minipelo4,
                        R.drawable.minipelo5,
                        R.drawable.minipelo6,
                        R.drawable.minipelo7,
                        R.drawable.minipelo8
                    };
                for(int i = 0; i < cabelloID.length; i++){
                    //Ropa[i] = cabelloID[i];
                    Ropa.add(i, cabelloID[i]);
                }
                int truecabelloID[]={
                        R.drawable.oruga1,
                        R.drawable.oruga2,
                        R.drawable.oruga3,
                        R.drawable.oruga4,
                        R.drawable.pelo1,
                        R.drawable.pelo2,
                        R.drawable.pelo3,
                        R.drawable.pelo4,
                        R.drawable.pelo5,
                        R.drawable.pelo6,
                        R.drawable.pelo7,
                        R.drawable.pelo8
                };
                for(int i = 0; i < truecabelloID.length; i++){
                    //Ropa[i] = cabelloID[i];
                    trueRopa.add(i, truecabelloID[i]);
                }
                break;
            case 1:
                int ropaID[]={
                        R.drawable.miniblusa1,
                        R.drawable.miniblusa2,
                        R.drawable.miniblusa3,
                        R.drawable.miniblusa4,
                        R.drawable.miniblusa5,
                        R.drawable.miniblusa6,
                        R.drawable.miniblusa7,
                        R.drawable.minivestido1,
                        R.drawable.minivestido2,
                        R.drawable.minivestido3,
                        R.drawable.minipantalon1,
                        R.drawable.minipantalon2,
                        R.drawable.minishort1,
                        R.drawable.minishort2
                };
                for(int i = 0; i < ropaID.length; i++){
                    //Ropa[i] = ropaID[i];
                    Ropa.add(i, ropaID[i]);
                }
                int trueropaID[]={
                        R.drawable.blusa1,
                        R.drawable.blusa2,
                        R.drawable.blusa3,
                        R.drawable.blusa4,
                        R.drawable.blusa5,
                        R.drawable.blusa6,
                        R.drawable.blusa7,
                        R.drawable.vestido1,
                        R.drawable.vestido2,
                        R.drawable.vestido3,
                        R.drawable.pantalon1,
                        R.drawable.pantalon2,
                        R.drawable.short1,
                        R.drawable.short2
                };
                for(int i = 0; i < ropaID.length; i++){
                    //Ropa[i] = ropaID[i];
                    trueRopa.add(i, trueropaID[i]);
                }
                break;
            case 2:
                int zapatosID[]={
                        R.drawable.minizapatos1,
                        R.drawable.minizapatos2,
                        R.drawable.minizapatos3,
                        R.drawable.minizapatos4,
                        R.drawable.minizapatos5,
                        R.drawable.minizapatos6
                };
                for(int i = 0; i < zapatosID.length; i++){
                    //Ropa[i] = zapatosID[i];
                    Ropa.add(i, zapatosID[i]);
                }
                int truezapatosID[]={
                        R.drawable.zapatos1,
                        R.drawable.zapatos2,
                        R.drawable.zapatos3,
                        R.drawable.zapatos4,
                        R.drawable.zapatos5,
                        R.drawable.zapatos6
                };
                for(int i = 0; i < zapatosID.length; i++){
                    //Ropa[i] = zapatosID[i];
                    trueRopa.add(i, truezapatosID[i]);
                }
                break;
        }
    }
}
