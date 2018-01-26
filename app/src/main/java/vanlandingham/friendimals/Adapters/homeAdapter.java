package vanlandingham.friendimals.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;

import vanlandingham.friendimals.Model.Post;
import vanlandingham.friendimals.Model.Upload;
import vanlandingham.friendimals.Model.User;
import vanlandingham.friendimals.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Owner on 11/17/2017.
 */

public class homeAdapter extends ArrayAdapter<Upload> {

    private String username;
    private TextView post_message;
    private TextView post_time;
    private TextView post_username;
    private ImageView post_image;


    public homeAdapter(Context context, ArrayList<Upload> uploads){

        super(context,0,uploads);

    }

    static class ViewHolder {
        private TextView post_message;
        private TextView post_time;
        private TextView post_username;
        private ImageView post_image;

    }

    public View getView(int position, View convertView, ViewGroup parent) {



        Upload upload = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_layout,parent,false);

        ViewHolder holder = new ViewHolder();
        holder.post_message =  convertView.findViewById(R.id.post_message);
        holder.post_username = convertView.findViewById(R.id.post_username);
        holder.post_image = convertView.findViewById(R.id.post_imageView);
        holder.post_time = convertView.findViewById(R.id.post_time_textView);
        convertView.setTag(holder);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(upload.getTimestamp());

        holder.post_message.setText(upload.getMessage());
        holder.post_username.setText(upload.getUsername());
        holder.post_time.setText(calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) );

        Glide.with(getContext()).load(upload.getUrl()).into(holder.post_image);


        return convertView;
    }

}
