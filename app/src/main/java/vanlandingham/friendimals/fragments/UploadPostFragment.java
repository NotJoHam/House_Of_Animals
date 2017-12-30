package vanlandingham.friendimals.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vanlandingham.friendimals.Model.Upload;
import vanlandingham.friendimals.Model.User;
import vanlandingham.friendimals.R;

/**
 * Created by Owner on 12/28/2017.
 */

public class UploadPostFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static String Muid;
    private User curr_user;

    public  UploadPostFragment() {}

    public UploadPostFragment newInstance(int sectionNumber,String uid,User curr_user) {
        UploadPostFragment fragment = new UploadPostFragment();
        this.Muid = uid;
        this.curr_user = curr_user;
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_post_layout, null);

        return view;
    }
}
