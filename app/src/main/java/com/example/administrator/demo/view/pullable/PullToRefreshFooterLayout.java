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

public class PullToRefreshFooterLayout extends LinearLayout implements PullToRefreshLayout.RefreshableView {
	public PullToRefreshFooterLayout(Context context) {
		super(context);
		initView(context);
	}
	
	public PullToRefreshFooterLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public PullToRefreshFooterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}
	
	/**
	 * 上拉头
	 */
	private View containerView;
	/**
	 * 上拉的箭头
	 */
	private View arrowImageView;
	/**
	 * 正在加载的图标
	 */
	private View loadingImageView;
	/**
	 * 加载结果图标
	 */
	private View stateImageView;
	/**
	 * 加载结果：成功或失败
	 */
	private TextView stateTextView;
	
	/**
	 * 箭头向上旋转180度
	 */
	private Animation rotateDownAnimation;
	/**
	 * 箭头向下旋转180度
	 */
	private Animation rotateUpAnimation;
	/**
	 * 均匀旋转动画
	 */
	private RotateAnimation rotateAnimation;
	
	private void initView(Context context) {
		containerView = LayoutInflater.from(context).inflate(R.layout.playout_footer, this, true);
		setLayoutTopGravity(containerView);
		
		arrowImageView = containerView.findViewById(R.id.arrowup_imageview);
		stateTextView = (TextView) containerView.findViewById(R.id.state_textview);
		loadingImageView = containerView.findViewById(R.id.loading_imageview);
		stateImageView = containerView.findViewById(R.id.state_imageview);
	}

	/**
	 * 父容器是RelativeLayout故这里需要设置子视图位于底部
	 */
	private void setLayoutTopGravity(View view) {
		if (view instanceof LinearLayout) {
			((LinearLayout) view).setGravity(Gravity.TOP);
		}
		else if (view instanceof RelativeLayout) {
			((RelativeLayout) view).setGravity(Gravity.TOP);
		}
		else {
//			if (L.D) L.e("you need set your header.xml with parameter layout_gravity='top'");
		}
	}
	
	@Override
	public void refreshFinish(int state) {
		loadingImageView.clearAnimation();
		loadingImageView.setVisibility(View.GONE);
		switch (state)
		{
		case PullToRefreshLayout.SUCCESS:
			// 加载成功
			stateImageView.setVisibility(View.VISIBLE);
			stateTextView.setText("加载成功");
			stateImageView.setBackgroundResource(R.drawable.icon_load_success);
			break;
		case PullToRefreshLayout.FAIL:
		default:
			// 加载失败
			stateImageView.setVisibility(View.VISIBLE);
			stateTextView.setText("加载失败");
			stateImageView.setBackgroundResource(R.drawable.icon_load_failed);
			break;
		}
	}

	@Override
	public void changeState(int state) {
		switch (state)
		{
		case PullToRefreshLayout.STATE_INIT:
			// 上拉布局初始状态
			stateImageView.setVisibility(View.GONE);
			stateTextView.setText("上拉加载更多");
			arrowImageView.clearAnimation();
			if (arrowImageView.isShown()) {
				arrowImageView.startAnimation(getRotateUpAnimation());
			}
			else {
				arrowImageView.setVisibility(View.VISIBLE);
			}
			break;
		case PullToRefreshLayout.STATE_RELEASE_TO_REFRESH:
			break;
		case PullToRefreshLayout.STATE_REFRESHING:
			break;
		case PullToRefreshLayout.STATE_RELEASE_TO_LOAD:
			// 释放加载状态
			stateTextView.setText("释放立即加载");
			arrowImageView.startAnimation(getRotateDownAnimation());
			break;
		case PullToRefreshLayout.STATE_LOADING:
			// 正在加载状态
			arrowImageView.clearAnimation();
			loadingImageView.setVisibility(View.VISIBLE);
			arrowImageView.setVisibility(View.INVISIBLE);
			loadingImageView.startAnimation(getRotateAnimation());
			stateTextView.setText("正在加载");
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
		return ((ViewGroup) ((ViewGroup)containerView).getChildAt(0)).getMeasuredHeight();
	}

	@Override
	public void onMoving(float current, float total) {
//		if (L.D) L.i("footer moving current="+current+" "+((int)(current*100/total))+"%");
	}

	public Animation getRotateUpAnimation() {
		if (rotateUpAnimation == null) {
			rotateUpAnimation = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			rotateUpAnimation.setDuration(180);
			rotateUpAnimation.setFillAfter(true);
		}
		return rotateUpAnimation;
	}

	public Animation getRotateDownAnimation() {
		if (rotateDownAnimation == null) {
			rotateDownAnimation = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
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
	}
}
