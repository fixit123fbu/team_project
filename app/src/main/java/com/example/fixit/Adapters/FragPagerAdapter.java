package com.example.fixit.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.fixit.Fragments_Top.Social;
import com.example.fixit.Fragments_Top.Trending;

public class FragPagerAdapter extends FragmentPagerAdapter {

    public FragPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new Trending(); //ChildFragment1 at position 0
            case 1:
                return new Social(); //ChildFragment2 at position 1
        }
        return null; //does not happen
    }

    @Override
    public int getCount() {
        return 2; //three fragments
    }

}
