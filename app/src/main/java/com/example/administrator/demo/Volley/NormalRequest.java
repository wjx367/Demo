package com.example.administrator.demo.Volley;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
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

public class NormalRequest extends Request<JSONObject>{
	private Map<String, String> mMap;
    private Listener<JSONObject> mListener;
    public String cookieFromResponse;
    private String mHeader;
    private Map<String, String> sendHeader=new HashMap<String, String>(1);
    
    public NormalRequest(int method,String url, Listener<JSONObject> listener,ErrorListener errorListener, Map<String, String> map) {
        super(method, url, errorListener);
        mListener = listener;
        mMap = map;
        init();
    }
    
    public NormalRequest(int method,String url, Listener<JSONObject> listener,ErrorListener errorListener, Map<String, String> map,String cookie) {
        super(method, url, errorListener);
        mListener = listener;
        mMap = map;
        init();
        setSendCookie(cookie);
    }
    
    public NormalRequest(int method,String url, Listener<JSONObject> listener,ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        init();
    }

    private void init(){
        setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    
    //mMap是已经按照前面的方式,设置了参数的实例
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }
     
    //此处因为response返回值需要json数据,和JsonObjectRequest类一样即可
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
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
                JSONObject jsonObject = new JSONObject(jsonString);
                jsonObject.put("Cookie",cookieFromResponse);
                return Response.success(jsonObject,
                        HttpHeaderParser.parseCacheHeaders(response));
            }
            return Response.success(new JSONObject(jsonString),HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
    @Override
    protected void deliverResponse(JSONObject response) {
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
