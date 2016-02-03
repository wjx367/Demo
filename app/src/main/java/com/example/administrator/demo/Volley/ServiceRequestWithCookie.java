package com.example.administrator.demo.Volley;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/11/18.
 */
public class ServiceRequestWithCookie extends Request<String> {
    private ServiceListener listener;
    private HashMap<String, String> params;
    public String cookieFromResponse;
    private String mHeader;
    private Map<String, String> sendHeader=new HashMap<String, String>(1);
    public ServiceRequestWithCookie(int method, String url, ServiceListener listener) {
        super(method, url, new ServiceErrorWraper(listener));
        this.listener = listener;
        init();
    }

    /**
     *
     * @param method
     * @param url
     * @param params
     * @param listener
     * @param cookie
     */
    public ServiceRequestWithCookie(int method, String url, HashMap<String, String> params, ServiceListener listener, String cookie) {
        super(method, url, new ServiceErrorWraper(listener));
        this.params = params;
        this.listener = listener;
        setSendCookie(cookie);
        init();
    }
    /**
     *
     * @param method
     * @param url
     * @param params
     * @param listener
     */
    public ServiceRequestWithCookie(int method, String url, HashMap<String, String> params, ServiceListener listener) {
        super(method, url, new ServiceErrorWraper(listener));
        this.params = params;
        this.listener = listener;
        init();
    }
    private void init() {
        //重试策略
        setRetryPolicy(new DefaultRetryPolicy(45000, // 默认超时时间
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //不缓存
        setShouldCache(false);
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
        try {
            listener.onResult(response);
        }catch(Exception e){
            listener.onException(e);
        }
        finally {
            listener.onFinish();
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
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
