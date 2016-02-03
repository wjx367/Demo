package com.example.administrator.demo.activity.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.administrator.demo.APP;
import com.example.administrator.demo.G;
import com.example.administrator.demo.Operation;
import com.example.administrator.demo.R;
import com.example.administrator.demo.activity.SystemStatusManager;
import com.example.administrator.demo.activity.sbackapp.SwipeBackActivity;
import com.example.administrator.demo.activity.sbackapp.SwipeBackLayout;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;

public abstract class BaseActivity extends SwipeBackActivity {

	public static final int REQ_CODE_LOGIN = 1;
	public static final int RET_CODE_LOGIN_SUCCESS = 1;
	public static final int RET_CODE_LOGIN_CANCEL = 2;
	public static final int REQ_TOLOGIN = 100;//先登录后跳转标记
	protected static final Logger logger = LoggerFactory.getLogger();
	protected Intent afterLoginIntent;
	BaseActivity base;
	private SwipeBackLayout mSwipeBackLayout;


	/**当前Activity的弱引用，防止内存泄露**/
	private WeakReference<Activity> context = null;
	
	/**共通操作**/
	private Operation mBaseOperation = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus();
		mSwipeBackLayout = getSwipeBackLayout();
		// 设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		//将当前Activity压入栈
		context = new WeakReference<Activity>(this);
		mBaseOperation = new Operation(this);
		ViewUtils.inject(this);
		initTitle();
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); // 友盟流量统计
		MobclickAgent.onResume(this);
		initData();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen");
		MobclickAgent.onPause(this);
	}

	@Override
	public void finish() {
		super.finish();
		if (G.isActivityAnimationEnabled && android.os.Build.VERSION.SDK_INT >= 5) {
			overridePendingTransition(
					R.anim.translate_between_interface_left_in,
					R.anim.translate_between_interface_right_out);
		}
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		if (G.isActivityAnimationEnabled && android.os.Build.VERSION.SDK_INT >= 5) {
			overridePendingTransition(
					R.anim.translate_between_interface_right_in,
					R.anim.translate_between_interface_left_out);
		}
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		if (G.isActivityAnimationEnabled && android.os.Build.VERSION.SDK_INT >= 5) {
			overridePendingTransition(
					R.anim.translate_between_interface_right_in,
					R.anim.translate_between_interface_left_out);
		}
	}

	//初始化数据
	public abstract void initData();

	//初始化视图
	public abstract  void initView();

	//初始化标题
	public abstract void initTitle();
	/**
	 * 设置状态栏背景状态
	 */
	@SuppressLint("InlinedApi")
	private void setTranslucentStatus() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);
		}
		SystemStatusManager tintManager = new SystemStatusManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(0);// 状态栏无背景
	}

	@Override
	public APP getApplicationContext() {
		// TODO Auto-generated method stub
		return (APP) super.getApplicationContext();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == REQ_CODE_LOGIN) {
			if (resultCode == RET_CODE_LOGIN_SUCCESS) {
				this.startActivity(this.afterLoginIntent);
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

//	public void startActivityAfterLogin(Intent intent) {
//		this.afterLoginIntent = intent;
//		if (this.getApplicationContext().hasLogin()) {
//			this.startActivity(this.afterLoginIntent);
//			overridePendingTransition(R.anim.translate_between_interface_right_in, R.anim.translate_between_interface_left_out);
//		} else {
//			Intent loginIntent = new Intent();
//			loginIntent.setClass(this, MapActivity.class);
//			loginIntent.putExtra("setFnum", REQ_TOLOGIN);
//			this.startActivityForResult(loginIntent, REQ_CODE_LOGIN);
//			MyToast.toast(R.string.tips_not_logged_in);
//			overridePendingTransition(R.anim.translate_between_interface_right_in, R.anim.translate_between_interface_left_out);
//		}
//	}
	
	/**
	 * 获取共通操作机能
	 */
	public Operation getOperation(){
		return this.mBaseOperation;
	}
	
	/**
	 * 当ui元素都不可见时，如切换到另一个app，进行资源释放操作
	 */
	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		switch (level) {
		case TRIM_MEMORY_UI_HIDDEN:
			// 进行资源释放操作
			break;
		}
	}
	
	/**
	 * toast提示错误
	 * @param msg
	 */
	public void onError(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private ProgressDialog progressDialog;
	public void showProgress(int messageResId) {
		showProgress(getString(messageResId));
	}
	public void showProgress(String message) {
		showProgress(message, false);
	}
	public void showProgress(String message, boolean cancelable) {
		try {
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(this);
				progressDialog.setMessage(message);
				progressDialog.setCancelable(cancelable);
				progressDialog.show();
			}
			else {
				progressDialog.setMessage(message);
				progressDialog.setCancelable(cancelable);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void hideProgress() {
		if (progressDialog != null) {
			try {
				progressDialog.dismiss();
				progressDialog = null;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onBackPressed() {
		hideProgress();
		super.onBackPressed();
	}
}
