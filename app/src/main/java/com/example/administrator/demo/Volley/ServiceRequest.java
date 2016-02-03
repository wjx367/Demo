package com.example.administrator.demo.Volley;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.administrator.demo.APP;
import com.example.administrator.demo.Session;
import com.example.administrator.demo.StrUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/11/18.
 */
public class ServiceRequest extends Request<String>{
    private ServiceListener listener;
    private HashMap<String, String> params;
    private String paramsEncoding;
    private String mHeader;
    public String cookieFromResponse;
    private boolean cookieFlag;
    public ServiceRequest(int method, String url, ServiceListener listener) {
        super(method, url, new ServiceErrorWraper(listener));
        this.listener = listener;
        init();
    }

    public ServiceRequest(int method, String url, HashMap<String, String> params, ServiceListener listener) {
        super(method, url, new ServiceErrorWraper(listener));
        this.params = params;
        this.listener = listener;
        cookieFlag = false;
        init();
    }
    public ServiceRequest(int method, String url, HashMap<String, String> params,boolean cookie,ServiceListener listener) {
        super(method, url, new ServiceErrorWraper(listener));
        this.params = params;
        this.listener = listener;
        cookieFlag = cookie;
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
//        String parsed;
//        try {
//            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//        }
//        catch (UnsupportedEncodingException e) {
//            parsed = new String(response.data);
//        }
//        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
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

//    @Override
//    protected String getParamsEncoding() {
//        return paramsEncoding;
//    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> map = new HashMap<String, String>();
        Session session = APP.getInstance().getSession();
        String accessToken = session.get("accessToken");
        String cookie = session.get("Cookie");
        if (StrUtils.notEmpty(accessToken)) {
            map.put("accessToken", accessToken);
        }
        if (cookieFlag) {
            map.put("Cookie", cookie);
        }
        if (StrUtils.isEmpty(accessToken) && !cookieFlag) {
            //Collections.emptyMap()返回回的是一个继承自AbstractMap的EMPTY_MAP类，大部分方法没有实现只抛出异常。
            map = Collections.emptyMap();
        }
        return map;
    }
}
