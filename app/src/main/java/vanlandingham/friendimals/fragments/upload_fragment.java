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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

import vanlandingham.friendimals.Home;
import vanlandingham.friendimals.Model.Images;
import vanlandingham.friendimals.Model.Upload;
import vanlandingham.friendimals.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Owner on 12/12/2017.
 */

public class upload_fragment extends android.support.v4.app.Fragment {

    private static final int PICK_IMAGE_REQUEST = 234;
    private final int PERMISSION_READ_EXTERNAL=111;
    private Context applicationContext;
    private Uri filePath;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    private ArrayList<Images> imagesArrayList = new ArrayList<>();
    private Button upload_button;
    private String username;
    private String mUserId;
    private TabLayout tabLayout;
    private ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //TODO: finish the camera layout, add functionality by letting users click on photos button or allow them to take picture
        //TODO: create two fragments separately that will populate the viewpager container when the tab is switched. Do this in the getView function of the ViewPager adapter. Finish both layouts.

        View view = inflater.inflate(R.layout.camera_layout, container, false);
        savedInstanceState = getArguments();
        username  = savedInstanceState.getString("username");

        tabLayout = view.findViewById(R.id.upload_tabs);
        pager = view.findViewById(R.id.upload_viewPager);
        applicationContext = Home.getContextOfApplication();
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserId = FirebaseAuth.getInstance().getUid();

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_READ_EXTERNAL);

        }
        else {

            upload_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    retrieveAndSetImages();

                }
            });


        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {

            case PERMISSION_READ_EXTERNAL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    retrieveAndSetImages();
                }

            }

        }
    }

    public void retrieveAndSetImages() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);

/*
        imagesArrayList.clear();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                imagesArrayList.clear();
                Cursor cursor = applicationContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,null,null,null);

                if (cursor!=null) {
                    cursor.moveToFirst();

                    for (int x=0;x < cursor.getCount();++x) {
                        cursor.moveToPosition(x);
                        Images images = new Images(Uri.parse(cursor.getString(1)));
                        imagesArrayList.add(images);
                    }

                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //set Images
                        //Update Images
                    }
                });
            }

        });
        */

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            UploadFile();
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = applicationContext.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void UploadFile() {

        if (filePath != null) {

            final ProgressBar progressBar = new ProgressBar(applicationContext,null,android.R.attr.progressBarStyleSmall);
            StorageReference sRef = storageReference.child("uploads/" + System.currentTimeMillis() + "." + getFileExtension(filePath));
            sRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_SHORT).show();
                    Upload upload = new Upload(username, UUID.randomUUID().toString(),taskSnapshot.getDownloadUrl().toString());

                    String uploadId = mDatabase.push().getKey();

                    mDatabase.child("uploads").child(uploadId).setValue(upload);
                    mDatabase.child("users").child(mUserId).child("posts").child(uploadId).setValue(upload);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(applicationContext, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    /*
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                     */
                }
            });

        }

        else {
            Toast.makeText(applicationContext, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

}
