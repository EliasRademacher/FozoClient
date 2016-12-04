package com.eli.fozoclient;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Elias on 12/4/2016.
 */

public class StringRequestWithHeaders extends StringRequest {

    private static String token;
    private static String userId;

    public StringRequestWithHeaders(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public StringRequestWithHeaders(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    public  String getToken() {
        return token;
    }

    public void setToken(String token) {
        StringRequestWithHeaders.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        StringRequestWithHeaders.userId = userId;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {

        if (null == token) {
            token = response.headers.get("token");
        }

        if (null == userId) {
            userId = response.headers.get("userId");
        }

        return super.parseNetworkResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String>  params = new HashMap<String, String>();
        params.put("token", token);
        params.put("userId", userId);
        return params;
    }


}
