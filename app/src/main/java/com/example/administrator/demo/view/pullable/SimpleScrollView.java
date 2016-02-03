package com.example.administrator.demo.view.pullable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.example.administrator.demo.MyViewUtils;


/**
 * 防止默认的ScrollView在某些系统上超出边界后还可以滑动
 */
public class SimpleScrollView extends ScrollView {

	public SimpleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		MyViewUtils.disableOverScroll(this);
	}

	public SimpleScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		MyViewUtils.disableOverScroll(this);
	}

	public SimpleScrollView(Context context) {
		super(context);
		MyViewUtils.disableOverScroll(this);
	}

}
