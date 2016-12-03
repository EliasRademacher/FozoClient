package com.eli.fozoclient;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Elias on 12/2/2016.
 */

public class JsonObjectRequestWithHeaders extends JsonObjectRequest {

    private String token;
    private String userId;


    public JsonObjectRequestWithHeaders(int post, String url, String requestBody, Response.Listener<JSONObject> message, Response.ErrorListener errorListener) {
        super(post, url, requestBody, message, errorListener);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        HeaderKeeper.getInstance().setHeaders(response.headers);



        return super.parseNetworkResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String>  params = new HashMap<String, String>();
        params.put("token", this.token);
        params.put("userId", this.userId);
        return params;
    }



}
