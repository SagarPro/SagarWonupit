package sagsaguz.sagarwonupit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import sagsaguz.sagarwonupit.fragment.PostFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<Integer> colorList = new ArrayList<>();

    public ViewPagerAdapter(Context context, List<Integer> colorList) {
        super(((FragmentActivity)context).getSupportFragmentManager());
        this.colorList = colorList;
    }

    @Override
    public Fragment getItem(int position) {
        PostFragment fragment = new PostFragment();
        fragment.setColorID(colorList.get(position));
        return fragment;
    }

    public int getItemPosition(@NonNull Object item) {
        PostFragment fragment = (PostFragment)item;
        int colorID = fragment.getColorID();
        int position = colorList.indexOf(colorID);

        if (position >= 0) {
            return position;
        } else {
            return POSITION_NONE;
        }
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);
    }

}
