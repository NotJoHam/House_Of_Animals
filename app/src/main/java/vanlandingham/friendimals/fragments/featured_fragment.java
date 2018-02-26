package vanlandingham.friendimals.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import vanlandingham.friendimals.Adapters.featuredAdapter;
import vanlandingham.friendimals.Adapters.featured_post;
import vanlandingham.friendimals.Home;
import vanlandingham.friendimals.Model.User;
import vanlandingham.friendimals.R;

/**
 * Created by Owner on 11/16/2017.
 */

public class featured_fragment extends android.support.v4.app.Fragment {

    public ArrayList<String> childkey_list = new ArrayList<>();
    private View view;

    private Query query;
    private User user;

    private List<User> mUserList;
    public  ArrayList<featured_post> featured_post_list = new ArrayList<>();
    private ListView listView;
    featuredAdapter adapter;

    public featured_fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.featured_activity, container, false);
        listView = view.findViewById(R.id.featured_list);

        featured_post post = new featured_post();
        adapter = new featuredAdapter(getContext(),featured_post_list,featured_fragment.this);
        adapter.add(post);
        listView.setAdapter(adapter);



        return view;

    }


    public void changeFragments(User user1) {
        ((Home)getActivity()).ChangeFragment(profile_fragment.class,user1);
    }

}
