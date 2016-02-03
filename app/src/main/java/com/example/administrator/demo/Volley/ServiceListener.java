package com.example.administrator.demo.Volley;

import org.json.JSONException;

/**
 * Created by Administrator on 2015/12/14.
 */
public interface ServiceListener {
    /**
     * 请求结果返回
     * @param result 请求结果字符串
     */
    public void onResult(String result) throws JSONException;
    /**
     * 请求发生错误
     * @param e 异常
     */
    public void onException(Exception e);
    /**
     * 请求结束
     */
    public void onFinish();
    /**
     * 网络错误
     */
    public void onNetworkError();
}
