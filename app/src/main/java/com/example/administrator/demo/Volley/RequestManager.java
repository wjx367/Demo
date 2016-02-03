package com.example.administrator.demo.Volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.NetworkImageView;
import com.example.administrator.demo.APP;
import com.example.administrator.demo.G;
import com.example.administrator.demo.NetworkUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestManager {
	private static Context tcontext;
	public static Response.ErrorListener error = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			// TODO Auto-generated method stub
		}
	};


	/**
	 *  @param context
	 * @param url
	 * @param maps
	 * @param listener
	 */
	public static void goRquest(Context context, String url, HashMap<String, String> maps, ServiceListener listener) {
		tcontext = context;
		//没有网络
		if (!NetworkUtils.isNetworkAvailable(APP.getContext())) {
			try {
				listener.onNetworkError();
			} finally {
				listener.onFinish();
			}
			return;
		}
		ServiceRequest sq = new ServiceRequest(Method.POST, url, maps, listener) {
		};
		sq.setTag(getTag());
		APP.getRequestQueue().add(sq);
	}

	/**
	 *  @param context
	 * @param url
	 * @param maps
	 * @param listener
	 */
	public static void goRquest(Context context, String url, HashMap<String, String> maps, boolean cookie ,ServiceListener listener) {
		tcontext = context;
		//没有网络
		if (!NetworkUtils.isNetworkAvailable(APP.getContext())) {
			try {
				listener.onNetworkError();
			} finally {
				listener.onFinish();
			}
			return;
		}
		ServiceRequest sq = new ServiceRequest(Method.POST, url, maps,cookie,listener) {
		};
		sq.setTag(getTag());
		APP.getRequestQueue().add(sq);
	}



	/**
	 * 带cookie
	 * @param context
	 * @param url
	 * @param maps
	 * @param listener
	 * @param cookie
	 */
	public static void goRquestCookieSend(Context context, String url, HashMap<String, String> maps,String cookie, ServiceListener listener) {
		tcontext = context;
		//没有网络
		if (!NetworkUtils.isNetworkAvailable(APP.getContext())) {
			try {
				listener.onNetworkError();
			} finally {
				listener.onFinish();
			}
			return;
		}
		ServiceRequestWithCookie sq = new ServiceRequestWithCookie(Method.POST, url, maps, listener,cookie) {
		};
		sq.setTag(getTag());
		APP.getRequestQueue().add(sq);
	}
	/**
	 * 带cookie
	 * @param context
	 * @param url
	 * @param maps
	 * @param listener
	 */
	public static void goRquestCookieSave(Context context, String url, HashMap<String, String> maps, ServiceListener listener) {
		tcontext = context;
		//没有网络
		if (!NetworkUtils.isNetworkAvailable(APP.getContext())) {
			try {
				listener.onNetworkError();
			} finally {
				listener.onFinish();
			}
			return;
		}
		ServiceRequestWithCookie sq = new ServiceRequestWithCookie(Method.POST, url, maps, listener) {
		};
		sq.setTag(getTag());
		APP.getRequestQueue().add(sq);
	}
	/**
	 * 
	 * @param url
	 * @param maps
	 * @param getSuccess
	 */
	public static void goJSONRequestPost(Context context, String url,
			Map<String, String> maps, Response.Listener<JSONObject> getSuccess) {
		tcontext = context;
		Request<JSONObject> mRequest = new NormalRequest(Method.POST, url,
				getSuccess, error, maps);
		mRequest.setShouldCache(false);
		APP.getRequestQueue().add(mRequest);
	}
	
	public static void goJSONRequestPost(Context context, String url,
			Map<String, String> maps, Response.Listener<JSONObject> getSuccess,boolean cookieflag) {
		tcontext = context;
		Request<JSONObject> mRequest = null;
		if (cookieflag) {
			String cookie = ((APP)context.getApplicationContext()).getSession().get("Cookie");
			mRequest = new NormalRequest(Method.POST, url,
					getSuccess, error, maps,cookie);
		} else {
			mRequest = new NormalRequest(Method.POST, url,
					getSuccess, error, maps);
		}
		APP.getRequestQueue().add(mRequest);
	}
	
	public static void goJSONRequestPost(Context context,String url,
			Map<String, String> maps, Response.Listener<JSONObject> getSuccess,
			Response.ErrorListener error,boolean cookieflag) {
		Request<JSONObject> mRequest = null;
		if (cookieflag) {
			String cookie = ((APP)context.getApplicationContext()).getSession().get("Cookie");
			mRequest = new NormalRequest(Method.POST, url,
					getSuccess, error, maps,cookie);
		} else {
			mRequest = new NormalRequest(Method.POST, url,
					getSuccess, error, maps);
		}
		
		APP.getRequestQueue().add(mRequest);
	}

	/**
	 * 
	 * @param url
	 * @param maps
	 * @param getSuccess
	 */
	public static void goJSONRequestPost(String url,
			Map<String, String> maps, Response.Listener<JSONObject> getSuccess,
			Response.ErrorListener error) {
		Request<JSONObject> mRequest = new NormalRequest(Method.POST, url,
				getSuccess, error, maps);
		APP.getRequestQueue().add(mRequest);
	}
	
	/**
	 * 使用默认errorlistener
	 * 
	 * @param url
	 * @param getSuccess
	 */
	public static void goJSONRequestGet(Context context, String url,
			Response.Listener<JSONObject> getSuccess) {
		tcontext = context;
		Request<JSONObject> mRequest = new NormalRequest(Method.GET, url,
				getSuccess, error);
		APP.getRequestQueue().add(mRequest);

	}

	/**
	 * 
	 * @param url
	 * @param getSuccess
	 * @param error
	 */
	public static void goJSONRequestGet(String url,
			Response.Listener<JSONObject> getSuccess,
			Response.ErrorListener error) {
		Request<JSONObject> mRequest = new NormalRequest(Method.GET, url,
				getSuccess, error);
		APP.getRequestQueue().add(mRequest);

	}

	/**
	 * 
	 * @param context
	 * @param url
	 * @param maps
	 * @param getSuccess
	 */
	public static void goStringRequestPost(Context context, String url,
			Map<String, String> maps, Response.Listener<String> getSuccess) {
		tcontext = context;
		NormalStringRequest mStringRequest = new NormalStringRequest(url, getSuccess, error,maps);
		mStringRequest.setShouldCache(false);
		APP.getRequestQueue().add(mStringRequest);
	}
	
	public static void goStringRequestPost(Context context, String url,
			Map<String, String> maps, Response.Listener<String> getSuccess,boolean cookieflag) {
		tcontext = context;
		NormalStringRequest mStringRequest = null;
		if (cookieflag) {
			String cookie = ((APP)context.getApplicationContext()).getSession().get("Cookie");
			mStringRequest = new NormalStringRequest(url, getSuccess, error,maps,cookie);
		} else {
			mStringRequest = new NormalStringRequest(url, getSuccess, error,maps);
		}
		mStringRequest.setShouldCache(false);
		APP.getRequestQueue().add(mStringRequest);
	}
	
	
	/**
	 * 
	 * @param context
	 * @param url
	 * @param maps
	 * @param getSuccess
	 * @param error
	 */
	public static void goStringRequestPost(Context context, String url,
			Map<String, String> maps, Response.Listener<String> getSuccess,Response.ErrorListener error) {
		tcontext = context;
		NormalStringRequest mStringRequest = new NormalStringRequest(url, getSuccess, error,maps);
		mStringRequest.setShouldCache(false);
		APP.getRequestQueue().add(mStringRequest);
	}
	
	
	public static void goStringRequestPost(Context context, String url,
			Map<String, String> maps, Response.Listener<String> getSuccess,Response.ErrorListener error,boolean cookieflag) {
		tcontext = context;
		NormalStringRequest mStringRequest = null;
		if (cookieflag) {
			String cookie = ((APP)context.getApplicationContext()).getSession().get("Cookie");
			mStringRequest = new NormalStringRequest(url, getSuccess, error,maps,cookie);
		} else {
			mStringRequest = new NormalStringRequest(url, getSuccess, error,maps);
		}
		mStringRequest.setShouldCache(false);
		APP.getRequestQueue().add(mStringRequest);
	}
	/**
	 * 
	 * @param context
	 * @param url
	 * @param getSuccess
	 */
	public static void goStringRequestGet(Context context, String url,
			 Response.Listener<String> getSuccess) {
		tcontext = context;
		NormalStringRequest mStringRequest = new NormalStringRequest(url, getSuccess, error);
		APP.getRequestQueue().add(mStringRequest);
	}
	/**
	 * 
	 * @param context
	 * @param url
	 * @param getSuccess
	 * @param error
	 */
	public static void goStringRequestGet(Context context, String url,
			 Response.Listener<String> getSuccess,Response.ErrorListener error) {
		tcontext = context;
		NormalStringRequest mStringRequest = new NormalStringRequest(url, getSuccess, error);
		APP.getRequestQueue().add(mStringRequest);
	}
	/**
	 * 加载网络请求图片
	 * 
	 * @return
	 */
	public static ImageLoader getImageLoder() {
		ImageLoader mImageLoader = new ImageLoader(
				APP.getRequestQueue(), new BitmapCache());
		return mImageLoader;
	}

	/**
	 * 加载网络请求图片
	 * 
	 * @param image
	 * @param url
	 */
	// public static void setImage(NetworkImageView image,String url){
	// ImageLoader mImageLoader = new
	// ImageLoader(APP.getRequestQueue(),
	// new BitmapCache());
	// ImageListener imageListener=ImageLoader.getImageListener(image,
	// R.drawable.umeng_socialize_share_pic,
	// R.drawable.umeng_socialize_share_pic); //设置默认图片 错误图片
	// mImageLoader.get(url, imageListener);
	// image.setImageUrl(url,mImageLoader);
	// }

	/**
	 * 加载网络请求图片,在WZDApplicationlication中全局化变量imageloader,实现图片缓存功能
	 * 
	 * @param image
	 * @param url
	 * @param defaultpic
	 * @param errorpic
	 */
	public static void setImage(NetworkImageView image, String url,
			int defaultpic, int errorpic) {
		ImageListener imageListener = ImageLoader.getImageListener(image,
				defaultpic, errorpic); // 设置默认图片 错误图片
		APP.getImageLoader().get(url, imageListener);
		image.setImageUrl(url, APP.getImageLoader());
	}

	
	/**
	 * 
	 * @return 返回通用post请求maps
	 */
	public static HashMap<String, String> getCommonMap(){
		HashMap<String, String> maps = new HashMap<String, String>();
		maps.put("appSource", "android");
		maps.put("appVersion", G.APPVERSION);
		return maps;
	}

	private static String getTag() {
		return tcontext.getClass().getSimpleName();
	}

}
