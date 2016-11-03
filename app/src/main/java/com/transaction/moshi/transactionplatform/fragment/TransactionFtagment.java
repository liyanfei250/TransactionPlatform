package com.transaction.moshi.transactionplatform.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.transaction.moshi.transactionplatform.R;
import com.transaction.moshi.transactionplatform.adapter.FragmentPagerAdapter;
import com.transaction.moshi.transactionplatform.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by LYF on 2016/10/25.
 */
public class TransactionFtagment extends BaseFragment {
    @Bind(R.id.sliding_tabs)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    private List<String> mTitleList;
    private List<Fragment> fragments;

    @Override
    public void attachView() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_transaction;
    }

    private void init() {
        mTitleList = new ArrayList<>();//页卡标题集合
//        oneFragment = new OneFragment();
//        twoFragment = new TwoFragment();
//        threeFragment = new ThreeFragment();
        fragments = new ArrayList<>();
//        fragments.add(OneFragment);
//        fragments.add(TwoFragment);
//        fragments.add(ThreeFragment);
        mTitleList.add("自选");
        mTitleList.add("当前交易");
        mTitleList.add("历史交易");
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager(), fragments, mTitleList);
        mTabLayout.setTabsFromPagerAdapter(adapter);
        mViewPager.setAdapter(adapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
    }

    @OnClick({R.id.sliding_tabs, R.id.viewpager})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sliding_tabs:
                break;
            case R.id.viewpager:
                break;
        }
    }
}
