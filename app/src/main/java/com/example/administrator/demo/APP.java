package com.example.administrator.demo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.administrator.demo.Volley.BitmapCache;
import com.example.administrator.demo.Volley.OkHttpStack;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderUN;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.net.CookieStore;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class APP extends Application {

	private ExecutorService service_task_executor;
	private ExecutorService resource_task_executor;
	
	private static Context mContext;
	private static RequestQueue mRequestQueue;
	private static ImageLoader imageLoader;
	private Session session;

	private String filesPath;
	private String bannerPath;
	private String logoPath;
	private String iconPath;

	// 手势密码添加
	private static APP mInstance;

	public static APP getInstance() {
		return mInstance;
	}


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		mInstance = this;

		Log.d(G.tag("Smith"), "Application start.");
		
		mContext = getApplicationContext();
		mRequestQueue = Volley.newRequestQueue(mContext, new OkHttpStack());
//		mRequestQueue = Volley.newRequestQueue(mContext);
		imageLoader=new ImageLoader(mRequestQueue, new BitmapCache());
		// 初始化环境变量
		this.filesPath = this.getFilesDir().getAbsolutePath();
		this.bannerPath = this.filesPath + "/banner";
		this.logoPath = this.filesPath + "/logo";
		this.iconPath = this.filesPath + "/icon";

		// 初始化线程池
		this.service_task_executor = (ExecutorService) Executors
				.newCachedThreadPool();
		this.resource_task_executor = (ExecutorService) Executors
				.newFixedThreadPool(G.RESOURCE_THREAD_POOL_SIZE); // 创建一个可重用固定线程数的线程池，以共享的无界队列方式来运行这些线程。
		this.session = new SharedPreferencesSession(this);
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		Log.d(G.tag("Smith"), "Application finished.");
		super.onTerminate();
	}

	protected void clearConfigFile() {
		new File(this.filesPath + "/" + G.BANNER_CONFIG_FILENAME).delete();
	}

	public String getFilesPath() {
		return filesPath;
	}

	public String getBannerPath() {
		return bannerPath;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public String getIconPath() {
		return iconPath;
	}

	public ExecutorService getServiceTaskExecutor() {
		// TODO Auto-generated method stub
		return this.service_task_executor;
	}

	public ExecutorService getResourceTaskExecutor() {
		// TODO Auto-generated method stub
		return this.resource_task_executor;
	}

	// cookie相关管理
	private CookieStore cookies;

	public CookieStore getCookie() {
		return cookies;
	}

	public void setCookie(CookieStore cks) {
		cookies = cks;
	}

	public Session getSession() {
		return this.session;
	}

	public boolean hasLogin() {
		boolean flag = "1".equals(this.session.get("isLogin"));
		return flag;
	}

	public boolean hasSigned() {
		boolean flag = "1".equals(this.session.get("isSigned"));
		return flag;
	}

	public String getSessionId() {

		return this.session.get("jSessionId");
	}

	public void logout() {
		Map<String, String> vals = new HashMap<String, String>();
		vals.put("isLogin", "0");
		vals.put("jSessionId", "");
		vals.put("isAutoLogin", "");
		vals.put("name", "");
		vals.put("passWord", "");
		this.session.set(vals);
	}
	
	public static Context getContext() {
		return mContext;
	}
	
	//获取volley请求队列
	public static RequestQueue getRequestQueue() {
		return mRequestQueue;
	}
	
	public static ImageLoader getImageLoader(){
		return imageLoader;
	}

	public void initImageLoader() {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(APP.getInstance());
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
//		if (L.D) config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoaderUN.getInstance().initUN(config.build());
	}

}
