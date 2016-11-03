package com.transaction.moshi.transactionplatform.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.transaction.moshi.transactionplatform.Constant;
import com.transaction.moshi.transactionplatform.NewApplication;
import com.transaction.moshi.transactionplatform.R;
import com.transaction.moshi.transactionplatform.utils.ToastUtils;

import butterknife.ButterKnife;


public abstract class BaseActivity extends ForBaseActivity {

    public Toolbar mCommonToolbar;

    protected TextView toolbar_title;

    protected ImageView right_buttom;

    protected Toast mToast;

    protected ProgressDialog bar;
    private Dialog progressDialog;
    protected String TAG;
    protected FragmentManager fragmentManager;
    private TextView mTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        NewApplication.pushActivity(this);
        fragmentManager = getSupportFragmentManager();
        mCommonToolbar = ButterKnife.findById(this, R.id.common_toolbar);
        mTitle = ButterKnife.findById(this, R.id.tv_title);
        if (mCommonToolbar != null) {
            initToolBar();
            setSupportActionBar(mCommonToolbar);
        }
        initDatas();      //初始化变量
        configViews();    //加载布局
        loadData();       //调用接口
        init();
    }

    protected abstract void loadData();

    protected abstract void configViews();

    protected abstract void initDatas();

    protected abstract int getLayoutId();

    protected abstract void initToolBar();


    protected void init() {

        TAG = getClass().getSimpleName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解綁
        ButterKnife.unbind(this);
        //清除activity
        NewApplication.popActivity(this);
        //根据 Tag 取消请求
        OkGo.getInstance().cancelTag(this);
    }

    //封装Intent，但是需要携带数据还是需要重新写intent
    public void go(Class cls) {
        startActivity(new Intent(this, cls));

    }

    //String ----> text show
    public void showToast(CharSequence text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_LONG);

        }
        mToast.setText(text);
        mToast.show();

    }

    //getResources -----> text show
    public void showToast(@StringRes int res) {
        showToast(getResources().getText(res));

    }

    //show ProgressDialog
    public void showProgress(String msg) {
        if (bar == null) {
            bar = new ProgressDialog(this);
            bar.setMessage(msg);
            bar.setIndeterminate(true);
            bar.setCancelable(false);
        }

        if (bar.isShowing()) {
            bar.setMessage(msg);
        }
        bar.show();

    }

    //hide ProgressDialog
    public void dismissDialog() {
        if (bar != null && bar.isShowing()) {
            bar.setCancelable(false);
            bar.dismiss();
        }

    }

    //click menu and event
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 1, 设置TAG 2, 避免重复创建
     *
     * @param containId 存放fragment布局的布局id
     * @param clz       要存放 fragment_transaction 的 类名
     */
    public void replaceFragment(int containId, Class<? extends Fragment> clz) {
        replaceFragmentWithArgs(containId, clz, null);
    }

    public void replaceFragmentWithArgs(int containId, Class<? extends Fragment> clz, Bundle bundle) {
        String TAG = clz.getSimpleName();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {
            try {
                fragment = clz.newInstance();
                if (bundle != null) {
                    fragment.setArguments(bundle);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
        }

        fragmentManager.beginTransaction()
                .replace(containId, fragment, TAG)
                .addToBackStack(TAG)
                .commitAllowingStateLoss();
    }

    protected void addFragment(int containId, Class<?> clz, boolean isAddBackStack) {
        addFragment(containId, clz, isAddBackStack, null);
    }

    protected void addFragment(int containId, Class<?> clz, boolean isAddBackStack, Bundle bundle) {
        String TAG = clz.getSimpleName();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {
            try {
                fragment = (Fragment) clz.newInstance();
                if (bundle != null) {
                    fragment.setArguments(bundle);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        if (fragment_transaction != null && fragment_transaction.isAdded()){
//            transaction.add(containId, fragment_transaction, TAG);
//        }
//        if (isAddBackStack){
//            transaction.addToBackStack(TAG);
//        }
//        transaction.commit();
        if (fragmentManager.getFragments() != null && fragmentManager.getFragments().size() >= 1) {
            for (Fragment f : fragmentManager.getFragments()) {
                fragmentManager.beginTransaction()
                        .hide(f)
                        .commit();
            }
        }

        if (fragment != null && fragment.isAdded()) {
            fragmentManager.beginTransaction()
                    .show(fragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .add(containId, fragment, TAG)
                    .addToBackStack(isAddBackStack ? TAG : null)
                    .show(fragment)
                    .commitAllowingStateLoss();
        }
    }


    /**
     * 跳转页面,无extra简易型
     *
     */
    /*public void intent2Activity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(this, tarActivity);
        startActivity(intent);
    }*/
    public void showToast(String msg) {
        ToastUtils.showToast(this, msg, Toast.LENGTH_SHORT);
    }

    public void showLog(String msg) {
        Log.i(TAG, msg);
    }

    public void showProgressDialog() {
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }


    protected void startActivityForRes(Class TotalActivity, int flag) {
        Intent intent = new Intent(this, TotalActivity);
        startActivityForResult(intent, flag);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param TotalActivity 要跳转的目标Activity
     */
    protected void startActivity(Class TotalActivity) {
        Intent intent = new Intent(this, TotalActivity);
        this.startActivity(intent);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param data           Activity之间传递数据，Intent的Extra key为Constant.EXTRA_NAME.DATA
     * @param targetActivity 要跳转的目标Activity
     */
    protected final void startActivityWithData(@NonNull Bundle data, @NonNull Class<?> targetActivity) {
        final Intent intent = new Intent();
        intent.putExtra(Constant.EXTRA_NAME, data);
        intent.setClass(this, targetActivity);
        startActivity(intent);
    }

    protected void startActivityForResultWithData(Class TotalActivity, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, TotalActivity);
        intent.putExtras(bundle);
        this.startActivityForResult(intent, requestCode);
    }


}
