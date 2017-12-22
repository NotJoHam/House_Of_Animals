package vanlandingham.friendimals.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vanlandingham.friendimals.Adapters.profile_placeholder_adapter;
import vanlandingham.friendimals.Model.Post;
import vanlandingham.friendimals.Model.Upload;
import vanlandingham.friendimals.R;

/**
 * Created by Owner on 12/19/2017.
 */

public class PlaceholderFragment extends android.support.v4.app.Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView listView;
    private List<Upload> user_posts = new ArrayList<>();
    private static String uid;
    private profile_placeholder_adapter adapter;

    public PlaceholderFragment( ) {    }

    public  PlaceholderFragment newInstance(int sectionNumber,String uid) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        this.uid = uid;
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_fragment,container,false);

        listView = view.findViewById(R.id.viewPager_listView);

        FirebaseDatabase.getInstance().getReference("users").child(uid).child("posts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Upload upload = dataSnapshot.getValue(Upload.class);

                user_posts.add(upload);

                adapter = new profile_placeholder_adapter(getContext(),user_posts);
                listView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
