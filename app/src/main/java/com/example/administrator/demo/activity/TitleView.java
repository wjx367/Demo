package com.example.administrator.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.demo.G;
import com.example.administrator.demo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class TitleView extends FrameLayout implements View.OnClickListener {
	@ViewInject(R.id.title_view)
	private LinearLayout titleView;
	@ViewInject(R.id.left_btn)
	private ImageView mLeftBtn; // 左上角返回按钮
	@ViewInject(R.id.left_btn1)
	private ImageView hLeftBtn; // 左上角返回按钮
	@ViewInject(R.id.right_text)
	private TextView mRightText; // 例如 首页右上角的登陆
	@ViewInject(R.id.right_btn)
	private ImageView mRightbtn;
	@ViewInject(R.id.title_text)
	private TextView bTitle;
	@ViewInject(R.id.small_title_text)
	private TextView mTitle; // 普通页面标题
	@ViewInject(R.id.blank)
	private LinearLayout blank;
	@ViewInject(R.id.title_pic)
	private ImageView titlepic;		//首页标题图片
	// @ViewInject(R.id.title_up_btn)
	// private ImageView upbtn;
	// @ViewInject(R.id.title_down_btn)
	// private ImageView downbtn;
	private OnLeftButtonClickListener mOnLeftButtonClickListener;
	private OnRightButtonClickListener mOnRightButtonClickListener;
	private OnSelfLeftButtonClickListener mOnSelfLeftButtonClickLIstener;
	private OnDownButtonClickListener mOnDownButtonClickLIstener;
	private OnRTextClickListener mOnRTextClickListener;
	private OnmTitleClickListener mTitleClickListener;

	private Activity activity;

	public interface OnLeftButtonClickListener {
		void onClick(View button);
	}

	public interface OnRightButtonClickListener {
		void onClick(View button);
	}

	public interface OnSortButtonClickListener {
		void onClick(View button);
	}

	public interface OnSelfLeftButtonClickListener {
		void onClick(View button);
	}

	public interface OnDownButtonClickListener {
		void onClick(View button);
	}

	public interface OnRTextClickListener {
		void onClick(View text);
	}

	public interface OnmTitleClickListener {
		void onClick(View text);
	}

	public void setLeftButton(String text, OnLeftButtonClickListener listener) {
		mLeftBtn.setVisibility(View.VISIBLE);
		mOnLeftButtonClickListener = listener;
	}

	public void setLeftButton(int stringID, OnLeftButtonClickListener listener) {
		mLeftBtn.setVisibility(View.VISIBLE);
		mOnLeftButtonClickListener = listener;
	}

	public void setSelfLeftButton(OnSelfLeftButtonClickListener listener) {
		mLeftBtn.setVisibility(View.GONE);
		hLeftBtn.setVisibility(View.VISIBLE);
		mOnSelfLeftButtonClickLIstener = listener;
	}

	public void removeLeftButton() {
		mLeftBtn.setVisibility(View.INVISIBLE);
		mOnLeftButtonClickListener = null;
	}

	public void hiddenLeftButton() {
		mLeftBtn.setVisibility(View.INVISIBLE);
	}

	public void showLeftButton() {
		mLeftBtn.setVisibility(View.VISIBLE);
	}

	// public void setRightButton(int StringID,OnRightButtonClickListener
	// listener) {
	//
	// mRightText.setVisibility(View.VISIBLE);
	// mOnRightButtonClickListener = listener;
	// }

	public void setRightButton(String str, OnRightButtonClickListener listener) {
		mRightText.setText(str);
		mRightText.setVisibility(View.VISIBLE);
		mRightbtn.setVisibility(View.GONE);
		mOnRightButtonClickListener = listener;
	}

	public void setRightImageButton(int srcId, OnRightButtonClickListener listener) {
		mRightText.setVisibility(View.GONE);
		mRightbtn.setVisibility(View.VISIBLE);
		mRightbtn.setImageResource(srcId);
		mOnRightButtonClickListener = listener;
	}

	public void hideRightButton() {
		mRightText.setVisibility(View.GONE);
		mRightbtn.setVisibility(View.INVISIBLE);
	}

	public void removeRightButton() {
		// mRightText.setText("");
		mRightText.setVisibility(View.INVISIBLE);
		mOnRightButtonClickListener = null;
	}

	public void hiddenRightButton() {
		mRightbtn.setVisibility(View.INVISIBLE);
	}

	public void showRightButton() {
		mRightText.setVisibility(View.VISIBLE);
	}

	public void setDownButton(OnDownButtonClickListener listener) {
		mOnDownButtonClickLIstener = listener;
	}

	public void setRText(OnRTextClickListener listener) {
		mOnRTextClickListener = listener;
	}

	// public void showUpDownButton(int i) {
	// if (i == 0) {
	// upbtn.setVisibility(View.VISIBLE);
	// downbtn.setVisibility(View.GONE);
	// } else if (i == 1) {
	// upbtn.setVisibility(View.GONE);
	// downbtn.setVisibility(View.VISIBLE);
	// } else {
	// upbtn.setVisibility(View.GONE);
	// downbtn.setVisibility(View.GONE);
	// }
	// }

	public TitleView(Context context) {
		this(context, null);
	}

	public TitleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if(!isInEditMode()) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.title_view, this, true);
			ViewUtils.inject(this);
			if (G.SYSTEM_SDK_API < 19) {
				blank.setVisibility(View.GONE);
			} else {
				blank.setVisibility(View.VISIBLE);
			}
			// titleView = (RelativeLayout) findViewById(R.id.title_view);
			// mLeftBtn = (ImageView) findViewById(R.id.left_btn);
			mLeftBtn.setVisibility(View.INVISIBLE);
			mLeftBtn.setOnClickListener(this);
			// mRightText = (ImageView) findViewById(R.id.right_btn);
			mRightText.setVisibility(View.GONE);
			mRightText.setOnClickListener(this);
			mRightbtn.setVisibility(View.INVISIBLE);
			mRightbtn.setOnClickListener(this);
			hLeftBtn.setVisibility(View.GONE);
			hLeftBtn.setOnClickListener(this);
			mTitle.setVisibility(View.GONE);
			bTitle.setVisibility(View.INVISIBLE);
			mTitle.setOnClickListener(this);
			titlepic.setVisibility(View.GONE);
			// upbtn.setVisibility(View.GONE);
			// downbtn.setVisibility(View.GONE);
			// upbtn.setOnClickListener(this);
			// downbtn.setOnClickListener(this);
			// titleView.setBackgroundColor(Color.rgb(0xf1, 0x3b, 0x37));
		}
	}

	public void setHeight(int height) {
		titleView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				height));
	}

	// 靠左标题使用，带监听
	public void setTitle(String text, Activity context) {
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText(text);
		activity = context;
		mTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				activity.finish();
			}
		});

	}

	// webview使用
	public void setwTitle(String text, Activity context, OnmTitleClickListener m) {
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText(text);
		activity = context;
		mTitleClickListener = m;

	}

	public void setTitle(int stringID, Activity context) {
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText(stringID);
		activity = context;
		mTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				activity.finish();
			}
		});
	}

	public void setTitleColor() {
		mTitle.setTextColor(Color.rgb(0x00, 0x00, 0x00));
		titleView.setBackgroundColor(Color.rgb(0xf7, 0xf7, 0xf7));
		// mRightText.setTextColor(Color.rgb(0xff, 0xff, 0xff));
	}

	// 居中标题设置
	public void setBTitle(int stringID) {
		bTitle.setVisibility(View.VISIBLE);
		bTitle.setText(stringID);

	}

	// 居中标题设置
	public void setBTitle(String stringID) {
		bTitle.setVisibility(View.VISIBLE);
		bTitle.setText(stringID);

	}

	//首页标题设置
	public void setHomeTitle(){
		titlepic.setVisibility(View.VISIBLE);
		bTitle.setVisibility(View.GONE);
	}
	// 居中标题设置String
	public void setStringBTitle(String str) {
		bTitle.setVisibility(View.VISIBLE);
		bTitle.setText(str);
	}

	public void setBTitleColor() {
		bTitle.setTextColor(Color.rgb(0x00, 0x00, 0x00));
		// TextPaint tpPaint = bTitle.getPaint();
		// tpPaint.setFakeBoldText(true); // 加粗
		titleView.setBackgroundColor(Color.rgb(0xf7, 0xf7, 0xf7));
		// mRightText.setTextColor(Color.rgb(0xff, 0xff, 0xff));
	}

	public void setholdassetsTitleColor() {
		mTitle.setTextColor(Color.rgb(0xff, 0xff, 0xff)); // 标题白色
		titleView.setBackgroundColor(Color.argb(0x00, 0x00, 0x00, 0x00));
		// 设置为透明
	}

	// 设置个人中心 标题颜色
	public void setTitleSelfCenterColor() {
		mTitle.setTextColor(Color.rgb(0xff, 0xff, 0xff));
		titleView.setBackgroundColor(Color.rgb(0xff, 0x95, 0x1a));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn:
			if (mOnLeftButtonClickListener != null)
				mOnLeftButtonClickListener.onClick(v);
			break;
		case R.id.right_text:
		case R.id.right_btn:
			if (mOnRightButtonClickListener != null)
				mOnRightButtonClickListener.onClick(v);
			break;
		case R.id.left_btn1:
			if (mOnSelfLeftButtonClickLIstener != null)
				mOnSelfLeftButtonClickLIstener.onClick(v);
			break;
		case R.id.small_title_text:
			if (mTitleClickListener != null)
				mTitleClickListener.onClick(v);
			break;

		// case R.id.title_down_btn:
		// if (mOnDownButtonClickLIstener != null)
		// mOnDownButtonClickLIstener.onClick(v);
		// break;

		}
	}

}
