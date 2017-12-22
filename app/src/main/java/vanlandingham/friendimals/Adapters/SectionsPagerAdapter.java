package vanlandingham.friendimals.Adapters;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import vanlandingham.friendimals.fragments.PlaceholderFragment;

/**
 * Created by Owner on 12/19/2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private String uid;

    public SectionsPagerAdapter(FragmentManager fm,String uid) {
        super(fm);
        this.uid = uid;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        return fragment.newInstance(position+1,uid);
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
