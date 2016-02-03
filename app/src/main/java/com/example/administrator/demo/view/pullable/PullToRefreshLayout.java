package com.example.administrator.demo.view.pullable;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义的布局，用来管理三个子控件，其中一个是下拉头，一个是包含内容的pullableView（可以是实现PullableView接口的的任何View），
 * 还有一个上拉头，更多详解见博客http://blog.csdn.net/zhongkejingwang/article/details/38868463
 */
public class PullToRefreshLayout extends RelativeLayout {
	// 初始状态
	public static final int STATE_INIT = 0;
	// 释放刷新
	/**
	 * 触发下拉刷新时，手指还在下拉动作过程中并且下拉距离达到了刷新临界时控件的状态。这种状态下松手触发action_up立即触发刷新动作（包括刷新头显示刷新中提示和客户端进行数据刷新）
	 */
	public static final int STATE_RELEASE_TO_REFRESH = 1;
	// 正在刷新
	/**
	 * 刷新中的状态
	 */
	public static final int STATE_REFRESHING = 2;
	// 释放加载
	public static final int STATE_RELEASE_TO_LOAD = 3;
	// 正在加载
	public static final int STATE_LOADING = 4;
	// 操作完毕
	public static final int STATE_DONE = 5;
	// 当前状态
	private int state = STATE_INIT;
	// 刷新成功
	public static final int SUCCESS = 0;
	// 刷新失败
	public static final int FAIL = 1;
	// 按下Y坐标，上一个事件点Y坐标
	private float downY, lastY;
	// 按下X坐标
	private float downX;
	// 下拉刷新完成后延时恢复状态
	private long refreshFinishDelayed = 1000;
	// 上拉加载完成后延时恢复状态
	private long loadFinishDelayed = 1000;
	
	/**
	 * 解决下拉内容包含ViewPager这样左右滑动的控件时滑动冲突的问题
	 */
	private boolean resolveTouchConflict = false;

	/**
	 * 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
	 */
	public float pullDownY = 0;
	/**
	 * 上拉的距离
	 */
	private float pullUpY = 0;

	/**
	 * 释放刷新的距离（刷新：下拉刷新）
	 */
	private float headerContentHeight = 200;
	// 
	/**
	 * 释放加载的距离（加载：上拉加载）
	 */
	private float footerContentHeight = 200;

	private MyTimer timer;
	// 回滚速度
	public float MOVE_SPEED = 8;
	// 第一次执行布局
	private boolean isLayout = false;
	// 在刷新过程中滑动操作
	private boolean isTouch = false;
	// 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
	private float radio = 2;

	//头部
	private RefreshableView header;
	//底部
	private RefreshableView footer;
	
	// 实现了Pullable接口的View
	private PullableView pullable;
	// 刷新回调接口
	private OnRefreshListener mListener;
	/**
	 * 过滤多点触碰
	 */
	private int mEvents;
	// 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
	private boolean canPullDown = true;
	private boolean canPullUp = true;
	//初始的时候刷新
	private boolean initRefreshEnabled = false;
	
	/**
	 * 执行自动回滚的handler
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 回弹速度随下拉距离moveDeltaY增大而增大
			MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2
					/ getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
			if (!isTouch) {
				// 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
				if (state == STATE_REFRESHING && pullDownY <= headerContentHeight) {
					pullDownY = headerContentHeight;
					timer.cancel();
				} else if (state == STATE_LOADING && -pullUpY <= footerContentHeight) {
					pullUpY = -footerContentHeight;
					timer.cancel();
				}
			}
			if (pullDownY > 0) {
				pullDownY -= MOVE_SPEED;
			}
			else if (pullUpY < 0) {
				pullUpY += MOVE_SPEED;
			}
			if (pullDownY < 0) {
				// 已完成回弹
				pullDownY = 0;
				if (header != null) {
					header.onFinish();
				}
				// 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != STATE_REFRESHING && state != STATE_LOADING) {
					changeState(STATE_INIT);
				}
				timer.cancel();
			}
			if (pullUpY > 0) {
				// 已完成回弹
				pullUpY = 0;
				if (footer != null) {
					footer.onFinish();
				}
				// 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != STATE_REFRESHING && state != STATE_LOADING) {
					changeState(STATE_INIT);
				}
				timer.cancel();
			}
			
			if (pullDownY <= 0 && pullUpY >= 0) {
				timer.cancel();
			}
			
			// 刷新布局,会自动调用onLayout
			requestLayout();
		}
	};

	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	public PullToRefreshLayout(Context context) {
		super(context);
		initView(context);
	}

	public PullToRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	protected void initView(Context context) {
		timer = new MyTimer(handler);
	}

	public RefreshableView getHeader() {
		return header;
	}

	public RefreshableView getFooter() {
		return footer;
	}

	public void setInitRefreshEnabled(boolean initRefreshEnabled) {
		this.initRefreshEnabled = initRefreshEnabled;
	}

	private void hideRefreshable() {
		timer.schedule(5);
	}
	
	public void show() {
		if (pullable instanceof View) {
			((View) pullable).setVisibility(View.VISIBLE);
		}
		setVisibility(View.VISIBLE);
	}
	public void hide() {
		if (pullable instanceof View) {
			((View) pullable).setVisibility(View.GONE);
		}
		setVisibility(View.GONE);
	}

	/**
	 * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
	 * 
	 * @param result
	 *            PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
	 */
	public void onLoadFinish(int result) {
		if (state == STATE_REFRESHING) {
			if (header != null) {
				header.refreshFinish(result);
			}
			
			delayedFinishLoad(refreshFinishDelayed);
		}
		else if (state == STATE_LOADING) {
			if (footer != null) {
				footer.refreshFinish(result);
			}
			
			delayedFinishLoad(loadFinishDelayed);
		}
	}
	
	private void delayedFinishLoad(long delayed) {
		if (delayed > 0) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					finishLoad();
				}
			}, delayed);
		}
		else {
			finishLoad();
		}
	}
	
	private void finishLoad() {
		changeState(STATE_DONE);
		hideRefreshable();
	}
	
	public void onLoadSuccess() {
		onLoadFinish(SUCCESS);
	}
	public void onLoadFailed() {
		onLoadFinish(FAIL);
	}
	
	private void changeState(int toState) {
		state = toState;
		if (header != null) {
			header.changeState(toState);
		}
		if (footer != null) {
			footer.changeState(toState);
		}
	}

	/**
	 * 不限制上拉或下拉
	 */
	private void releasePull() {
		canPullDown = true;
		canPullUp = true;
	}

	/**
	 * 设置是否解决左右滑动冲突
	 */
	public void setResolveTouchConflict(boolean value) {
		this.resolveTouchConflict = value;
	}
	
	/*
	 * （非 Javadoc）由父控件决定是否分发事件，防止事件冲突
	 * 
	 * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getActionMasked()) {
		case MotionEvent.ACTION_DOWN: {
			downY = ev.getY();
			lastY = downY;
			downX = ev.getX();
			timer.cancel();
			mEvents = 0;
			releasePull();
			break;
		}
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP: {
			// 过滤多点触碰
			mEvents = -1;
			break;
		}
		case MotionEvent.ACTION_MOVE:{
			if (resolveTouchConflict && Math.abs(ev.getX() - downX) > Math.abs(ev.getY() - downY)) {
				break;
			}
			
			if (mEvents == 0) {
				if (((PullableView) pullable).canPullDown() && canPullDown
						&& state != STATE_LOADING) {//下拉刷新的前提：客户端设置可以刷新模式、canPullDown处于可以刷新的状态，不在上拉加载过程中
					// 可以下拉，正在加载时不能下拉
					// 对实际滑动距离做缩小，造成用力拉的感觉
					pullDownY = pullDownY + (ev.getY() - lastY) / radio;
					if (pullDownY < 0) {
						pullDownY = 0;
						canPullDown = false;
						canPullUp = true;
					}
					if (pullDownY > getMeasuredHeight()) {
						pullDownY = getMeasuredHeight();
					}
					if (state == STATE_REFRESHING) {
						// 正在刷新的时候触摸移动
						isTouch = true;
					}
					if (header != null) {
						header.onMoving(pullDownY, headerContentHeight);//下拉刷新
					}
				} else if (((PullableView) pullable).canPullUp() && canPullUp
						&& state != STATE_REFRESHING) {
					// 可以上拉，正在刷新时不能上拉
					pullUpY = pullUpY + (ev.getY() - lastY) / radio;
					if (pullUpY > 0) {
						pullUpY = 0;
						canPullDown = true;
						canPullUp = false;
					}
					if (pullUpY < -getMeasuredHeight()) {
						pullUpY = -getMeasuredHeight();
					}
					if (state == STATE_LOADING) {
						// 正在加载的时候触摸移动
						isTouch = true;
					}
					
					if (footer != null) {
						footer.onMoving(-pullUpY, footerContentHeight);//上拉加载更多
					}
				} else {//排除以上下拉刷新、上拉加载更多状态外，其他状态把canPullDown，canPullUp分别设置为可以下拉刷新，可以上拉加载更多的状态
					releasePull();
				}
			} else {
				mEvents = 0;
			}
			//一个move手势是由一个action_down、多段move action、一个action_up组成，每一段move action完成后，修正参数，达到非线性拖动感
			lastY = ev.getY();
			// 根据下拉距离改变比例
			radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
					* (pullDownY + Math.abs(pullUpY))));
			
			requestLayout();
			
			if (pullDownY <= headerContentHeight && state == STATE_RELEASE_TO_REFRESH) {
				// 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
				changeState(STATE_INIT);
			}
			if (pullDownY >= headerContentHeight && state == STATE_INIT) {
				// 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
				changeState(STATE_RELEASE_TO_REFRESH);
			}
			// 下面是判断上拉加载的，同上，注意pullUpY是负值
			if (-pullUpY <= footerContentHeight && state == STATE_RELEASE_TO_LOAD) {
				changeState(STATE_INIT);
			}
			if (-pullUpY >= footerContentHeight && state == STATE_INIT) {
				changeState(STATE_RELEASE_TO_LOAD);
			}
			// 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
			// Math.abs(pullUpY))就可以不对当前状态作区分了
			if ((pullDownY + Math.abs(pullUpY)) > 8) {
				// 防止下拉过程中误触发长按事件和点击事件
				ev.setAction(MotionEvent.ACTION_CANCEL);
			}
			break;
		}
		case MotionEvent.ACTION_UP:{
			if (resolveTouchConflict && Math.abs(ev.getX() - downX) > Math.abs(ev.getY() - downY)) {
				break;
			}
			
			if (pullDownY > headerContentHeight || -pullUpY > footerContentHeight) {
				// 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
				isTouch = false;
			}
			if (state == STATE_RELEASE_TO_REFRESH) {
				changeState(STATE_REFRESHING);
				// 刷新操作
				if (mListener != null) {
					mListener.onRefresh(this);
				}
			} else if (state == STATE_RELEASE_TO_LOAD) {
				changeState(STATE_LOADING);
				// 加载操作
				if (mListener != null) {
					mListener.onLoadMore(this);
				}
			}
			hideRefreshable();
		}
		default:
			break;
		}
		// 事件分发交给父类
		super.dispatchTouchEvent(ev);
		return true;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		boolean beLayout = isLayout;
		if (!beLayout) {
			// 这里是第一次进来的时候做一些初始化
			this.header = (RefreshableView) getChildAt(0);
			this.pullable = (PullableView) getChildAt(1);
			if (getChildCount()>2) {
				this.footer = (RefreshableView) getChildAt(2);
				this.footerContentHeight = footer.getContentHeight();
				this.footer.initLayout(this);
			}
			this.headerContentHeight = header.getContentHeight();
//			this.header.initLayout(this);
			this.isLayout = true;
		}
		
		
		ViewGroup headerView = (ViewGroup) header;
		// 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
		headerView.layout(0,
				(int) (pullDownY + pullUpY) - headerView.getMeasuredHeight(),//pullDownY、pullUpY至少一个为0
				headerView.getMeasuredWidth(), (int) (pullDownY + pullUpY));
		
		View contentView = (View) pullable;
		contentView.layout(0, (int) (pullDownY + pullUpY),
				contentView.getMeasuredWidth(), (int) (pullDownY + pullUpY)
						+ contentView.getMeasuredHeight());
		
		if (footer != null) {
			ViewGroup footerView = (ViewGroup) footer;
			footerView.layout(0,
					(int) (pullDownY + pullUpY) + contentView.getMeasuredHeight(),
					footerView.getMeasuredWidth(),
					(int) (pullDownY + pullUpY) + contentView.getMeasuredHeight()
							+ footerView.getMeasuredHeight());
		}
		
		if (!beLayout) {
			//初始化即开始加载
			if (initRefreshEnabled) {
				if (mListener != null) {
					mListener.onRefresh(this);
				}
				pullDownY = headerContentHeight;
				changeState(STATE_REFRESHING);
			}
		}
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		handler = null;
	}
	
	class MyTimer {
		private Handler handler;
		private Timer timer;
		private MyTask mTask;

		public MyTimer(Handler handler) {
			this.handler = handler;
			timer = new Timer();
		}

		public void schedule(long period) {
			cancel();
			mTask = new MyTask(handler);
			timer.schedule(mTask, 0, period);
		}

		public void cancel() {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
		}

		class MyTask extends TimerTask {
			private Handler handler;
			public MyTask(Handler handler) {
				this.handler = handler;
			}
			@Override
			public void run() {
				handler.obtainMessage().sendToTarget();
			}
		}
	}

	/**
	 * 刷新加载回调接口
	 * 
	 * @author chenjing
	 * 
	 */
	public interface OnRefreshListener {
		/**
		 * 刷新操作
		 */
		void onRefresh(PullToRefreshLayout pullToRefreshLayout);

		/**
		 * 加载操作
		 */
		void onLoadMore(PullToRefreshLayout pullToRefreshLayout);
	}

	/**
	 * 下拉的内容视图
	 */
	public interface PullableView {
		/**
		 * 判断是否可以下拉，如果不需要下拉功能可以直接return false
		 * 
		 * @return true如果可以下拉否则返回false
		 */
		boolean canPullDown();

		/**
		 * 判断是否可以上拉，如果不需要上拉功能可以直接return false
		 * 
		 * @return true如果可以上拉否则返回false
		 */
		boolean canPullUp();
	}
	
	/**
	 * 头部刷新或底部加载更多的视图
	 */
	public interface RefreshableView {
		/**
		 * 视图初始化
		 */
		public void initLayout(PullToRefreshLayout pullToRefreshLayout);
		/**
		 * 刷新完毕
		 */
		public void refreshFinish(int refreshResult);
		/**
		 * 状态改变
		 * @param toState 新状态
		 */
		public void changeState(int toState);
		/**
		 * 视图已恢复
		 */
		public void onFinish();
		/**
		 * 获取内容高度
		 * @return 内容实际高度
		 */
		public int getContentHeight();
		
		/**
		 * 下拉或上拉的事件
		 * @param current 下拉或上拉距离
		 * @param total 总的距离
		 */
		public void onMoving(float current, float total);
	}

	public void setLoadFinishDelayed(long delayed) {
		this.loadFinishDelayed = delayed;
	}
	public void setRefreshFinishDelayed(long delayed) {
		this.refreshFinishDelayed = delayed;
	}
}
