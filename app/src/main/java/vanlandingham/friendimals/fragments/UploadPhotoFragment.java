package vanlandingham.friendimals.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import vanlandingham.friendimals.Home;
import vanlandingham.friendimals.Model.Upload;
import vanlandingham.friendimals.Model.User;
import vanlandingham.friendimals.R;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * Created by Owner on 12/28/2017.
 */

public class UploadPhotoFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private String Muid;
    private static final int PICK_IMAGE_REQUEST = 234;
    private final int PERMISSION_READ_EXTERNAL=111;
    private Context applicationContext;
    private Uri filePath;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    private FirebaseFirestore firestore;
    private String username;
    private String mUserId;

    private User curr_user;

    private ImageButton chooseImageButton;
    private EditText photo_description;
    private Button upload_button;

    public UploadPhotoFragment() {}

    public  UploadPhotoFragment newInstance(int sectionNumber,String uid,User user) {
        UploadPhotoFragment fragment = new UploadPhotoFragment();
        curr_user = user;
        Log.d(TAG, "newInstance: added user" + curr_user);
        this.Muid = uid;
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_picture_layout,null);

        savedInstanceState = this.getArguments();

        curr_user = savedInstanceState.getParcelable("curr_user");

        chooseImageButton = view.findViewById(R.id.choose_image_button);
        photo_description = view.findViewById(R.id.photo_description);
        upload_button = view.findViewById(R.id.upload_button);

        applicationContext = Home.getContextOfApplication();
        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserId = FirebaseAuth.getInstance().getUid();

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieveAndSetImages();
            }
        });

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_READ_EXTERNAL);

        }
        else {

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

            upload_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UploadFile();
                }
            });

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
                    long timestamp = System.currentTimeMillis();
                    String message = photo_description.getText().toString();

                    //TODO: Create a value for the user class for the last time they posted and then only grab users with posts in the last few hours
                    Upload upload = new Upload(curr_user.getUsername(),UUID.randomUUID().toString(),taskSnapshot.getDownloadUrl().toString(),timestamp,message);

                    String uploadId = mDatabase.push().getKey();

                    //mDatabase.child("uploads").child(uploadId).setValue(upload);
                    firestore.collection("users").document(mUserId).collection("posts").document(uploadId).set(upload).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(applicationContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    firestore.collection("uploads").document(uploadId).set(upload).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(applicationContext, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                    //mDatabase.child("users").child(mUserId).child("posts").child(uploadId).setValue(upload);


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

