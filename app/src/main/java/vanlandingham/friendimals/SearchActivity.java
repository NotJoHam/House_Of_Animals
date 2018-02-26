package vanlandingham.friendimals;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import vanlandingham.friendimals.Adapters.featured_post;
import vanlandingham.friendimals.Adapters.searchAdapter;
import vanlandingham.friendimals.Model.User;
import vanlandingham.friendimals.fragments.profile_fragment;

import static android.content.ContentValues.TAG;
import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Owner on 12/17/2017.
 */

public class SearchActivity extends AppCompatActivity{

    private List<User> mUserList;
    private EditText search_editText;
    private ListView search_listView;
    private Button back_button;
    private ImageView search_imageView;
    private User user;
    private searchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        mUserList = new ArrayList<>();
        search_editText = findViewById(R.id.search_editText);
        search_listView = findViewById(R.id.search_listView);
        back_button = findViewById(R.id.back_button);

        search_imageView = findViewById(R.id.search_imageView);

        search_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = search_editText.getText().toString();
                searchForMatch(text);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                intent.putExtra("user",user);
                intent.putExtra("call_class",0);
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        });

        search_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                final String text = search_editText.getText().toString();
                if (text.length() == 0)
                    searchForMatch(text);
                else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            searchForMatch(text);

                        }
                    }, 1000);

                }

            }
        });

    }


    public void searchForMatch(String keyword) {
        Log.d(TAG, "searchForMatch: " + keyword);
        mUserList.clear();
        if (keyword.length()==0) {
            updateList();
        }
        else {

            FirebaseFirestore.getInstance().collection("users").whereEqualTo("username",keyword).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    for (DocumentSnapshot document : documentSnapshots.getDocuments()) {
                        mUserList.add(document.toObject(User.class));
                        user = document.toObject(User.class);

                        updateList();
                    }
                }
            });
/*
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.child("users").orderByChild("username").equalTo(keyword);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Log.d(TAG, "onDataChange: found username:" + singleSnapshot.getValue(User.class).getUsername());

                        mUserList.add(singleSnapshot.getValue(User.class));
                        user = singleSnapshot.getValue(User.class);

                        updateList();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
*/

        }
    }

    public void updateList() {

        hideSoftKeyboard();
        adapter = new searchAdapter(SearchActivity.this,R.layout.searches_user_layout,mUserList,SearchActivity.this);
        search_listView.setAdapter(adapter);

        search_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                changeFragments(user);
            }
        });

        adapter.notifyDataSetChanged();

    }

    public void changeFragments(User user1) {
        Intent intent = getIntent();
        intent.putExtra("user",user1);
        intent.putExtra("call_class",1);
        setResult(RESULT_OK,intent);
        finish();
    }



    private void hideSoftKeyboard(){
        if(getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public RoundedBitmapDrawable createDr(Bitmap bitmap) {

        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        dr.setCornerRadius(2500);

        return dr;
    }
}
