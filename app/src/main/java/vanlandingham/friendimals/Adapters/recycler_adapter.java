package vanlandingham.friendimals.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vanlandingham.friendimals.Model.Upload;
import vanlandingham.friendimals.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Owner on 11/18/2017.
 */

public class recycler_adapter extends RecyclerView.Adapter<recycler_adapter.MyViewHolder> {
    private List<featured_post> featuredList;
    private Context context;
    private List<Upload> uploads;
    private int count = 0;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;
    private FirebaseStorage storage;
    private StorageReference selfieRef;
    private StorageReference storageRef;
    private StorageReference dogRef;
    private Bitmap bitmap;
    private String username;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public ImageView image;
        public MyViewHolder(View view) {

            super(view);
            this.text = view.findViewById(R.id.my_text);
            this.image = view.findViewById(R.id.recycler_imageView);

        }
    }

    public recycler_adapter(Context context, List<Upload> uploadList, StorageReference storageRef,String username) {
        this.uploads = uploadList;
        this.context = context;
        this.storageRef = storageRef;
        this.username = username;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(context).inflate(R.layout.recycler_activity, null);
            ImageView image = itemView.findViewById(R.id.recycler_imageView);

        //BitmapDrawable drawable = new BitmapDrawable(parent.getResources(),bitmap);
        //image.setBackground(drawable);
            //Glide.with(context).using(new FirebaseImageLoader()).load(selfieRef).into(image);
            //Glide.with(context).using(new FirebaseImageLoader()).load(dogRef).into(image);
            MyViewHolder viewHolder = new MyViewHolder(itemView);
            ++count;
            return viewHolder;


    }

    //This only sets the Text. It is bug free.
    @Override
    public void onBindViewHolder(MyViewHolder holder,int position) {



        Upload upload = uploads.get(position);


        //featured_post featured_post = featuredList.get(position);
        //Log.d(TAG, "url: " + featured_post.getUrl());
        //Glide.with(context).using(new FirebaseImageLoader()).load(dogRef).into(holder.image);
        Glide.with(context).load(upload.getUrl()).into(holder.image);
        //Glide.with(context).load(featured_post.getUrl()).into(holder.image);
        holder.text.setText(upload.getUsername().toString());
        //holder.text.setText(username);

    }

    @Override
    public int getItemCount() {

        return uploads.size();
    }


}
