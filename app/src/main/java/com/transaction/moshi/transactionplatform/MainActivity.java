package com.transaction.moshi.transactionplatform;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.transaction.moshi.transactionplatform.base.BaseActivity;
import com.transaction.moshi.transactionplatform.fragment.FoundFragment;
import com.transaction.moshi.transactionplatform.fragment.TransactionFtagment;
import com.transaction.moshi.transactionplatform.fragment.UserCenterFragment;
import com.transaction.moshi.transactionplatform.net.Api;
import com.transaction.moshi.transactionplatform.utils.ToastUtils;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    @Bind(R.id.line)
    View line;
    @Bind(R.id.tv_title)
    TextView mTitle;
    @Bind(R.id.ll_transaction)
    LinearLayout llTransaction;
    @Bind(R.id.ll_found)
    LinearLayout llFound;
    @Bind(R.id.ll_user_center)
    LinearLayout llUserCenter;
    @Bind(R.id.bottom)
    RelativeLayout bottom;
    @Bind(R.id.contain)
    FrameLayout contain;

    private int containId = R.id.contain;
    // 退出时间
    private long currentBackPressedTime = 0;
    // 退出间隔
    private static final int BACK_PRESSED_INTERVAL = 2000;

    @Override
    protected void loadData() {

    }

    @Override
    protected void configViews() {
        initTransactionTab();
    }

    @Override
    protected void initDatas() {
        // TODO: 2016/11/2 此处是测试网络请求的代码
        OkGo.get(Api.GET_ACCEPT_ADDR)     // 请求方式和请求url
                .tag(this)
                // 请求的 tag, 主要用于取消对应的请求
//                .cacheKey("cacheKey")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
//                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        // s 即为所需要的结果
                        showToast(s);
                    }

                });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initToolBar() {
        mCommonToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.transaction));
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
                currentBackPressedTime = System.currentTimeMillis();
                ToastUtils.showLongToast(this, getString(R.string.exit_tips));
                return true;
            } else {
                finish(); // 退出
            }
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @OnClick({R.id.ll_transaction, R.id.ll_found, R.id.ll_user_center, R.id.contain})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_transaction:
                initTransactionTab();
                break;
            case R.id.ll_found:
                initFoundTab();
                break;
            case R.id.ll_user_center:
                initUserTab();
                break;
            case R.id.contain:
                break;
        }
    }

    private void initTransactionTab() {
        addFragment(containId, TransactionFtagment.class, false);
        llTransaction.setSelected(true);
        if (llFound.isSelected() | llUserCenter.isSelected()) {
            llFound.setSelected(false);
            llUserCenter.setSelected(false);
        }
    }

    private void initFoundTab() {
        addFragment(containId, FoundFragment.class, true);
        llFound.setSelected(true);
        if (llTransaction.isSelected() | llUserCenter.isSelected()) {
            llTransaction.setSelected(false);
            llUserCenter.setSelected(false);
        }
    }

    public void initUserTab() {
        addFragment(containId, UserCenterFragment.class, true);
        llUserCenter.setSelected(true);
        if (llTransaction.isSelected() | llFound.isSelected()) {
            llTransaction.setSelected(false);
            llFound.setSelected(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
