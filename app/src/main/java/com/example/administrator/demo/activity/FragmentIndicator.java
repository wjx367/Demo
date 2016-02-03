package com.example.administrator.demo.activity;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.demo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class FragmentIndicator extends LinearLayout {

	private int mDefaultIndicator = 0;
	private Context context;
	public static int mCurIndicator; // 上一活动指标
	@ViewInject(R.id.bottomImg1)
	private static ImageView img1;
	@ViewInject(R.id.bottomImg2)
	private static ImageView img2;
	@ViewInject(R.id.bottomImg4)
	private static ImageView img4;
	@ViewInject(R.id.bottomImg5)
	private static ImageView img5;
	@ViewInject(R.id.bottomText1)
	private static TextView textView1;
	@ViewInject(R.id.bottomText2)
	private static TextView textView2;
	@ViewInject(R.id.bottomText4)
	private static TextView textView4;
	@ViewInject(R.id.bottomText5)
	private static TextView textView5;

	private OnIndicateListener mOnIndicateListener;

	private FragmentIndicatorInterface indicatorInterface;

	private FragmentIndicator(Context context) {
		super(context);
		this.context=context;
	}

	public FragmentIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.bottom_tab_view, this, true);
		ViewUtils.inject(this);
		mCurIndicator = mDefaultIndicator;
	}

	public interface FragmentIndicatorInterface {
		void checkLogin();

		boolean hasLogin();

		void goLogin(int lastid, int nextid);

	}

	public void setFragmentIndicatorInterface(
			FragmentIndicatorInterface indicatorInterface) {
		this.indicatorInterface = indicatorInterface;
	}

	// 焦点切换时 替换tab图标
	public static void setIndicator(int which) {
		switch (mCurIndicator) {
		case 0:
			img1.setImageResource(R.drawable.ic_home_normal);
			textView1.setTextColor(Color.rgb(0x82,0x89,0xa2));
			break;
		case 1:
			img2.setImageResource(R.drawable.ic_invests_normal);
			textView2.setTextColor(Color.rgb(0x82,0x89,0xa2));
			break;
		case 2:
			img4.setImageResource(R.drawable.ic_selfcenter_normal);
			textView4.setTextColor(Color.rgb(0x82,0x89,0xa2));
			break;
		case 3:
			img5.setImageResource(R.drawable.ic_more_normal);
			textView5.setTextColor(Color.rgb(0x82,0x89,0xa2));
			break;
		}

		switch (which) {
		case 0:
			img1.setImageResource(R.drawable.ic_home_focused);
			textView1.setTextColor(Color.rgb(0xff, 0xff, 0xff));
			break;
		case 1:
			img2.setImageResource(R.drawable.ic_invests_focused);
			textView2.setTextColor(Color.rgb(0xff, 0xff, 0xff));
			break;
		case 2:
			img4.setImageResource(R.drawable.ic_selfcenter_focused);
			textView4.setTextColor(Color.rgb(0xff, 0xff, 0xff));
			break;
		case 3:
			img5.setImageResource(R.drawable.ic_more_focused);
			textView5.setTextColor(Color.rgb(0xff, 0xff, 0xff));
			break;
		}
		mCurIndicator = which;
	}

	public interface OnIndicateListener {
		void onIndicate(View v, int which);
	}

	public void setOnIndicateListener(OnIndicateListener listener) {
		mOnIndicateListener = listener;
	}

	@OnClick({ R.id.bottomLy1, R.id.bottomLy2, R.id.bottomLy4,
			R.id.bottomLy5 })
	private void onclickevent(View view) {
		switch (view.getId()) {
		case R.id.bottomLy1:
			if (mCurIndicator != 0) {
				mOnIndicateListener.onIndicate(view, 0);
				setIndicator(0);
			}
			break;
		case R.id.bottomLy2:
			if (mCurIndicator != 1) {
				mOnIndicateListener.onIndicate(view, 1);
				setIndicator(1);
				}
			break;
		case R.id.bottomLy4:
//			if (mCurIndicator != 2) {
//				indicatorInterface.checkLogin();
//				if (!indicatorInterface.hasLogin()) {
//					indicatorInterface.goLogin(mCurIndicator,2);
//				} else {
					mOnIndicateListener.onIndicate(view, 2);
					setIndicator(2);
//				}
//			}
			break;
		case R.id.bottomLy5:
			if (mCurIndicator != 3) {
				mOnIndicateListener.onIndicate(view, 3);
				setIndicator(3);
			}
			break;
		}
	}

}

