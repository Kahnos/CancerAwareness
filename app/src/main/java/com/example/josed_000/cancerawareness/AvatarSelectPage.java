package com.example.josed_000.cancerawareness;

import android.app.Activity;
import android.content.ClipData;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AvatarSelectPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AvatarSelectPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvatarSelectPage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PAGE = "Page";

    // TODO: Rename and change types of parameters
    private int page;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page The page the fragment represents.
     * @return A new instance of fragment AvatarSelectPage.
     */
    // TODO: Rename and change types and number of parameters
    public static AvatarSelectPage newInstance(int page) {
        AvatarSelectPage fragment = new AvatarSelectPage();
        Bundle args = new Bundle();
        args.putInt(PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    public AvatarSelectPage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt(PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_page1, container, false);
        GridView gridview = (GridView) view.findViewById(R.id.grid_view);
        final ClothesAdapter clothesAdapter = new ClothesAdapter(view.getContext(), page);
        if(clothesAdapter!=null)
            Log.d("INFO", "No es nulo");
        gridview.setAdapter(clothesAdapter);
        //Listener for long click(drag)

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ClipData clipData=ClipData.newPlainText("Cloth",""+clothesAdapter.gettrueRopaAt(position));
                view.startDrag (clipData, new View.DragShadowBuilder(view), null, 0);
                return true;
            }
        });

        //Listener for click
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                SharedPreferences sp=getActivity().getSharedPreferences("AVATAR", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();

                switch (page){
                    case 0:
                        editor.putInt("WIG_VIEW", clothesAdapter.gettrueRopaAt(position));
                        ((ImageView)getActivity().findViewById(R.id.wig_view)).
                                setImageResource(clothesAdapter.gettrueRopaAt(position));
                        editor.putInt("AVATAR_WIG",clothesAdapter.gettrueRopaAt(position));
                        editor.commit();
                        break;
                    case 1:
                        if(position<10) {
                            editor.putInt("CLOTHES_VIEW",clothesAdapter.gettrueRopaAt(position));
                            ((ImageView) getActivity().findViewById(R.id.clothes_view)).
                                    setImageResource(clothesAdapter.gettrueRopaAt(position));
                            editor.putInt("AVATAR_CLOTHES",clothesAdapter.gettrueRopaAt(position));
                            editor.commit();
                        }else {
                            editor.putInt("PANTS_VIEW",clothesAdapter.gettrueRopaAt(position));
                            ((ImageView) getActivity().findViewById(R.id.pants_view)).
                                    setImageResource(clothesAdapter.gettrueRopaAt(position));
                            editor.putInt("AVATAR_PANTS",clothesAdapter.gettrueRopaAt(position));
                            editor.commit();
                        }
                        break;
                    case 2:
                        editor.putInt("SHOES_VIEW",clothesAdapter.gettrueRopaAt(position));
                        ((ImageView)getActivity().findViewById(R.id.shoes_view)).
                                setImageResource(clothesAdapter.gettrueRopaAt(position));
                        editor.putInt("AVATAR_SHOES",clothesAdapter.gettrueRopaAt(position));
                        editor.commit();
                        break;
                }
                return;
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}