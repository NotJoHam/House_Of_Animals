package vanlandingham.friendimals.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import vanlandingham.friendimals.Home;
import vanlandingham.friendimals.Model.Upload;
import vanlandingham.friendimals.Model.User;
import vanlandingham.friendimals.NpaLinearLayoutManager;
import vanlandingham.friendimals.R;
import vanlandingham.friendimals.fragments.featured_fragment;
import vanlandingham.friendimals.fragments.profile_fragment;

import static android.content.ContentValues.TAG;

/**
 * Created by Owner on 11/16/2017.
 */

public class featuredAdapter extends ArrayAdapter<featured_post>{

    private List<featured_post> featured_postList = new ArrayList<>();
    private TextView featured_header;
    private RecyclerView recyclerView;
    private recycler_adapter adapter;
    private Context context;
    private int count;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseUploads;
    private DatabaseReference mDatabaseUsers;
    private String mUserId;
    private FirebaseStorage storage;
    private StorageReference imagesRef;
    private StorageReference storageRef;
    private Bitmap bitmap = null;
    private List<Upload> uploads;
    private String username;
    private User user;
    private RelativeLayout featured_layout;
    private featured_fragment featured_fragment;




    public featuredAdapter(Context context, ArrayList<featured_post> users,featured_fragment featured_fragment){

        super(context,0,users);
        this.context=context;
        this.count = 0;
        this.featured_fragment = featured_fragment;

    }

    public View getView(int position, View convertView, final ViewGroup parent) {



        featured_post post_item = getItem(position);

        if(convertView == null) {
            featured_postList.clear();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.featured_fragment_activity, parent, false);

            featured_header = convertView.findViewById(R.id.featured_header);
            recyclerView = convertView.findViewById(R.id.featured_recyclerView);
            recyclerView.setLayoutManager(new NpaLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            user = post_item.getUser();
            Log.d(TAG, "getView: " + user);

            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseUser = mFirebaseAuth.getCurrentUser();
            mDatabaseUsers = FirebaseDatabase.getInstance().getReference("users");
            mDatabaseUploads = FirebaseDatabase.getInstance().getReference("uploads");
            //mUserId = mFirebaseUser.getUid();
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();

            uploads = new ArrayList<>();
/*
            adapter = new recycler_adapter(context,uploads,storageRef,username);

            recyclerView.setAdapter(adapter);
            */

            featured_layout = convertView.findViewById(R.id.search_layout);

            featured_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    featured_fragment.changeFragments(user);
                }
            });
/*
            featured_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    android.support.v4.app.Fragment fragment = null;
                    android.support.v4.app.FragmentManager fragmentManager = ((FragmentActivity)getContext()).getSupportFragmentManager();
                    Class fragmentClass = profile_fragment.class;
                    try {
                        fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("user",user);
                    fragment.setArguments(bundle);

                    fragmentManager.beginTransaction().replace(R.id.flContent,fragment).addToBackStack(null).commit();
                }
            });
            */


            //TODO find a way to save the data so you don't have to download all of it each time the user clears the SearchView


            username = "Dogs";
            featured_header.setText(username);
            mDatabaseUploads.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                    Upload upload = dataSnapshot.getValue(Upload.class);
                    uploads.add(upload);

                    //adapter.notifyDataSetChanged();
                    adapter = new recycler_adapter(context, uploads, storageRef, username);

                    recyclerView.setAdapter(adapter);


                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Upload upload = dataSnapshot.getValue(Upload.class);
                    uploads.remove(upload);
                    adapter = new recycler_adapter(context, uploads, storageRef, username);
                    Log.d(TAG, "onChildRemoved: Removed");
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            //Glide.with(getContext()).using(new FirebaseImageLoader()).load(storageRef).placeholder(R.drawable.placeholder);


        }

        return convertView;
    }

}
