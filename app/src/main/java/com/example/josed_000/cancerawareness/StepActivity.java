package com.example.josed_000.cancerawareness;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class StepActivity extends ActionBarActivity {
    private FragmentPagerAdapter adapterViewPager;
    private ViewPager vpPager;
    private int step = 0;
    public StringBuilder result;
    public MediaPlayer mp[];

    public void infoBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(StepActivity.this);
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        builder.setCancelable(true);
        builder.setTitle(R.string.info_steps_title);
        builder.setMessage(R.string.info_steps);
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopAllSound();
    }

    public void stopAllSound(){
        for(int i=0; i<13; i++){
            if(mp[i].isPlaying()) {
                mp[i].pause();
                mp[i].seekTo(0);
            }
            else {
                mp[i].seekTo(0);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        vpPager = (ViewPager) findViewById(R.id.steps_viewPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        ImageButton sound = (ImageButton) findViewById(R.id.steps_imageButton_sound);
        sound.setVisibility(View.GONE);

        result = new StringBuilder();
        mp = new MediaPlayer[]{
                MediaPlayer.create(this, R.raw.audio1),
                MediaPlayer.create(this, R.raw.audio2),
                MediaPlayer.create(this, R.raw.audio2_1),
                MediaPlayer.create(this, R.raw.audio2_2),
                MediaPlayer.create(this, R.raw.audio2_3),
                MediaPlayer.create(this, R.raw.audio3),
                MediaPlayer.create(this, R.raw.audio3_1),
                MediaPlayer.create(this, R.raw.audio3_2),
                MediaPlayer.create(this, R.raw.audio3_3),
                MediaPlayer.create(this, R.raw.audio4),
                MediaPlayer.create(this, R.raw.audio4_1),
                MediaPlayer.create(this, R.raw.audio5),
                MediaPlayer.create(this, R.raw.audio5_1),
        };

        vpPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                TextView stepsText = (TextView) findViewById(R.id.steps_textView);
                ImageButton sound = (ImageButton) findViewById(R.id.steps_imageButton_sound);

                switch(position){
                    case 0:
                        step=0;
                        stepsText.setText(R.string.step0);
                        stopAllSound();
                        sound.setVisibility(View.GONE);
                        break;
                    case 1:
                        step=1;
                        stepsText.setText(R.string.step1);
                        stopAllSound();
                        sound.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        step=2;
                        stepsText.setText(R.string.step2);
                        stopAllSound();
                        break;
                    case 3:
                        step=3;
                        stepsText.setText(R.string.step2_1);
                        stopAllSound();
                        break;
                    case 4:
                        step=4;
                        stepsText.setText(R.string.step2_2);
                        stopAllSound();
                        break;
                    case 5:
                        step=5;
                        stepsText.setText(R.string.step2_3);
                        stopAllSound();
                        break;
                    case 6:
                        step=6;
                        stepsText.setText(R.string.step3);
                        stopAllSound();
                        break;
                    case 7:
                        step=7;
                        stepsText.setText(R.string.step3_1);
                        stopAllSound();
                        break;
                    case 8:
                        step=8;
                        stepsText.setText(R.string.step3_2);
                        stopAllSound();
                        break;
                    case 9:
                        step=9;
                        stepsText.setText(R.string.step3_3);
                        stopAllSound();
                        break;
                    case 10:
                        step=10;
                        stepsText.setText(R.string.step4);
                        stopAllSound();
                        break;
                    case 11:
                        step=11;
                        stepsText.setText(R.string.step4_1);
                        stopAllSound();
                        break;
                    case 12:
                        step=12;
                        stepsText.setText(R.string.step5);
                        stopAllSound();
                        break;
                    case 13:
                        step=13;
                        stepsText.setText(R.string.step5_1);
                        stopAllSound();
                        sound.setVisibility(View.VISIBLE);
                        break;
                    default:
                        step=0;
                        stepsText.setText(R.string.step0);
                        stopAllSound();
                        sound.setVisibility(View.VISIBLE);
                        break;
                }
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preferences), Context.MODE_PRIVATE);

        result.replace(0,6, sharedPref.getString( getString(R.string.temp_result_pref), "000000"));
        Log.v("step results", result.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_step, menu);
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
        if (id == R.id.info_steps) {
            infoBox();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void stepsClick(View v){
        ImageView stepsImage = (ImageView) this.findViewById(R.id.steps_imageView);
        TextView stepsText = (TextView) this.findViewById(R.id.steps_textView);
        int viewID = v.getId();

        if (viewID == R.id.imageView_steps_next) { // CLICK next image.
            switch (step) {
                case 0:
                    vpPager.setCurrentItem(1);
                    break;
                case 1:
                    vpPager.setCurrentItem(2);
                    break;
                case 2:
                    vpPager.setCurrentItem(3);
                    break;
                case 3:
                    vpPager.setCurrentItem(4);
                    break;
                case 4:
                    vpPager.setCurrentItem(5);
                    break;
                case 5:
                    vpPager.setCurrentItem(6);
                    break;
                case 6:
                    vpPager.setCurrentItem(7);
                    break;
                case 7:
                    vpPager.setCurrentItem(8);
                    break;
                case 8:
                    vpPager.setCurrentItem(9);
                    break;
                case 9:
                    vpPager.setCurrentItem(10);
                    break;
                case 10:
                    vpPager.setCurrentItem(11);
                    break;
                case 11:
                    vpPager.setCurrentItem(12);
                    break;
                case 12:
                    vpPager.setCurrentItem(13);
                    break;
                case 13:
                    vpPager.setCurrentItem(0);
                    break;
                default:
                    vpPager.setCurrentItem(0);
                    break;
            }
        }
        else if (viewID == R.id.imageView_steps_prev){ // CLICK previous image.
            switch (step) {
                case 0:
                    vpPager.setCurrentItem(13);
                    break;
                case 1:
                    vpPager.setCurrentItem(0);
                    break;
                case 2:
                    vpPager.setCurrentItem(1);
                    break;
                case 3:
                    vpPager.setCurrentItem(2);
                    break;
                case 4:
                    vpPager.setCurrentItem(3);
                    break;
                case 5:
                    vpPager.setCurrentItem(4);
                    break;
                case 6:
                    vpPager.setCurrentItem(5);
                    break;
                case 7:
                    vpPager.setCurrentItem(6);
                    break;
                case 8:
                    vpPager.setCurrentItem(7);
                    break;
                case 9:
                    vpPager.setCurrentItem(8);
                    break;
                case 10:
                    vpPager.setCurrentItem(9);
                    break;
                case 11:
                    vpPager.setCurrentItem(10);
                    break;
                case 12:
                    vpPager.setCurrentItem(11);
                    break;
                case 13:
                    vpPager.setCurrentItem(12);
                    break;
                default:
                    vpPager.setCurrentItem(0);
            }
        }
        else if (viewID == R.id.textView_steps_ohshit){ // CLICK survey button.
            Intent startIntent = new Intent(this, SurveyActivity.class);
            startActivity(startIntent);
        }
        else if (viewID == R.id.textView_steps_finish){
            // Start shared preferences and editor.
            SharedPreferences sharedPref = getSharedPreferences(
                    getString(R.string.preferences), Context.MODE_PRIVATE);

            result.replace(0, 6, sharedPref.getString(getString(R.string.temp_result_pref), "000000"));

            Intent startIntent = new Intent(this, ResultActivity.class);
            startIntent.putExtra("Result", result.toString());
            startActivity(startIntent);
        }
        else if (viewID == R.id.steps_imageButton_sound){

            switch (step) {
                case 0:
                    break;
                case 1:
                    if( mp[0].isPlaying() ){
                        mp[0].pause();
                    }
                    else{
                        mp[0].start();
                    }
                    break;
                    case 2:
                    if( mp[1].isPlaying() ){
                    mp[1].pause();
                    }
                    else{
                        mp[1].start();
                    }
                    break;
                case 3:
                    if( mp[2].isPlaying() ){
                        mp[2].pause();
                    }
                    else{
                        mp[2].start();
                    }
                    break;
                case 4:
                    if( mp[3].isPlaying() ){
                        mp[3].pause();
                    }
                    else{
                        mp[3].start();
                    }
                    break;
                case 5:
                    if( mp[4].isPlaying() ){
                        mp[4].pause();
                    }
                    else{
                        mp[4].start();
                    }
                    break;
                case 6:
                    if( mp[5].isPlaying() ){
                        mp[5].pause();
                    }
                    else{
                        mp[5].start();
                    }
                    break;
                case 7:
                    if( mp[6].isPlaying() ){
                        mp[6].pause();
                    }
                    else{
                        mp[6].start();
                    }
                    break;
                case 8:
                    if( mp[7].isPlaying() ){
                        mp[7].pause();
                    }
                    else{
                        mp[7].start();
                    }
                    break;
                case 9:
                    if( mp[8].isPlaying() ){
                        mp[8].pause();
                    }
                    else{
                        mp[8].start();
                    }
                    break;
                case 10:
                    if( mp[9].isPlaying() ){
                        mp[9].pause();
                    }
                    else{
                        mp[9].start();
                    }
                    break;
                case 11:
                    if( mp[10].isPlaying() ){
                        mp[10].pause();
                    }
                    else{
                        mp[10].start();
                    }
                    break;
                case 12:
                    if( mp[11].isPlaying() ){
                        mp[11].pause();
                    }
                    else{
                        mp[11].start();
                    }
                    break;
                case 13:
                    if( mp[12].isPlaying() ){
                        mp[12].pause();
                    }
                    else{
                        mp[12].start();
                    }
                    break;
                default:
                    break;
            }
        }

    }

    public void onClickDialog(View v){
        int id = v.getId();

        final View dialogView = View.inflate(this, R.layout.step1_dialog, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        //View parent = (View) v.getParent();
        ImageView dialogImage = (ImageView) dialogView.findViewById(R.id.dialog_imageView);

        switch(id){
            case R.id.image_sintoma1:
                dialogImage.setImageResource(R.drawable.sintoma_1_secrecion);
                alertDialog.setView(dialogView);
                alertDialog.show();
                break;

            case R.id.image_sintoma2:
                dialogImage.setImageResource( R.drawable.sintoma_2_pezon );
                alertDialog.setView(dialogView);
                alertDialog.show();
                break;

            case R.id.image_sintoma3:
                dialogImage.setImageResource( R.drawable.sintoma_3_ );
                alertDialog.setView(dialogView);
                alertDialog.show();
                break;

            case R.id.image_sintoma4:
                dialogImage.setImageResource( R.drawable.sintoma_4_tension );
                alertDialog.setView(dialogView);
                alertDialog.show();
                break;

            case R.id.image_sintoma5:
                dialogImage.setImageResource( R.drawable.sintoma_5_textura );
                alertDialog.setView(dialogView);
                alertDialog.show();
                break;

            case R.id.image_sintoma6:
                dialogImage.setImageResource( R.drawable.sintoma_6_masa );
                alertDialog.setView(dialogView);
                alertDialog.show();
                break;

            default:
                break;
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 14;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return Steps.newInstance(0);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return Step1.newInstance(1);
                case 2: // Fragment # 1 - This will show SecondFragment
                    return Step1.newInstance(2);
                case 3:
                    return Steps.newInstance(3);
                case 4:
                    return Steps.newInstance(4);
                case 5:
                    return Steps.newInstance(5);
                case 6:
                    return Step1.newInstance(6);
                case 7:
                    return Steps.newInstance(7);
                case 8:
                    return Steps.newInstance(8);
                case 9:
                    return Steps.newInstance(9);
                case 10:
                    return Step1.newInstance(10);
                case 11:
                    return Steps.newInstance(11);
                case 12:
                    return Step1.newInstance(12);
                case 13:
                    return Steps.newInstance(13);
                default:
                    return null;
            }
        }

    }

}
