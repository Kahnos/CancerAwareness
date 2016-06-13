package com.example.josed_000.cancerawareness;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class LibraryActivityFragment extends Fragment {

    public LibraryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=(View)inflater.inflate(R.layout.fragment_library, container, false);
        ArrayList<Integer> texts=new ArrayList<>(5);
        texts.add(R.string.librarytitle1);
        texts.add(R.string.librarytitle2);
        texts.add(R.string.librarytitle3);
        LibraryAdapter arrayAdapter=new LibraryAdapter(layout.getContext(),texts);
        ListView listView=(ListView)layout.findViewById(R.id.list_library);
        listView.setAdapter(arrayAdapter);
        return layout;
    }


    //Adaptador para el menu
    public class LibraryAdapter extends ArrayAdapter<Integer> {
        private final Context context;
        private final ArrayList<Integer> values;
        public LibraryAdapter(Context context, ArrayList<Integer> values) {
            super(context, R.layout.libraryitem, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final int Content[]={
                    R.string.library1,
                    R.string.library2,
                    R.string.library3
            };

            View rowView;

            rowView = inflater.inflate(R.layout.libraryitem, parent, false);

            TextView textView = (TextView) rowView.findViewById(R.id.item_text_library);
            //ImageView imageView = (ImageView) rowView.findViewById(R.id.item_image_library);

            // Setting the title
            textView.setText(values.get(position));
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((TextView)getActivity().findViewById(R.id.library_title)).setText(values.get(position));
                    ((TextView)getActivity().findViewById(R.id.library_text)).setText(Content[position]);
                }
            });
            return rowView;
        }

    }
}