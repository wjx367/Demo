package com.example.administrator.demo.Volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by Administrator on 2015/11/18.
 */
public class ServiceErrorWraper implements Response.ErrorListener {

    private ServiceListener listener;
    public ServiceErrorWraper(ServiceListener listener) {
        this.listener = listener;
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        // Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
        // For AuthFailure, you can re login with user credentials.
        // For ClientError, 400 & 401, Errors happening on client side when sending api request.
        // In this case you can check how client is forming the api and debug accordingly.
        // For ServerError 5xx, you can do retry or handle accordingly.
        if( error instanceof NetworkError) {
        } else if( error instanceof ServerError) {
        } else if( error instanceof AuthFailureError) {
        } else if( error instanceof ParseError) {
        } else if( error instanceof NoConnectionError) {
        } else if( error instanceof TimeoutError) {
        }
        try {
            listener.onException(error);
        }
        finally {
            listener.onFinish();
        }
    }
}
