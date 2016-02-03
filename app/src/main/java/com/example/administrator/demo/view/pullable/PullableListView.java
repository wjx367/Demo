package com.example.administrator.demo.view.pullable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Adapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.example.administrator.demo.MyViewUtils;

public class PullableListView extends ListView implements PullToRefreshLayout.PullableView {

	public PullableListView(Context context) {
		super(context);
		MyViewUtils.disableOverScroll(this);
	}
	public PullableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		MyViewUtils.disableOverScroll(this);
	}
	public PullableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		MyViewUtils.disableOverScroll(this);
	}

	private Mode successMode = Mode.getDefault();
	private Mode hasMoreMode = Mode.PULL_FROM_END;
	private Mode noMoreMode = Mode.DISABLED;
	private PullableListViewListener mListener;
	private boolean hasMore = true;
	
	public void setLoadModes(Mode successMode, Mode hasMoreMode, Mode noMoreMode) {
		this.successMode = successMode;
		this.hasMoreMode = hasMoreMode;
		this.noMoreMode = noMoreMode;
	}
	
	public void setPullableListViewListener(PullableListViewListener listener) {
		this.mListener = listener;
	}
	
	@Override
	public boolean canPullDown() {
		if (getCount() == 0) {
			// 没有item的时候不可以下拉刷新
			return false;
		} 
		if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0) {
			// 滑到ListView的顶部了
			return successMode.permitsPullToRefresh();
		}
		return false;
	}

	@Override
	public boolean canPullUp() {
		if (getCount() == 0) {
			// 没有item的时候不可以上拉加载
			return false;
		}
		if (getLastVisiblePosition() == (getCount() - 1)) {
			// 滑到底部了
			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
					&& getChildAt(getLastVisiblePosition() - getFirstVisiblePosition())
							.getBottom() <= getMeasuredHeight()) {
				return hasMore ? hasMoreMode.permitsPullToLoad() : noMoreMode.permitsPullToLoad();
			}
		}
		return false;
	}
	
	/**
	 * 判断adapter的数据是否为空
	 */
	public boolean isEmpty() {
		Adapter adapter = getAdapter();
		if (adapter != null) {
			if (adapter instanceof HeaderViewListAdapter) {
				HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) adapter;
				int emptyCount = headerViewListAdapter.getHeadersCount() + headerViewListAdapter.getFootersCount();
				return adapter.getCount() == emptyCount;
			}
			else {
				return adapter.isEmpty();
			}
		}
		return true;
	}
	
	/**
	 * 加载数据成功
	 * @param hasMore 是否还有更多数据
	 */
	public void onLoadSuccess(boolean hasMore) {
		if (isEmpty()) {
			if (mListener != null) {
				mListener.loadNothing();
			}
		}
		else {
			this.hasMore = hasMore;
			
//			if (!hasMore) {
//				MyToast.toast(R.string.htips_no_more_data);
//			}
			
			if (mListener != null) {
				mListener.loadSuccess();
			}
		}
	}

	/**
	 * 加载数据失败，如果原来已经有数据则不会调用mListViewListener.loadFailed();
	 */
	public void onLoadFailed() {
		if (isEmpty()) {
			if (mListener != null) {
				mListener.loadFailed();
			}
		}
	}
	
	/**
	 * 网络错误
	 */
	public void onNetworkError() {
		if (isEmpty() && mListener != null) {
			mListener.networkError();
		}
	}
	
	public static enum Mode {
		/**
		 * Disable all Pull-to-Refresh gesture and Refreshing handling
		 */
		DISABLED(0x0),

		/**
		 * Only allow the user to Pull from the start of the Refreshable View to
		 * refresh. The start is either the Top or Left, depending on the
		 * scrolling direction.
		 */
		PULL_FROM_START(0x1),

		/**
		 * Only allow the user to Pull from the end of the Refreshable View to
		 * refresh. The start is either the Bottom or Right, depending on the
		 * scrolling direction.
		 */
		PULL_FROM_END(0x2),

		/**
		 * Allow the user to both Pull from the start, from the end to refresh.
		 */
		BOTH(0x3);

		/**
		 * Maps an int to a specific mode. This is needed when saving state, or
		 * inflating the view from XML where the mode is given through a attr
		 * int.
		 * 
		 * @param modeInt - int to map a Mode to
		 * @return Mode that modeInt maps to, or PULL_FROM_START by default.
		 */
		static Mode mapIntToValue(final int modeInt) {
			for (Mode value : Mode.values()) {
				if (modeInt == value.getIntValue()) {
					return value;
				}
			}

			// If not, return default
			return getDefault();
		}

		static Mode getDefault() {
			return PULL_FROM_END;
		}
		
		private int mIntValue;

		// The modeInt values need to match those from attrs.xml
		Mode(int modeInt) {
			mIntValue = modeInt;
		}

		
		/**
		 * @return true if the mode permits Pull-to-Refresh
		 */
		boolean permitsPullToRefresh() {
			return this == PULL_FROM_START || this == BOTH;
		}
		
		boolean permitsPullToLoad() {
			return this == PULL_FROM_END || this == BOTH;
		}

		int getIntValue() {
			return mIntValue;
		}

	}
	
	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface PullableListViewListener {
		/**
		 * 接口调用成功但没有返回任何数据
		 */
		public void loadNothing();
		/**
		 * 接口调用成功返回了数据
		 */
		public void loadSuccess();
		/**
		 * 接口调用失败
		 */
		public void loadFailed();
		/**
		 * 网络异常
		 */
		public void networkError();
	}
}
