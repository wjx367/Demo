package com.example.administrator.demo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;


/**
 * 基本的操作共通抽取
 * @author zhiwei.yin
 * @version 1.0
 *
 */
public class Operation {

	/**激活Activity组件意图**/
	private Intent mIntent = new Intent();
	/***上下文**/
	private Activity mContext = null;
	/***整个应用Applicaiton**/
	private APP mApplication = null;
	
	public Operation(Activity mContext) {
		this.mContext = mContext;
		mApplication = (APP) this.mContext.getApplicationContext();
	}

	/**
	 * 跳转Activity
	 * @param activity 需要跳转至的Activity
	 */
	public void forward(Class<? extends Activity> activity) {
		mIntent.setClass(mContext, activity);
		mContext.startActivity(mIntent);
		//淡入淡出
//		mContext.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		//右往左推效果
		if (G.isActivityAnimationEnabled && android.os.Build.VERSION.SDK_INT >= 5) {
			mContext.overridePendingTransition(R.anim.translate_between_interface_right_in, R.anim.translate_between_interface_left_out);
		}
	}

	/**
	 * 跳转Activity
	 * @param activity 需要登录验证跳转的Activity
	 */
//	public void forwardAfterLogin(Class<? extends Activity> activity) {
//		if(mApplication.hasLogin()){
//			mIntent.setClass(mContext, activity);
//			mContext.startActivity(mIntent);
//
//		}else{
//			MyToast.toast(R.string.tips_not_logged_in);
//			mIntent.setClass(mContext, LoginActivity1.class);
//			mIntent.putExtra("setFnum", 100);
//			mContext.startActivity(mIntent);
//		}
//		if (G.isActivityAnimationEnabled && android.os.Build.VERSION.SDK_INT >= 5) {
//			mContext.overridePendingTransition(R.anim.translate_between_interface_right_in, R.anim.translate_between_interface_left_out);
//		}
//
//	}
	/**
	 * 跳转Activity
	 * @param activity 需要跳转至的Activity
	 */
	public void forwardClearTop(Class<? extends Activity> activity) {
		mIntent.setClass(mContext, activity);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		mContext.startActivity(mIntent);
		//淡入淡出
//		mContext.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		//右往左推效果
		if (G.isActivityAnimationEnabled && android.os.Build.VERSION.SDK_INT >= 5) {
			mContext.overridePendingTransition(R.anim.translate_between_interface_right_in, R.anim.translate_between_interface_left_out);
		}
	}
	
	/**
	 * 设置传递参数
	 * @param value 数据传输对象
	 */
	public void addParameter(Bundle value) {
		mIntent.putExtras(value);
	}
	
	/**
	 * 设置传递参数 zhiwei.yin
	 * @param key 参数key
	 * @param value 数据传输对象
	 */
	public void addParameterSerializable(String key,Serializable value) {
		mIntent.putExtra(key,value);
	}
	/**
	 * 获取传递参数：Bundle
	 * @param key 参数key
	 */
	public Serializable getParameterSerializable(String key) {
		return mIntent.getExtras().getSerializable(key);
	}
	
	/**
	 * 设置传递参数
	 * @param key 参数key
	 * @param value 数据传输对象
	 */
	public void addParameter(String key,Serializable value) {
		mIntent.putExtra(key, value);
	}
	
	
	/**
	 * 设置传递参数
	 * @param key 参数key
	 * @param value 数据传输对象
	 */
	public void addParameter(String key,String value) {
		mIntent.putExtra(key, value);
	}
	
	public void addParameter(String key,Integer value) {
		mIntent.putExtra(key, value);
	}
	
	public void addParameter(String key,long value) {
		mIntent.putExtra(key, value);
	}
	/**
	 * 获取传递参数  zhiwei.yin
	 * @param key 参数key
	 */
	public String getParameter(String key) {
		return mIntent.getStringExtra(key);
	}
	
	/**
	 * 获取传递参数  zhiwei.yin
	 * @param key 参数key
	 */
	public Integer getParameterInteger(String key) {
		return mIntent.getIntExtra(key, 0);
	}
	
	
	
}

