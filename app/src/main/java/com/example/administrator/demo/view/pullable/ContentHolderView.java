package com.example.administrator.demo.view.pullable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.demo.MyViewUtils;
import com.example.administrator.demo.R;

public class ContentHolderView extends LinearLayout {

	public ContentHolderView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}
	public ContentHolderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	public ContentHolderView(Context context) {
		super(context);
		initView(context);
	}
	
	private View loadingView;
	private TextView loadingText;
	private ProgressBar loadingProgressBar;
//	private ImageView loadingImageView;
	
	private View emptyView;
	private TextView emptyText;
	private ImageView emptyImage;
	
	private int emptyIconResId = R.drawable.icon_tips_empty;
	private int failedIconResId = R.drawable.icon_tips_failed;
	private int networkErrorIconResId = R.drawable.icon_tips_network_error;

	private void initView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.layout_content_holder_view, this, true);
		loadingView = findViewById(R.id.loading_layout);
		loadingText = (TextView) findViewById(R.id.loading_text_view);
		loadingProgressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);
		
		emptyView = findViewById(R.id.empty_layout);
		emptyImage = (ImageView) findViewById(R.id.empty_image_view);
		emptyText = (TextView) findViewById(R.id.empty_text_view);
		
//		loadingImageView = (ImageView) findViewById(R.id.loading_imageview);
		
		MyViewUtils.hideViews(loadingView, emptyView);
	}
	
	public TextView getLoadingTextView() {
		return loadingText;
	}
	
	public ProgressBar getLoadingProgressBar() {
		return loadingProgressBar;
	}
	
	public TextView getEmptyTextView() {
		return emptyText;
	}
	
	public ImageView getEmptyImageView() {
		return emptyImage;
	}
	
	public void showLoadingView() {
		show();
		emptyView.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);
		
//		setLoadingAnimation(true);
	}
//	private void setLoadingAnimation(boolean start) {
//		AnimationDrawable animationDrawable = (AnimationDrawable) loadingImageView.getDrawable();
//		animationDrawable.stop();
//		if (start) {
//			animationDrawable.start();
//		}
//	}
	
	public void show() {
		setVisibility(View.VISIBLE);
	}
	public void hide() {
		setVisibility(View.GONE);
	}
	
	public void showEmptyView() {
		showEmptyView("当前数据为空");
	}
	public void showEmptyView(int imageResId, int textResId) {
		showEmptyView(imageResId, getContext().getString(textResId), null);
	}
	public void showEmptyView(String text) {
		showEmptyView(-1, text, null);
	}
	public void showEmptyView(String text, OnClickListener clickListener) {
		showEmptyView(-1, text, clickListener);
	}
	public void showEmptyView(int imageResId, String text) {
		showEmptyView(imageResId, text, null);
	}
	public void showEmptyView(int imageResId, int textResId, OnClickListener clickListener) {
		showEmptyView(imageResId, getContext().getString(textResId), clickListener);
	}
	public void showEmptyView(int imageResId, String text, OnClickListener clickListener) {
		show();
//		setLoadingAnimation(false);
		loadingView.setVisibility(View.GONE);
		emptyView.setVisibility(View.VISIBLE);
		
		emptyText.setText(text);
		if (imageResId > 0) {
			emptyImage.setImageResource(imageResId);
		}
		else {
			emptyImage.setImageResource(emptyIconResId);
		}
		emptyView.setOnClickListener(clickListener);
	}
	
	public void showFailedView(OnClickListener clickListener) {
		showFailedView(failedIconResId, "网络加载失败，请点击重试", clickListener);
	}
	public void showFailedView(int imageResId, int textResId, OnClickListener clickListener) {
		showEmptyView(imageResId, getContext().getString(textResId), clickListener);
	}
	public void showFailedView(int textResId, OnClickListener clickListener) {
		showEmptyView(failedIconResId, getContext().getString(textResId), clickListener);
	}
	public void showFailedView(int imageResId, String text, OnClickListener clickListener) {
		showEmptyView(imageResId, text, clickListener);
	}
	public void showFailedView(String text, OnClickListener clickListener) {
		showEmptyView(failedIconResId, text, clickListener);
	}
	public void showNetworkErrorView(OnClickListener clickListener) {
		showFailedView(networkErrorIconResId, "网络异常！请检查网络后重试", clickListener);
	}
	public void setEmptyIconResId(int emptyIconResId) {
		this.emptyIconResId = emptyIconResId;
	}
	public void setFailedIconResId(int failedIconResId) {
		this.failedIconResId = failedIconResId;
	}
	public void setNetworkErrorIconResId(int networkErrorIconResId) {
		this.networkErrorIconResId = networkErrorIconResId;
	}
	
//	@Override
//	protected void onDetachedFromWindow() {
//		setLoadingAnimation(false);
//		super.onDetachedFromWindow();
//	}
}
