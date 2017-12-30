package vanlandingham.friendimals.fragments;

import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
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

import vanlandingham.friendimals.Model.Post;
import vanlandingham.friendimals.Adapters.homeAdapter;
import vanlandingham.friendimals.Model.User;
import vanlandingham.friendimals.R;

/**
 * Created by Owner on 11/16/2017.
 */

public class home_fragment extends android.support.v4.app.Fragment {

    public ArrayList<String> childkey_list = new ArrayList<>();
    public ArrayList<Post> messageList = new ArrayList<>();
    private String username;
    private User curr_user;

    public home_fragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.home_activity, container, false);
        setUpWindowAnimations();
        savedInstanceState = getArguments();

        if (savedInstanceState != null) {
            curr_user = savedInstanceState.getParcelable("curr_user");
            username = savedInstanceState.getString("username", "username");
        }
        System.out.println(username);
        ListView listView = view.findViewById(R.id.home_list);


        FirebaseDatabase.getInstance().getReference("users").child(curr_user.getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        homeAdapter adapter = new homeAdapter(getContext(),messageList);
        listView.setAdapter(adapter);


        String message = "This is the home fragment";


        Post post = new Post(message,username);
        messageList.add(post);

        System.out.println("Adding post 1");
        //adapter.add(post);



        return view;

    }

    private void setUpWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(2000);
        Transition transition;
        transition = TransitionInflater.from(getContext()).inflateTransition(R.transition.activity_fade);
        getActivity().getWindow().setEnterTransition(transition);

        Slide slide = new Slide();
        slide.setDuration(2000);
        getActivity().getWindow().setExitTransition(slide);

    }
}
