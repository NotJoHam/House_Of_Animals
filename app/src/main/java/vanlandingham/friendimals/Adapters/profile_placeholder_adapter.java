package vanlandingham.friendimals.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vanlandingham.friendimals.Model.Post;
import vanlandingham.friendimals.Model.Upload;
import vanlandingham.friendimals.R;

/**
 * Created by Owner on 12/20/2017.
 */

public class profile_placeholder_adapter extends ArrayAdapter<Upload> {

    private TextView post_message;
    private TextView post_username;
    private ImageView post_image;


    public profile_placeholder_adapter(Context context, List<Upload> user_uploads) {
        super(context,0,user_uploads);
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        Upload upload = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_layout,parent,false);

        post_username = convertView.findViewById(R.id.post_username);
        post_message = convertView.findViewById(R.id.post_message);
        post_image = convertView.findViewById(R.id.post_imageView);


        //Glide.with(getContext()).load(upload.getUrl()).into(post_image);
        Glide.with(getContext()).load(upload.getUrl()).into(post_image);

        post_message.setText(upload.getFilename().toString());
        post_username.setText(upload.getUsername().toString());

        return convertView;
    }
}
