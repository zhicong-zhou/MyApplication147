package com.jzkl.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created viewpager
 */
public class Viewpager_adapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    private FragmentManager fragmetnmanager;  //创建FragmentManager
    public Viewpager_adapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.fragmetnmanager=fm;
        this.list=list;
    }


    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
