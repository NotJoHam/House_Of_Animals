package vanlandingham.friendimals.fragments;

import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import vanlandingham.friendimals.Model.Post;
import vanlandingham.friendimals.Adapters.homeAdapter;
import vanlandingham.friendimals.Model.Upload;
import vanlandingham.friendimals.Model.User;
import vanlandingham.friendimals.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Owner on 11/16/2017.
 */

public class home_fragment extends android.support.v4.app.Fragment {

    private int preLast;
    private long curr_time = System.currentTimeMillis()-50000000;
    private long prev_time;
    private int n = 0;

    public ArrayList<User> user_list = new ArrayList<>();
    public ArrayList<Upload> UploadList = new ArrayList<>();
    private String username;
    private User curr_user;

    private ListView listView;

    public home_fragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.home_activity, container, false);
        //setUpWindowAnimations();
        savedInstanceState = getArguments();

        if (savedInstanceState != null) {
            curr_user = savedInstanceState.getParcelable("curr_user");
            username = savedInstanceState.getString("username", "username");
        }
        System.out.println(username);
        listView = view.findViewById(R.id.home_list);




        FirebaseDatabase.getInstance().getReference("users").child(curr_user.getUid()).child("following").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                user_list.add(user);
                //for (User this_user : user_list) {
                    //Log.d(TAG, "onCreateView: " + this_user);
                    String user_uid = user.getUid();
                    //startAt((double)System.currentTimeMillis()-21600000)
                    FirebaseDatabase.getInstance().getReference("users").child(user_uid).child("posts").orderByChild("timestamp").startAt((double)System.currentTimeMillis()-50000000).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Upload upload = dataSnapshot.getValue(Upload.class);
                            if (upload != null) {
                                Log.d(TAG, "onChildAdded: Initializing");
                                UploadList.add(upload);
                                Collections.sort(UploadList);
                                homeAdapter adapter = new homeAdapter(getContext(), UploadList);
                                listView.setAdapter(adapter);
                            }
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
                //}
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

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                final int lastItem = i + i1;

                if (lastItem == i2) {
                    //if (preLast != lastItem) {
                        preLast = lastItem;
                        ++n;
                    Log.d(TAG, "onScroll: prev_time" + prev_time);
                        prev_time = curr_time;
                        curr_time = curr_time-7200000;
                    Log.d(TAG, "onScroll: curr_time" + curr_time);

                        /*FirebaseDatabase.getInstance().getReference("users").child(curr_user.getUid()).child("following").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                user_list.add(user);
                                //for (User this_user : user_list) {
                                //Log.d(TAG, "onCreateView: " + this_user);
                                String user_uid = user.getUid();
                                //startAt((double)System.currentTimeMillis()-21600000)
                                */


                                for (User this_user : user_list) {
                                    Log.d(TAG, "onScroll: for loop " +  this_user.getUsername());
                                    String user_uid = this_user.getUid();

                                    FirebaseDatabase.getInstance().getReference("users").child(user_uid).child("posts").orderByChild("timestamp").startAt((double) curr_time).endAt((double) prev_time).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Upload upload = dataSnapshot.getValue(Upload.class);
                                            Log.d(TAG, "onChildAdded: updating");
                                            if (upload != null) {
                                                if (upload.getUsername() != null) {
                                                    UploadList.add(upload);
                                                    Log.d(TAG, "onDataChange: " + "updated : " + upload);
                                                    Collections.sort(UploadList);
                                                    homeAdapter adapter = new homeAdapter(getContext(), UploadList);
                                                    listView.setAdapter(adapter);
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                /*
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        */
                                }

                    //}
                }

            }
        });



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
