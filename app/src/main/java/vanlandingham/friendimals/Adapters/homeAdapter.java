package vanlandingham.friendimals.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vanlandingham.friendimals.Model.Post;
import vanlandingham.friendimals.R;

/**
 * Created by Owner on 11/17/2017.
 */

public class homeAdapter extends ArrayAdapter<Post> {

    private String username;
    public homeAdapter(Context context, ArrayList<Post> users){

        super(context,0,users);

    }

    public View getView(int position, View convertView, ViewGroup parent) {



        Post post = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_layout,parent,false);

        TextView post_message = (TextView) convertView.findViewById(R.id.post_message);
        TextView post_username = (TextView) convertView.findViewById(R.id.post_username);

        post_message.setText(post.getMessage());
        post_username.setText(post.getUsername());
        System.out.println("adding element 1");

        return convertView;
    }

}
