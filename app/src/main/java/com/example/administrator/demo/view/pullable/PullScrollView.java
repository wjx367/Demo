package com.example.administrator.demo.view.pullable;

import android.content.Context;
import android.util.AttributeSet;

public class PullScrollView extends SimpleScrollView implements PullToRefreshLayout.PullableView {

	public PullScrollView(Context context) {
		super(context);
	}

	public PullScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown() {
		if (getScrollY() == 0) {
			return true;
		}
		else {
			return false;
		}
	}


	@Override
	public boolean canPullUp() {
		return false;
	}

}
