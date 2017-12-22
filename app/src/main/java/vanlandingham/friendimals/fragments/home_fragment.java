package vanlandingham.friendimals.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import vanlandingham.friendimals.Model.Post;
import vanlandingham.friendimals.Adapters.homeAdapter;
import vanlandingham.friendimals.R;

/**
 * Created by Owner on 11/16/2017.
 */

public class home_fragment extends android.support.v4.app.Fragment {

    public ArrayList<String> childkey_list = new ArrayList<>();
    public ArrayList<Post> messageList = new ArrayList<>();
    private String username;

    public home_fragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.home_activity, container, false);
        savedInstanceState = getArguments();
        if (savedInstanceState != null)
            username = savedInstanceState.getString("username","username");
        System.out.println(username);
        ListView listView = view.findViewById(R.id.home_list);
        homeAdapter adapter = new homeAdapter(getContext(),messageList);
        listView.setAdapter(adapter);


        String message = "This is the home fragment";


        Post post = new Post(message,username);
        messageList.add(post);

        System.out.println("Adding post 1");
        //adapter.add(post);



        return view;

    }

}
