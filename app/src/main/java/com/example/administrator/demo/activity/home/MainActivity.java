package com.example.administrator.demo.activity.home;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.administrator.demo.APP;
import com.example.administrator.demo.G;
import com.example.administrator.demo.R;
import com.example.administrator.demo.activity.FragmentIndicator;
import com.example.administrator.demo.activity.MyVersion;
import com.example.administrator.demo.activity.base.BaseFragmentActivity;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseFragmentActivity implements
        FragmentIndicator.FragmentIndicatorInterface{
    public static final String PARAMETER_TABID = "mainfragment_id";
    public static String TAG = "MainActivity";
    public static Fragment[] mFragments;
    public int lastId;//当前id
    public int defaultId = 0;//默认展示页面id
    // 个人中心使用

    private Thread mThread;
    MyHandler myHandler = new MyHandler(this);
    MyHandlerDown myHandler1 = new MyHandlerDown(this);
    AlertDialog dlg;

    private long exitTime = 0;

    // check update
    private String app_new_version;
    private String new_version_url;
    private SharedPreferences sp;
    private MyVersion.DataListEntity dataListEntity;
    private Map<String, String> vals = new HashMap<String, String>();
    /*
     * 保证home键和锁屏只能有一个再监听
     */
    public static boolean is_first_home = true; // 是否第一次按home键
    public static boolean home_flag = true; // 是否按下home键
    public static boolean lock_flag = true; // 是否锁屏标志

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int release = Integer.parseInt(android.os.Build.VERSION.SDK);
        G.SYSTEM_SDK_API = release;
        G.APPVERSION = getVersion();
        setContentView(R.layout.fragment_main);
        setFragmentIndicator(defaultId);
        lastId = defaultId;
        registerBoradcastReceiver();

    }

    @Override
    public void checkLogin() {

    }

    @Override
    public boolean hasLogin() {
        return false;
    }

    @Override
    public void goLogin(int lastid, int nextid) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int id = intent.getIntExtra(PARAMETER_TABID, 0);
        switchFragment(id);
    }
    private void switchFragment(int id) {
        for (int i = 0; i < 4; i++) {
            mFragments[i].onPause();
        }
        // lastId=which;
        getSupportFragmentManager().beginTransaction()
                .hide(mFragments[0]).hide(mFragments[1])
                .hide(mFragments[2]).hide(mFragments[3])
                .show(mFragments[id]).commit();
        mFragments[id].onStart();
        FragmentIndicator.setIndicator(id);
    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent intent) {
        super.onActivityResult(requestcode, resultcode, intent);
        if (requestcode == 1 && intent != null) {
            if (resultcode == 1) {
                lastId = intent.getIntExtra("nextid", 0);
            } else {
                lastId = intent.getIntExtra("lastid", 0);
            }
            switchFragment(lastId);
        }
    }

    /**
     * 2015-04-02 wang 返回键按两下退出
     *
     * @return
     * @throws Exception
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) // System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * fragment与mainactivity的交互
     */
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(G.SWITCHFRAGMENT);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(G.SWITCHFRAGMENT)) {
                int index = intent.getIntExtra("index", 0);
                getSupportFragmentManager().beginTransaction()
                        .hide(MainActivity.mFragments[0])
                        .hide(MainActivity.mFragments[1])
                        .hide(MainActivity.mFragments[2])
                        .hide(MainActivity.mFragments[3])
                        .show(MainActivity.mFragments[index]).commit();
                FragmentIndicator.setIndicator(index);
                lastId = index;
            }
        }

    };

    /**
     * 初始化fragment
     */
    private void setFragmentIndicator(int whichIsDefault) { // 默认加载第0个
        mFragments = new Fragment[4];
        mFragments[0] = getSupportFragmentManager().findFragmentById(
                R.id.fragment_home);
        mFragments[1] = getSupportFragmentManager().findFragmentById(
                R.id.fragment_invest);
        mFragments[2] = getSupportFragmentManager().findFragmentById(
                R.id.fragment_myaccount);
        mFragments[3] = getSupportFragmentManager().findFragmentById(
                R.id.fragment_more);
        getSupportFragmentManager().beginTransaction().hide(mFragments[0])
                .hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3])
                .show(mFragments[whichIsDefault]).commit();
        FragmentIndicator mIndicator = (FragmentIndicator) findViewById(R.id.indicator); // 底部的图标
        // 首页
        // 我要理财
        // 我的财产
        // 更多
        mIndicator.setFragmentIndicatorInterface(this);
        FragmentIndicator.setIndicator(whichIsDefault);

        // 监听切换 首页 我要理财 我的财产 更多
        mIndicator.setOnIndicateListener(new FragmentIndicator.OnIndicateListener() {
            @Override
            public void onIndicate(View v, int which) {
                for (int i = 0; i < 4; i++) {
                    mFragments[i].onPause();
                }
                // lastId=which;
                getSupportFragmentManager().beginTransaction()
                        .hide(mFragments[0]).hide(mFragments[1])
                        .hide(mFragments[2]).hide(mFragments[3])
                        .show(mFragments[which]).commit();
                mFragments[which].onStart();
                lastId = which;
            }
        });
    }
    /**
     * 自定义Handler
     */
    private static class MyHandlerDown extends Handler {
        private final WeakReference<MainActivity> mTarget;

        public MyHandlerDown(MainActivity aboutUsActivity) {
            mTarget = new WeakReference<MainActivity>(
                    aboutUsActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity target = mTarget.get();
            if (target != null) {
                switch (msg.what) {
                    case G.MSG_SUCCESS:
//                        Log.d("MainActivity", "DownLoad Successful");
//                        target.installApk((File) msg.obj);
                        break;
                    case G.MSG_FAILURE:
//                        Log.d("MainActivity", "DownLoad Failed");
                        break;
                    default:
//                        Log.e("MainActivity", "DownLoad Failed");
                        break;
                }
            }
        }
    }
    /**
     * 自定义Handler
     */
    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mTarget;

        public MyHandler(MainActivity activity) {
            mTarget = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            MainActivity target = mTarget.get();
            if (target != null) {
                switch (msg.what) {
                    case G.MSG_FAILURE:

                        break;
                    case G.MSG_SELF_NOT_LOGIN:
                        Map<String, String> vals = new HashMap<String, String>();
                        vals.put("isLogin", "0");
                        vals.put("jSessionId", "");
                        ((APP) target.getApplicationContext())
                                .getSession().set(vals);
                        break;
                    case G.MSG_SELF_STILL_LOGIN:
					/*
					 * target.mThread = new Thread(target.getUserDetail);
					 * target.mThread.start();//线程启动
					 */
                        break;
                    case G.MSG_NO_CONNECTED:

                        break;
                    default:

                        break;
                }
            }
        }
    }
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }
}
