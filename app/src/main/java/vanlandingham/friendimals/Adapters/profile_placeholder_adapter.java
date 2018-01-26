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
import java.util.Calendar;
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

    static class ViewHolder {
        private TextView post_message;
        private TextView post_time;
        private TextView post_username;
        private ImageView post_image;

    }


    public profile_placeholder_adapter(Context context, List<Upload> user_uploads) {
        super(context,0,user_uploads);
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        Upload upload = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_layout,parent,false);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(upload.getTimestamp());

        ViewHolder holder = new ViewHolder();
        holder.post_username = convertView.findViewById(R.id.post_username);
        holder.post_message = convertView.findViewById(R.id.post_message);
        holder.post_image = convertView.findViewById(R.id.post_imageView);
        holder.post_time = convertView.findViewById(R.id.post_time_textView);


        //Glide.with(getContext()).load(upload.getUrl()).into(post_image);
        Glide.with(getContext()).load(upload.getUrl()).into(holder.post_image);

        holder.post_message.setText(upload.getMessage().toString());
        holder.post_username.setText(upload.getUsername().toString());
        holder.post_time.setText(calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) );


        return convertView;
    }
}
