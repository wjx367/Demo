package com.example.administrator.demo.view.pullable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListAdapter;

import com.example.administrator.demo.R;


public class PullableListViewLayout extends FrameLayout implements PullToRefreshLayout.OnRefreshListener, PullableListView.PullableListViewListener {
	public PullableListViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}
	public PullableListViewLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	public PullableListViewLayout(Context context) {
		super(context);
		initView(context);
	}
	
	private PullToRefreshLayout refreshLayout;
	private PullableListView listView;
	private ContentHolderView holderView;
	private PullableListDataLoader dataLoader;
	
	private void initView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.layout_pullable_list_view, this, true);
		refreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh_layout);
		refreshLayout.setOnRefreshListener(this);

		listView = (PullableListView) findViewById(R.id.list_view);
		listView.setPullableListViewListener(this);

		holderView = (ContentHolderView) findViewById(R.id.content_holder_view);
	}
	
	public void setLoadModes(PullableListView.Mode successMode, PullableListView.Mode hasMoreMode, PullableListView.Mode noMoreMode) {
		listView.setLoadModes(successMode, hasMoreMode, noMoreMode);
	}
	public void setPullableListDataLoader(PullableListDataLoader loader) {
		this.dataLoader = loader;
	}
	public void setAdapter(ListAdapter adapter) {
		listView.setAdapter(adapter);
	}
	public void init(PullableListDataLoader loader, PullableListView.Mode successMode, PullableListView.Mode hasMoreMode, PullableListView.Mode noMoreMode) {
		setPullableListDataLoader(loader);
		setLoadModes(successMode, hasMoreMode, noMoreMode);
	}
	
	public PullToRefreshLayout getPullToRefreshLayout() {
		return refreshLayout;
	}
	public PullableListView getPullableListView() {
		return listView;
	}
	public ContentHolderView getContentHolderView() {
		return holderView;
	}

	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		loadDataList(true, true);
	}

	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
		loadDataList(false, false);
	}
	
	@Override
	public void loadNothing() {
		refreshLayout.hide();
		holderView.showEmptyView("21222", new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadDataList(true);
			}
		});
	}

	@Override
	public void loadSuccess() {
		holderView.hide();
		refreshLayout.show();
	}

	@Override
	public void loadFailed() {
		refreshLayout.hide();
		holderView.showFailedView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadDataList(true);
			}
		});
	}

	@Override
	public void networkError() {
		refreshLayout.hide();
		holderView.showNetworkErrorView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadDataList(true);
			}
		});
	}
	
	private void loadDataList(boolean refresh) {
		loadDataList(refresh, false);
	}
	
	private void loadDataList(boolean refresh, boolean pullToRefresh) {
		if (refresh) {
			if (!pullToRefresh) {
				refreshLayout.hide();
				holderView.showLoadingView();
			}
		}
		if (dataLoader != null) {
			if ((refresh&&refreshClearData&&!pullToRefresh) || (pullToRefresh&&pullToRefreshClearData)) {
				/**
				 * 第一个判断条件(refresh&&refreshClearData&&!pullToRefresh) 在以下4种情形为true，网络加载失败，接口调用失败，初始加载，数据为空，需要清空数据
				 * 第二个是判断条件(pullToRefresh&&pullToRefreshClearData) 在下拉刷新的时候为true,下拉刷新清空数据
				 */
				dataLoader.clearData();
			}
			dataLoader.getDataList(refresh, pullToRefresh);
		}
	}
	
	public interface PullableListDataLoader {
		/**
		 * 加载数据
		 * @param refresh 是否刷新原有数据
		 * @param pullToRefresh 是否是下拉刷新
		 */
		public void getDataList(boolean refresh, boolean pullToRefresh);
		/**
		 * 清空数据
		 */
		public void clearData();
	}
	/**
	 * 刷新数据是否清空已有数据
	 */
	private boolean refreshClearData = true;
	/**
	 * 下拉刷新是否清空已有数据
	 */
	private boolean pullToRefreshClearData = true;
	
	public void setRefreshClearData(boolean refreshClearData) {
		this.refreshClearData = refreshClearData;
	}
	public void setPullToRefreshClearData(boolean pullToRefreshClearData) {
		this.pullToRefreshClearData = pullToRefreshClearData;
	}
	
	public void onLoadSuccess(boolean hasMore) {
		refreshLayout.onLoadSuccess();
		listView.onLoadSuccess(hasMore);
	}
	public void onLoadFailed() {
		refreshLayout.onLoadFailed();
		listView.onLoadFailed();
	}
	public void onNetworkError() {
		listView.onNetworkError();
	}
	public void startLoading() {
		loadDataList(true);
	}
}
