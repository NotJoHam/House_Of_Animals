package vanlandingham.friendimals;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.content.Intent;

import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import vanlandingham.friendimals.Model.User;

/**
 * Created by Owner on 12/24/2017.
 */

public class first_activity extends AppCompatActivity  {

    private ProgressBar startup_progress;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0,R.anim.slide_left);
        setContentView(R.layout.activity_first);

        startup_progress = findViewById(R.id.startup_progress);

        showProgress(true);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        Log.d("first_activity", "onCreate: " + mUser);


        if (mUser == null) {
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
        }

        else {

            String mUserId = FirebaseAuth.getInstance().getUid();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();



            DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(mUserId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user = documentSnapshot.toObject(User.class);

                    username = user.getUsername();
                    Intent intent = new Intent(getBaseContext(),Home.class);
                    intent.putExtra("curr_user",user);
                    startActivity(intent);
                    showProgress(false);
                }
            });



        }



    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            startup_progress.setVisibility(show ? View.VISIBLE : View.GONE);
            startup_progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    startup_progress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            startup_progress.setVisibility(show ? View.VISIBLE : View.GONE);

        }
    }

    private void setUpWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(2000);
        getWindow().setExitTransition(slide);
    }

}
