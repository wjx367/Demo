package com.example.administrator.demo.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.demo.G;
import com.example.administrator.demo.Operation;
import com.example.administrator.demo.R;
import com.example.administrator.demo.activity.SystemStatusManager;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("ValidFragment")
public class BaseFragment extends Fragment {
	private int layoutResId;//界面布局的资源id

	public static final int REQ_CODE_LOGIN = 1;

	public static final int RET_CODE_LOGIN_SUCCESS = 1;
	public static final int RET_CODE_LOGIN_CANCEL = 2;
	protected static final Logger logger = LoggerFactory.getLogger();
	protected Intent afterLoginIntent;
	private Operation mBaseOperation = null;
	private View contentView;

	public BaseFragment() {
		setRetainInstance(true);
	}

	public BaseFragment(int layoutResId) {
		this.layoutResId = layoutResId;
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setTranslucentStatus();
		mBaseOperation = new Operation(getActivity());
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		if (G.isActivityAnimationEnabled && Build.VERSION.SDK_INT >= 5) {
			getActivity().overridePendingTransition(
					R.anim.translate_between_interface_right_in,
					R.anim.translate_between_interface_left_out);
		}
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent,requestCode);
		if (G.isActivityAnimationEnabled && Build.VERSION.SDK_INT >= 5) {
			getActivity().overridePendingTransition(
					R.anim.translate_between_interface_right_in,
					R.anim.translate_between_interface_left_out);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(getActivity());
		MobclickAgent.onPageStart("MainScreen");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen");
		MobclickAgent.onPause(getActivity());
	}

	@Override
	@Deprecated
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (layoutResId > 0) {
			contentView = inflater.inflate(layoutResId, container, false);
			prepareContentView(contentView);
			return contentView;
		}
		return null;
	}

	protected View getContentView() {
		return this.contentView;
	}


	protected void prepareContentView(View contentView) {
		//子类实现
	}

	/**
	 * 设置状态栏背景状态
	 */
	@SuppressLint("InlinedApi")
	private void setTranslucentStatus() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window win = getActivity().getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);
		}
		SystemStatusManager tintManager = new SystemStatusManager(
				this.getActivity());
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(0);// 状态栏无背景
	}

	/**
	 * 获取共通操作机能
	 */
	public Operation getOperation() {
		return this.mBaseOperation;
	}


}
