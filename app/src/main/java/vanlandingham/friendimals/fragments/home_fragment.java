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
import com.google.android.gms.tasks.OnSuccessListener;
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
    private long following_count;
    private long startTime=0;
    private FirebaseFirestore firestore;

    public ArrayList<User> user_list = new ArrayList<>();
    private ArrayList<String> uid_list = new ArrayList<>();
    public ArrayList<Upload> UploadList = new ArrayList<>();
    private homeAdapter adapter;
    private String username;
    private User curr_user;
    private long num_of_posts=0;

    private ListView listView;

    public home_fragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_activity, container, false);
        savedInstanceState = getArguments();

        if (savedInstanceState != null) {
            curr_user = savedInstanceState.getParcelable("curr_user");
            username = savedInstanceState.getString("username", "username");
        }
        System.out.println(username);
        listView = view.findViewById(R.id.home_list);
        firestore = FirebaseFirestore.getInstance();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getfollowing_count();
                Log.d(TAG, "onCreateView: first following_count:  " + following_count);
            }
        };
        new Thread(runnable).start();

        initializeHome();
        return view;

    }

    private void getfollowing_count(){

        firestore.collection("users").document(curr_user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    following_count = (long) documentSnapshot.get("following_count");
                    Log.d(TAG, "onSuccess: second following_count: " + following_count);
                }
            }
        });

    }

    private void initializeHome() {

        curr_time = System.currentTimeMillis();
        firestore.collection("users").document(curr_user.getUid()).collection("following").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        uid_list.add((String) document.get("uid"));
                    }

                    getNum_Posts();

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

    private void getNum_Posts() {
        for (String uid : uid_list) {
            firestore.collection("users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        num_of_posts += (long) documentSnapshot.get("num_posts");
                        Log.d(TAG, "onSuccess: num_of_posts: " + num_of_posts);
                    }
                }
            });
        }
        getPosts(1);
    }

    private boolean getPosts(int num) {

        for (String this_uid : uid_list) {
            Log.d(TAG, "initializeHome: Iterating " + num);

            if (following_count < 20) {
                prev_time = curr_time;
                curr_time = curr_time - 8640000;
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

            firestore.collection("users").document(this_uid).collection("posts").whereGreaterThan("timestamp",curr_time).whereLessThanOrEqualTo("timestamp",prev_time).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Upload upload = document.toObject(Upload.class);
                            if (upload != null) {
                                has_updated = true;
                                UploadList.add(upload);
                                int index = listView.getFirstVisiblePosition();
                                View v = listView.getChildAt(0);
                                int top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());

                                Collections.sort(UploadList);
                                adapter = new homeAdapter(getContext(), UploadList);
                                listView.setAdapter(adapter);
                                listView.setSelectionFromTop(index, top);

                            }


                        }
                    }

                    if (!has_updated || (listView.getCount() < 3 && listView.getCount() < num_of_posts) ) {
                        getPosts(++n);
                    }
                    else {
                        postInitialization();
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
                if (lastItem == i2 && listView.getCount() < num_of_posts) {
                    if (preLast != lastItem) {
                        if (following_count > 0 ) {
                            Log.d(TAG, "onScroll: scrolled to bottom");
                            has_updated = false;
                            getPosts(1);
                            preLast = lastItem;
                        }
                    }
                }

            }
        });
    }

}
