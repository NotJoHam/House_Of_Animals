package vanlandingham.friendimals.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;

import vanlandingham.friendimals.Model.Upload;
import vanlandingham.friendimals.Model.User;
import vanlandingham.friendimals.NpaLinearLayoutManager;
import vanlandingham.friendimals.R;
import vanlandingham.friendimals.fragments.featured_fragment;

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

    private DatabaseReference mDatabaseUploads;
    private List<Upload> uploads;
    private String username;
    private User user;
    private RelativeLayout featured_layout;
    private featured_fragment featured_fragment;
    private featured_post post_item;




    public featuredAdapter(Context context, ArrayList<featured_post> users,featured_fragment featured_fragment){

        super(context,0,users);
        this.context=context;
        this.count = 0;
        this.featured_fragment = featured_fragment;

    }

    public View getView(int position, View convertView, final ViewGroup parent) {

        post_item = getItem(position);

        if(convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.featured_fragment_activity, parent, false);
            featured_header = convertView.findViewById(R.id.featured_header);
            recyclerView = convertView.findViewById(R.id.featured_recyclerView);
            recyclerView.setLayoutManager(new NpaLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    featured_postList.clear();

                    user = post_item.getUser();
                    Log.d(TAG, "getView: " + user);
                    mDatabaseUploads = FirebaseDatabase.getInstance().getReference("uploads");

                    uploads = new ArrayList<>();

                    mDatabaseUploads.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                            Upload upload = dataSnapshot.getValue(Upload.class);
                            uploads.add(upload);

                            //adapter.notifyDataSetChanged();
                            adapter = new recycler_adapter(context, uploads);

                            recyclerView.setAdapter(adapter);

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            Upload upload = dataSnapshot.getValue(Upload.class);
                            uploads.remove(upload);
                            adapter = new recycler_adapter(context, uploads);
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
                }
            };
            new Thread(runnable).start();
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


            //TODO find a way to save the data so you don't have to download all of it each time the user clears the SearchView


            username = "Dogs";
            featured_header.setText(username);



            //Glide.with(getContext()).using(new FirebaseImageLoader()).load(storageRef).placeholder(R.drawable.placeholder);


        }

        return convertView;
    }

}
