package com.example.fixit.Adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList = new ArrayList<>();

    public  FragPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position >= 0 && position < mFragmentList.size())
            return mFragmentList.get(position);
        return null; //does not happen
    }

    @Override
    public int getCount(){
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment)
    {
        mFragmentList.add(fragment);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            String title = "List View";
            return title.subSequence(title.lastIndexOf(".") + 1, title.length());
        }
        else {
            String title = "Map View";
            return title.subSequence(title.lastIndexOf(".") + 1, title.length());
        }
    }
}
