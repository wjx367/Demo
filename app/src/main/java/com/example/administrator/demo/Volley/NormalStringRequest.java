package com.example.administrator.demo.Volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author wang
 *
 */
public class NormalStringRequest extends Request<String> {
	private Map<String, String> mMap;
	private Listener<String> mListener;
	public String cookieFromResponse;
    private String mHeader;
    private Map<String, String> sendHeader=new HashMap<String, String>(1);
	public NormalStringRequest( String url,Listener<String> listener, ErrorListener errorListener,Map<String, String> map) {
		super(Method.POST, url, errorListener);
		mMap = map;
		mListener=listener;
	}
	
	public NormalStringRequest( String url,Listener<String> listener, ErrorListener errorListener,Map<String, String> map,String cookie) {
		super(Method.POST, url, errorListener);
		mMap = map;
		mListener=listener;
		setSendCookie(cookie);
	}
	
	public NormalStringRequest(String url,Listener<String> listener, ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		mListener=listener;
	}
	
	public NormalStringRequest(String url,Listener<String> listener, ErrorListener errorListener,String cookie) {
		super(Method.GET, url, errorListener);
		mListener=listener;
		setSendCookie(cookie);
	}
	
	 //mMap是已经按照前面的方式,设置了参数的实例
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }
    
	@Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
        	 String jsonString = new String(response.data,HttpHeaderParser.parseCharset(response.headers));
        	 mHeader = response.headers.toString();
             //使用正则表达式从reponse的头中提取cookie内容的子串
             Pattern pattern= Pattern.compile("Set-Cookie.*?;");
             Matcher m=pattern.matcher(mHeader);
             if(m.find()){
                 cookieFromResponse =m.group();
               //去掉cookie末尾的分号
                 cookieFromResponse = cookieFromResponse.substring(11,cookieFromResponse.length()-1);
                 //将cookie字符串添加到jsonObject中，该jsonObject会被deliverResponse递交，调用请求时则能在onResponse中得到
                 
                 JSONObject jsonObject;
                 try {
                	jsonObject = new JSONObject(jsonString);
					jsonObject.put("Cookie",cookieFromResponse);
					return Response.success(jsonObject.toString(),
	                         HttpHeaderParser.parseCacheHeaders(response));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
             }
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

	@Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
	@Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return sendHeader;
    }
    public void setSendCookie(String cookie){
    	if (cookie!=null &&!"".equals(cookie))
    		sendHeader.put("Cookie",cookie);
    }
}
