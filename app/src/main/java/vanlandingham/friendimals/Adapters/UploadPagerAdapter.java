package vanlandingham.friendimals.Adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import vanlandingham.friendimals.Model.User;
import vanlandingham.friendimals.fragments.PlaceholderFragment;
import vanlandingham.friendimals.fragments.UploadPhotoFragment;
import vanlandingham.friendimals.fragments.UploadPostFragment;

/**
 * Created by Owner on 12/28/2017.
 */

public class UploadPagerAdapter extends FragmentPagerAdapter {

    private String uid;
    private User user;

    public UploadPagerAdapter(FragmentManager fm, String uid,User curr_user) {
        super(fm);
        this.uid = uid;
        this.user = curr_user;

    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putParcelable("curr_user",user);

        switch (position) {
            case 0:
                UploadPostFragment uploadPostFragment = new UploadPostFragment();
                uploadPostFragment.setArguments(bundle);
                return uploadPostFragment;


            case 1:

                UploadPhotoFragment uploadPhotoFragment = new UploadPhotoFragment();
                uploadPhotoFragment.setArguments(bundle);
                return uploadPhotoFragment;

            default:
                return null;
        }

        //return PlaceholderFragment.newInstance(position+1,uid);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }



    @Override
    public int getCount() {
        return 2;
    }
}
