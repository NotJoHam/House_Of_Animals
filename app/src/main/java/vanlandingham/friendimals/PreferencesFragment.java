package vanlandingham.friendimals;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Owner on 11/15/2017.
 */




    public class PreferencesFragment extends Fragment {
        private int position;

        public PreferencesFragment() {
            // Required empty public constructor

            //Bundle bundle = getArguments();

            //position=this.getArguments().getInt("position");
            //String string = bundle.getString("nothing");


        }




        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            //Bundle bundle = getArguments();
            //position = bundle.getInt("position",54);
            // Inflate the layout for this fragment


           /* switch (position) {

                case 0:
                    Intent intent = new Intent(getActivity(),Featured.class);
                    startActivity(intent);
            }
            */


            return inflater.inflate(R.layout.featured_activity, container, false);
        }


    }


