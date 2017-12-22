package vanlandingham.friendimals.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import vanlandingham.friendimals.Adapters.SectionsPagerAdapter;
import vanlandingham.friendimals.Model.User;
import vanlandingham.friendimals.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Owner on 12/9/2017.
 */

public class profile_fragment extends Fragment {

    private ImageView profile_image;
    private TextView profile_username;
    private String username;
    private Bitmap bitmap;
    private BitmapDrawable bitmapDrawable;
    private RoundedBitmapDrawable dr;
    private User user;
    private User curr_user;
    private String uid;
    private Button follow_button;
    private Button edit_profile_button;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private SectionsPagerAdapter adapter;
    private boolean is_following = false;

    private int follower_count, following_count;

    private TextView followers_textView, following_textView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_activity, container, false);

        follow_button = view.findViewById(R.id.follow_button);
        edit_profile_button = view.findViewById(R.id.edit_profile_button);
        tabLayout = view.findViewById(R.id.tabs);
        mViewPager = view.findViewById(R.id.viewPager_container);
        followers_textView = view.findViewById(R.id.followers_textView);
        following_textView = view.findViewById(R.id.following_textView);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        Bundle bundle = this.getArguments();
        user = bundle.getParcelable("user");
        curr_user = bundle.getParcelable("curr_user");

        username = user.getUsername();
        uid = user.getUid();
        adapter = new SectionsPagerAdapter(getChildFragmentManager(),uid);

        mViewPager.setAdapter(adapter);

        mViewPager.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {

            }
        });

        follow_button.setText("Follow");
        check_if_following();
        setFollowersandFollowing();

        if( uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            follow_button.setVisibility(view.INVISIBLE);
            edit_profile_button.setVisibility(view.VISIBLE);

        }
        else {
            edit_profile_button.setVisibility(view.INVISIBLE);
            follow_button.setVisibility(view.VISIBLE);

        }

        follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (is_following) {
                    FirebaseDatabase.getInstance().getReference("users").child(curr_user.getUid()).child("following").child(user.getUid()).removeValue();
                    FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("followers").child(curr_user.getUid()).removeValue();
                    follow_button.setText("Follow");
                    is_following = false;
                }
                else {
                    FirebaseDatabase.getInstance().getReference("users").child(curr_user.getUid()).child("following").child(user.getUid()).setValue(user);
                    FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("followers").child(curr_user.getUid()).setValue(curr_user);
                    follow_button.setText("Following");
                    is_following = true;
                }
            }
        });

        profile_image = view.findViewById(R.id.profile_imageView);
        profile_username = view.findViewById(R.id.profile_username);
        profile_username.setText(username);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) profile_image.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        dr.setCornerRadius(2500);

        profile_image.setImageDrawable(dr);


        return view;
    }

    private void check_if_following() {

        Log.d(TAG, "check_if_following: user.getUid(): " + user.getUid());
        Log.d(TAG, "check_if_following: curr_user.getUid" + curr_user.getUid());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query query = reference.child(curr_user.getUid()).child("following").orderByChild("uid").equalTo(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: found username:" + singleSnapshot.getValue(User.class).getUsername());
                    follow_button.setText("Following");
                    is_following = true;
                    Log.d(TAG, "onDataChange: is_following: " + is_following);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Log.d(TAG, "check_if_following: " + is_following);


    }

    private void setFollowersandFollowing() {

        FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                follower_count = dataSnapshot.child("follower_count").getValue(Integer.class);
                followers_textView.setText(Integer.toString(follower_count));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                following_count = dataSnapshot.child("following_count").getValue(Integer.class);
                following_textView.setText(Integer.toString(following_count));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



}
