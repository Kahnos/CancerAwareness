package com.example.josed_000.cancerawareness;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by JD on 05/05/2015.
 */
public class Step1 extends Fragment {
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static Step1 newInstance(int page) {
        Step1 step1 = new Step1();
        Bundle args = new Bundle();
        args.putInt("Page", page);
        step1.setArguments(args);
        return step1;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("Page", 0);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step1, container, false);

        ImageView step = (ImageView) view.findViewById(R.id.step1_imageView);

        switch( page ){
            case 1:
                step.setImageResource(R.drawable.pos_1);
                break;
            case 2:
                step.setImageResource(R.drawable.pos_2_0);
                break;
            case 6:
                step.setImageResource(R.drawable.pos_3_0);
                break;
            case 10:
                step.setImageResource(R.drawable.pos_4_0);
                break;
            case 12:
                step.setImageResource(R.drawable.pos_5_0);
                break;
        }

        return view;
    }
}