package com.example.administrator.demo.view.pullable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.demo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PullToRefreshHeaderLayout extends LinearLayout implements PullToRefreshLayout.RefreshableView {
	public PullToRefreshHeaderLayout(Context context) {
		super(context);
		initView(context);
	}
	
	public PullToRefreshHeaderLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public PullToRefreshHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}
	
	/**
	 * 下拉头
	 */
	private View containerView;
	/**
	 * 下拉的箭头
	 */
	private View arrowImageView;
	/**
	 * 正在刷新的图标
	 */
	private View loadingImageView;
	/**
	 * 正在刷新的图标所在视图
	 */
	public View loadingLayout;
	/**
	 * 刷新结果图标
	 */
	private View stateImageView;
	/**
	 * 刷新结果：成功或失败
	 */
	private TextView stateTextView;
	
	/**
	 * 箭头向上旋转180度
	 */
	private Animation rotateUpAnimation;
	/**
	 * 箭头向下旋转180度
	 */
	private Animation rotateDownAnimation;
	/**
	 * 均匀旋转动画
	 */
	private RotateAnimation rotateAnimation;
	private TextView timer_tv;
	
	private void initView(Context context) {
		if (!isInEditMode()) {
			containerView = LayoutInflater.from(context).inflate(R.layout.playout_header, this, true);
			
			setLayoutBottomGravity(containerView);

			timer_tv= (TextView) containerView.findViewById(R.id.timer_tv);
			arrowImageView = containerView.findViewById(R.id.arrowdown_imageview);
			stateTextView = (TextView) containerView.findViewById(R.id.state_textview);
			loadingLayout = containerView.findViewById(R.id.loading_layout);
			loadingImageView = containerView.findViewById(R.id.loading_imageview);
			stateImageView = containerView.findViewById(R.id.state_imageview);
		}
	}
	
	/**
	 * 父容器是RelativeLayout故这里需要设置子视图位于底部
	 */
	private void setLayoutBottomGravity(View view) {
		if (view instanceof LinearLayout) {
			((LinearLayout) view).setGravity(Gravity.BOTTOM);
		}
		else if (view instanceof RelativeLayout) {
			((RelativeLayout) view).setGravity(Gravity.BOTTOM);
		}
		else {
//			if (L.D) L.e("you need set your header.xml with parameter layout_gravity='bottom'");
		}
	}

	@Override
	public void refreshFinish(int refreshResult) {
		loadingImageView.clearAnimation();
		loadingLayout.setVisibility(View.GONE);
		switch (refreshResult)
		{
		case PullToRefreshLayout.SUCCESS:
			// 刷新成功
			stateImageView.setVisibility(View.VISIBLE);
			stateTextView.setText("刷新成功");
			stateImageView.setBackgroundResource(R.drawable.icon_load_success);
			break;
		case PullToRefreshLayout.FAIL:
		default:
			// 刷新失败
			stateImageView.setVisibility(View.VISIBLE);
			stateTextView.setText("刷新失败");
			stateImageView.setBackgroundResource(R.drawable.icon_load_failed);
			break;
		}
	}

	@Override
	public void changeState(int state) {
		SimpleDateFormat formatter    =   new    SimpleDateFormat    ("  HH:mm:ss     ");
		Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
		String    str    =    formatter.format(curDate);
		switch (state)
		{
		case PullToRefreshLayout.STATE_INIT:
			// 下拉布局初始状态
			stateImageView.setVisibility(View.GONE);
			timer_tv.setText(str);
			stateTextView.setText("下拉刷新");
			arrowImageView.clearAnimation();
			if (arrowImageView.isShown()) {
				arrowImageView.startAnimation(getRotateDownAnimation());
			}
			else {
				arrowImageView.setVisibility(View.VISIBLE);
			}
			break;
		case PullToRefreshLayout.STATE_RELEASE_TO_REFRESH:
			// 释放刷新状态
			timer_tv.setText(str);
			stateTextView.setText("释放立即刷新");
			arrowImageView.startAnimation(getRotateUpAnimation());
			break;
		case PullToRefreshLayout.STATE_REFRESHING:
			// 正在刷新状态
			timer_tv.setText(str);
			arrowImageView.clearAnimation();
			loadingLayout.setVisibility(View.VISIBLE);
			arrowImageView.setVisibility(View.INVISIBLE);
			loadingImageView.startAnimation(getRotateAnimation());
			stateTextView.setText("正在刷新");
			break;
		case PullToRefreshLayout.STATE_RELEASE_TO_LOAD:
			break;
		case PullToRefreshLayout.STATE_LOADING:
			break;
		case PullToRefreshLayout.STATE_DONE:
			// 刷新或加载完毕，啥都不做
			break;
		}
	}

	@Override
	public void onFinish() {
		arrowImageView.clearAnimation();
	}

	@Override
	public int getContentHeight() {
		int contentHeight = 0;
		if (!isInEditMode()) {
			contentHeight = ((ViewGroup) ((ViewGroup)containerView).getChildAt(0)).getMeasuredHeight();
		}
		return contentHeight;
//		return ((ViewGroup) ((ViewGroup)containerView).getChildAt(0)).getMeasuredHeight();
	}

	@Override
	public void onMoving(float current, float total) {
//		if (L.D) L.i("header moving current="+current+" "+((int)(current*100/total))+"%");
	}

	public Animation getRotateUpAnimation() {
		if (rotateUpAnimation == null) {
			rotateUpAnimation = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			rotateUpAnimation.setDuration(180);
			rotateUpAnimation.setFillAfter(true);
		}
		return rotateUpAnimation;
	}

	public Animation getRotateDownAnimation() {
		if (rotateDownAnimation == null) {
			rotateDownAnimation = new RotateAnimation(180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			rotateDownAnimation.setDuration(180);
			rotateDownAnimation.setFillAfter(true);
		}
		return rotateDownAnimation;
	}

	public RotateAnimation getRotateAnimation() {
		if (rotateAnimation == null) {
			rotateAnimation = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnimation.setDuration(1500);
			rotateAnimation.setRepeatCount(Animation.INFINITE);
			rotateAnimation.setInterpolator(new LinearInterpolator());
		}
		return rotateAnimation;
	}

	@Override
	public void initLayout(PullToRefreshLayout pullToRefreshLayout) {
		SimpleDateFormat formatter    =   new    SimpleDateFormat    ("  HH:mm:ss     ");
		Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
		String    str    =    formatter.format(curDate);
		timer_tv.setText(str);
	}

}
