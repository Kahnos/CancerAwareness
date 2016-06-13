package com.example.josed_000.cancerawareness;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class AvatarCreation extends ActionBarActivity {
    ViewPager viewPager=null;

    public void infoBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AvatarCreation.this);
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        builder.setCancelable(true);
        builder.setTitle(R.string.info_avatar_title);
        builder.setMessage(R.string.info_avatar);
        builder.create().show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_avatar_creation);
        ImageView body=(ImageView) findViewById(R.id.body_view);

        //Recuperacion de las ultimas piezas de ropa utilizadas
        final SharedPreferences sp=getApplicationContext().getSharedPreferences("AVATAR",MODE_PRIVATE);
        final SharedPreferences.Editor spe=sp.edit();
        ((ImageView)findViewById(R.id.wig_view)).setImageResource(sp.getInt("AVATAR_WIG",R.drawable.pelo1));
        ((ImageView)findViewById(R.id.clothes_view)).setImageResource(sp.getInt("AVATAR_CLOTHES",R.drawable.blusa1));
        ((ImageView)findViewById(R.id.shoes_view)).setImageResource(sp.getInt("AVATAR_SHOES",R.drawable.zapatos1));
        ((ImageView)findViewById(R.id.pants_view)).setImageResource(sp.getInt("AVATAR_PANTS",R.drawable.short1));
        ((ImageView)findViewById(R.id.body_view)).setImageResource(sp.getInt("AVATAR_SKIN", R.drawable.munequita1));

        //Set gesture listener
        final GestureDetector gestureDetector=new GestureDetector(this,new GestureListener());
        body.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
        //Set Dropzone
        body.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action=event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        Log.e("WTF","Drag listener");
                        /*Toast.makeText(v.getContext(),"DragStarted", Toast.LENGTH_LONG).show();*/
                        return true;
                    case DragEvent.ACTION_DRAG_ENTERED:
                    case DragEvent.ACTION_DRAG_EXITED:
                        return true;
                    case DragEvent.ACTION_DROP:
                        ClipData.Item item = event.getClipData().getItemAt(0);
                        // Gets the text data from the item.
                        CharSequence dragData = item.getText();
                        // Displays a message containing the dragged data.
                        Log.e("WTF", "Drop listener");
                        // Gets the text data from the item.
                        /*Toast.makeText(v.getContext(),"Content:"+dragData, Toast.LENGTH_LONG).show();*/
                        SharedPreferences sp=getApplicationContext().getSharedPreferences("AVATAR",MODE_PRIVATE);
                        SharedPreferences.Editor spe=sp.edit();
                        switch (viewPager.getCurrentItem()){
                            case 0:
                                ((ImageView)findViewById(R.id.wig_view)).
                                        setImageResource(Integer.parseInt(dragData.toString()));
                                spe.putInt("AVATAR_WIG",Integer.parseInt(dragData.toString()));
                                spe.commit();
                                break;
                            case 1:
                                //Arreglo para verificar si es un pantalon
                                int pants[]={
                                        R.drawable.pantalon1, R.drawable.pantalon2, R.drawable.short1, R.drawable.short2
                                };
                                boolean clothes=true;
                                for(int i=0;i<pants.length;i++){
                                    if(Integer.parseInt(dragData.toString())==pants[i]){
                                        ((ImageView)findViewById(R.id.pants_view)).
                                                setImageResource(Integer.parseInt(dragData.toString()));
                                        clothes=false;
                                        spe.putInt("AVATAR_PANTS", Integer.parseInt(dragData.toString()));
                                        spe.commit();
                                        break;
                                    }
                                }
                                if(clothes) {
                                    ((ImageView) findViewById(R.id.clothes_view)).
                                            setImageResource(Integer.parseInt(dragData.toString()));
                                    spe.putInt("AVATAR_CLOTHES",Integer.parseInt(dragData.toString()));
                                    spe.commit();
                                    /* En caso de que se haga la validacion de si tiene un vestido puesto
                                    if(Integer.parseInt(dragData.toString())==R.drawable.vestido1 ||
                                            Integer.parseInt(dragData.toString())==R.drawable.vestido2 ||
                                            Integer.parseInt(dragData.toString())==R.drawable.vestido3){
                                        ((ImageView) findViewById(R.id.pants_view)).
                                                setImageResource(Integer.parseInt(dragData.toString()));
                                    }*/
                                }
                                break;
                            case 2:
                                ((ImageView)findViewById(R.id.shoes_view)).
                                        setImageResource(Integer.parseInt(dragData.toString()));
                                spe.putInt("AVATAR_SHOES", Integer.parseInt(dragData.toString()));
                                spe.commit();
                                break;
                        }
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        return true;
                }
                return true;
            }
        });
        //SharedPreferences sharedPreferences=getSharedPreferences("AVATAR", Context.MODE_PRIVATE);
        //((ImageView)findViewById(R.id.wig_view)).
        //        setImageResource();
        viewPager=(ViewPager)findViewById(R.id.pager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                paginas_b(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setAdapter(new AvatarPageAdapter(getSupportFragmentManager()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_avatar_creation, menu);
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

        if (id == R.id.info_avatar) {
            infoBox();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void avatarOnClick(View v){
        int id = v.getId();
        final SharedPreferences sp=getApplicationContext().getSharedPreferences("AVATAR",MODE_PRIVATE);
        final SharedPreferences.Editor spe=sp.edit();

        switch(id){
            case R.id.avatar_skin1:
                ((ImageView) findViewById(R.id.body_view)).
                        setImageResource(R.drawable.munequita1);
                spe.putInt("AVATAR_SKIN",R.drawable.munequita1);
                spe.commit();
                break;
            case R.id.avatar_skin2:
                ((ImageView) findViewById(R.id.body_view)).
                        setImageResource(R.drawable.munequita2);
                spe.putInt("AVATAR_SKIN",R.drawable.munequita2);
                spe.commit();
                break;
            case R.id.avatar_skin3:
                ((ImageView) findViewById(R.id.body_view)).
                        setImageResource(R.drawable.munequita3);
                spe.putInt("AVATAR_SKIN",R.drawable.munequita3);
                spe.commit();
                break;
            case R.id.avatar_finish:
                Intent startIntent = new Intent(this, MainActivity.class);
                startActivity(startIntent);
                break;
            default:
                break;
        }
    }

    public void paginas_b(int pagina){
        findViewById(R.id.cabello_button).setBackgroundColor(Color.parseColor("#fffaf0fa"));
        findViewById(R.id.ropa_button).setBackgroundColor(Color.parseColor("#fffaf0fa"));
        findViewById(R.id.zapatos_button).setBackgroundColor(Color.parseColor("#fffaf0fa"));
        switch(pagina){
            case 0:
                findViewById(R.id.cabello_button).setBackgroundColor(Color.parseColor("#FFF5C9F5"));
                break;
            case 1:
                findViewById(R.id.ropa_button).setBackgroundColor(Color.parseColor("#FFF5C9F5"));
                break;
            case 2:
                findViewById(R.id.zapatos_button).setBackgroundColor(Color.parseColor("#FFF5C9F5"));
                break;
        }
    }

    public void paginas(View view){
        int id=view.getId();
        switch(id){
            case R.id.cabello_button:
                viewPager.setCurrentItem(0,true);
                break;
            case R.id.ropa_button:
                viewPager.setCurrentItem(1,true);
                break;
            case R.id.zapatos_button:
                viewPager.setCurrentItem(2,true);
                break;
        }
    }

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            SharedPreferences sp=getApplicationContext().getSharedPreferences("AVATAR",MODE_PRIVATE);
            SharedPreferences.Editor spe=sp.edit();
            /*
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(getApplicationContext(),"LEFT",Toast.LENGTH_LONG).show();

                return true; // Right to left
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(getApplicationContext(),"RIGHT",Toast.LENGTH_LONG).show();
                return true; // Left to right
            }

            if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(getApplicationContext(),"UP",Toast.LENGTH_LONG).show();
                return true; // Bottom to top
            }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(getApplicationContext(),"DOWN",Toast.LENGTH_LONG).show();
                return true; // Top to bottom
            }
            */
            //Ante cualquier movimiento remover ropa
            if((e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) ||
                    (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) ||
                    (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) ||
                    (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
                    ){
                /*Toast.makeText(getApplicationContext(),"Movimiento recibido", Toast.LENGTH_LONG).show();*/
                if(sp.getInt("AVATAR_CLOTHES",R.drawable.blusa1)!=R.drawable.blank){
                    ((ImageView)findViewById(R.id.clothes_view)).
                            setImageResource(R.drawable.blank);
                    spe.putInt("AVATAR_CLOTHES",R.drawable.blank);
                    spe.commit();
                }else{
                    if(sp.getInt("AVATAR_SHOES",R.drawable.zapatos1)!=R.drawable.blank){
                        ((ImageView)findViewById(R.id.shoes_view)).
                                setImageResource(R.drawable.blank);
                        spe.putInt("AVATAR_SHOES",R.drawable.blank);
                        spe.commit();
                    }else{
                        if(sp.getInt("AVATAR_PANTS",R.drawable.short1)!=R.drawable.blank){
                            ((ImageView)findViewById(R.id.pants_view)).
                                    setImageResource(R.drawable.blank);
                            spe.putInt("AVATAR_PANTS",R.drawable.blank);
                            spe.commit();
                        }else{
                            if(sp.getInt("AVATAR_WIG",R.drawable.short1)!=R.drawable.blank){
                                ((ImageView)findViewById(R.id.wig_view)).
                                        setImageResource(R.drawable.blank);
                                spe.putInt("AVATAR_WIG",R.drawable.blank);
                                spe.commit();
                            }
                        }

                    }

                }

            }
            return false;
        }
    }
}


class AvatarPageAdapter extends FragmentStatePagerAdapter {
    protected AvatarPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position){
            case 0:
                fragment = new AvatarSelectPage().newInstance(0);
                break;
            case 1:
                fragment = new AvatarSelectPage().newInstance(1);
                break;
            case 2:
                fragment = new AvatarSelectPage().newInstance(2);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}