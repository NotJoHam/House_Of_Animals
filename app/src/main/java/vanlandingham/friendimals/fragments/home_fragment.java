package vanlandingham.friendimals.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
    private boolean has_updated = false;
    private long curr_time = System.currentTimeMillis()-50000000;
    private long prev_time;
    private int n = 1;
    private int following_count;
    private long startTime=0;
    private FirebaseFirestore firestore;

    public ArrayList<User> user_list = new ArrayList<>();
    public ArrayList<Upload> UploadList = new ArrayList<>();
    private homeAdapter adapter;
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

        following_count = curr_user.getFollowing_count();
        firestore = FirebaseFirestore.getInstance();

        initializeHome();
        return view;

    }

    private void initializeHome() {

        Log.d(TAG, "initializeHome: Start");

        firestore.collection("users").document(curr_user.getUid()).collection("following").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        user_list.add(document.toObject(User.class));
                        Log.d(TAG, "onComplete: user_list added");

                    }

                    getPosts(1);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        //TODO: Make a complex query and have a value for the last post a user posted, then only grab following users with a post last posted within 3(?) days?



    }

    private boolean getPosts(int num) {

        for (User this_user : user_list) {
            Log.d(TAG, "initializeHome: Iterating " + num);
            String user_uid = this_user.getUid();


            if (following_count < 20) {
                startTime = System.currentTimeMillis() - (172800000 *num);
            } else if (following_count < 100) {
                startTime = System.currentTimeMillis() - (6400000 *num);
            } else if (following_count < 500) {
                startTime = System.currentTimeMillis() - (4400000 *num);
            } else {
                startTime = System.currentTimeMillis() - (3600000 *num);
            }

            firestore.collection("users").document(user_uid).collection("posts").whereGreaterThan("timestamp",startTime).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: Was succesful");
                        for (DocumentSnapshot document : task.getResult()) {
                            Upload upload = document.toObject(Upload.class);
                            if (upload != null) {
                                Log.d(TAG, "onChildAdded: Initializing");
                                has_updated = true;
                                UploadList.add(upload);
                                Collections.sort(UploadList);
                                adapter = new homeAdapter(getContext(), UploadList);
                                listView.setAdapter(adapter);

                                if (n == following_count) {
                                    //postInitialization();
                                }

                            }


                        }
                    }

                    if (!has_updated) {
                        getPosts(++n);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


        return has_updated;
    }

    private void postInitialization() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                final int lastItem = i + i1;

                if (lastItem == i2 || i2 <= 3) {

                    preLast = lastItem;
                    ++n;
                    Log.d(TAG, "onScroll: prev_time" + prev_time);

                    if (i2 <= 3) {
                        prev_time = curr_time;
                        curr_time = curr_time - 432000000;
                    }
                    if (following_count < 20) {
                        prev_time = curr_time;
                        curr_time = curr_time - 86400000;
                    }

                    else if (following_count < 100) {
                        prev_time = curr_time;
                        curr_time = curr_time - 43200000;
                    }

                    else if (following_count < 500) {
                        prev_time = curr_time;
                        curr_time = curr_time - 7200000;
                    }

                    else {
                        prev_time = curr_time;
                        curr_time = curr_time - 1800000;
                    }

                    prev_time = curr_time;
                    curr_time = curr_time - 100200000;
                    Log.d(TAG, "onScroll: curr_time" + curr_time);



                    for (User this_user : user_list) {
                        String user_uid = this_user.getUid();

                        FirebaseDatabase.getInstance().getReference("users").child(user_uid).child("posts").orderByChild("timestamp").startAt((double) curr_time).endAt((double) prev_time).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                Upload upload = dataSnapshot.getValue(Upload.class);
                                Log.d(TAG, "onChildAdded: updating");
                                if (upload != null && !UploadList.contains(upload)) {
                                    if (upload.getUsername() != null) {
                                        UploadList.add(upload);
                                        Log.d(TAG, "onChildAdded: " + "updated : " + upload);
                                        Collections.sort(UploadList);

                                        int index = listView.getFirstVisiblePosition();
                                        View v = listView.getChildAt(0);
                                        int top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());
                                        homeAdapter adapter = new homeAdapter(getContext(), UploadList);
                                        listView.setAdapter(adapter);

                                        listView.setSelectionFromTop(index, top);
                                    }
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


                    }
                }

            }
        });
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
