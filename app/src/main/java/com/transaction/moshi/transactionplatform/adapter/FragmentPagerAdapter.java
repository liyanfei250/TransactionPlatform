package com.transaction.moshi.transactionplatform.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2015/12/7.
 */
public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private List<Fragment> mViewList;
    private List<String> mTitleList;
    private FragmentManager manager;


    public FragmentPagerAdapter(FragmentManager manager, List<Fragment> mViewList, List<String> mTitleList) {
        super(manager);
        this.manager = manager;
        this.mViewList = mViewList;
        this.mTitleList = mTitleList;
    }

    @Override
    public Fragment getItem(int position) {
        return mViewList.get(position);
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);//页卡标题
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}

