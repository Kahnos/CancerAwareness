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
public class Steps extends Fragment {
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static Steps newInstance(int page) {
        Steps steps = new Steps();
        Bundle args = new Bundle();
        args.putInt("Page", page);
        steps.setArguments(args);
        return steps;
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
        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        ImageView stepsImage = (ImageView) view.findViewById(R.id.steps_imageView);

        switch(page){
            case 0:
                stepsImage.setImageResource(R.drawable.cancer_chan);
                break;
            case 1:
                //stepsImage.setImageResource(R.drawable.pos_1);
                break;
            case 2:
                stepsImage.setImageResource(R.drawable.pos_2_0);
                break;
            case 3:
                stepsImage.setImageResource(R.drawable.pos_2_1);
                break;
            case 4:
                stepsImage.setImageResource(R.drawable.pos_2_2);
                break;
            case 5:
                stepsImage.setImageResource(R.drawable.pos_2_3);
                break;
            case 6:
                stepsImage.setImageResource(R.drawable.pos_3_0);
                break;
            case 7:
                stepsImage.setImageResource(R.drawable.pos_3_1);
                break;
            case 8:
                stepsImage.setImageResource(R.drawable.pos_3_2);
                break;
            case 9:
                stepsImage.setImageResource(R.drawable.pos_3_3);
                break;
            case 10:
                stepsImage.setImageResource(R.drawable.pos_4_0);
                break;
            case 11:
                stepsImage.setImageResource(R.drawable.pos_4_1);
                break;
            case 12:
                stepsImage.setImageResource(R.drawable.pos_5_0);
                break;
            case 13:
                stepsImage.setImageResource(R.drawable.pos_5_1);
                break;
            default:
                stepsImage.setImageResource(R.drawable.cancer_chan);
                break;
        }

        return view;
    }
}