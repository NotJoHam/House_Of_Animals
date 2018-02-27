package vanlandingham.friendimals.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

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

    private View view;
    private Bundle bundle;

    private long follower_count, following_count;

    private TextView followers_textView, following_textView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.profile_activity, container, false);

        follow_button = view.findViewById(R.id.follow_button);
        edit_profile_button = view.findViewById(R.id.edit_profile_button);
        tabLayout = view.findViewById(R.id.tabs);
        mViewPager = view.findViewById(R.id.upload_viewPager);
        followers_textView = view.findViewById(R.id.followers_textView);
        following_textView = view.findViewById(R.id.following_textView);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        bundle = this.getArguments();
        user = bundle.getParcelable("user");
        curr_user = bundle.getParcelable("curr_user");
        Log.d(TAG, "onResume: " + user);

        username = user.getUsername();
        uid = user.getUid();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                setFollowersandFollowing();
                adapter = new SectionsPagerAdapter(getChildFragmentManager(),uid);

                mViewPager.setAdapter(adapter);
            }
        };
        new Thread(runnable).start();

        mViewPager.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {

            }
        });

        follow_button.setText("Follow");


        if( uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            follow_button.setVisibility(view.INVISIBLE);
            edit_profile_button.setVisibility(view.VISIBLE);

        }
        else {
            check_if_following();
            edit_profile_button.setVisibility(view.INVISIBLE);
            follow_button.setVisibility(view.VISIBLE);

        }

        follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_following) {
                    FirebaseFirestore.getInstance().collection("users").document(curr_user.getUid()).collection("following").document(user.getUid()).delete().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    FirebaseFirestore.getInstance().collection("users").document(user.getUid()).collection("followers").document(curr_user.getUid()).delete().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    follow_button.setText("Follow");
                    is_following = false;
                }
                else {
                    FirebaseFirestore.getInstance().collection("users").document(curr_user.getUid()).collection("following").document(user.getUid()).set(user).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    FirebaseFirestore.getInstance().collection("users").document(user.getUid()).collection("followers").document(curr_user.getUid()).set(curr_user).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
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

    }

    private void check_if_following() {

        Log.d(TAG, "check_if_following: user.getUid(): " + user.getUid());
        Log.d(TAG, "check_if_following: curr_user.getUid" + curr_user.getUid());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        FirebaseFirestore.getInstance().collection("users").document(curr_user.getUid()).collection("following").whereEqualTo("uid",user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e == null && !documentSnapshots.isEmpty()) {
                    follow_button.setText("Following");
                    Log.d(TAG, "onEvent: Executing");
                    is_following = true;
                }
                else if (e != null) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        Log.d(TAG, "check_if_following: " + is_following);


    }

    private void setFollowersandFollowing() {


        FirebaseFirestore.getInstance().collection("users").document(user.getUid()).collection("profile_values").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                for (DocumentSnapshot document : documentSnapshots) {
                    Map<String,Object> documentData = document.getData();
                    follower_count = (Long) documentData.get("follower_count");
                    following_count = (Long) documentData.get("following_count");
                    followers_textView.setText(Long.toString(follower_count));
                    following_textView.setText(Long.toString(following_count));
                }
            }
        });

    }




}
