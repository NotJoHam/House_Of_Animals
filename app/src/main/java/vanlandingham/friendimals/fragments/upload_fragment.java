package vanlandingham.friendimals.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import vanlandingham.friendimals.Adapters.UploadPagerAdapter;
import vanlandingham.friendimals.Home;
import vanlandingham.friendimals.Model.Images;
import vanlandingham.friendimals.Model.Upload;
import vanlandingham.friendimals.Model.User;
import vanlandingham.friendimals.R;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * Created by Owner on 12/12/2017.
 */

public class upload_fragment extends android.support.v4.app.Fragment {

    private String mUserId;
    private TabLayout tabLayout;
    private ViewPager pager;
    private UploadPagerAdapter adapter;
    private View view;
    User curr_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //TODO: finish the camera layout, add functionality by letting users click on photos button or allow them to take picture
        //TODO: create two fragments separately that will populate the viewpager container when the tab is switched. Do this in the getView function of the ViewPager adapter. Finish both layouts.

        view = inflater.inflate(R.layout.camera_layout, container, false);
        savedInstanceState = this.getArguments();
        curr_user = savedInstanceState.getParcelable("curr_user");


        Log.d(TAG, "onCreateView: Got curr_user from home class:" + curr_user);

        tabLayout = view.findViewById(R.id.upload_tabs);
        pager = view.findViewById(R.id.upload_viewPager);
        mUserId = FirebaseAuth.getInstance().getUid();

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
        adapter = new UploadPagerAdapter(getChildFragmentManager(), mUserId, curr_user);
        pager.setAdapter(adapter);

        return view;
    }





}