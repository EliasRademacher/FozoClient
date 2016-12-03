package com.eli.fozoclient;

import java.util.Map;

/**
 * Created by Elias on 12/2/2016.
 */
public class HeaderKeeper {
    private static HeaderKeeper instance = null;

    Map<String, String> headers;

    protected HeaderKeeper() {
        // Exists only to defeat instantiation.
    }

    public static HeaderKeeper getInstance() {
        if(instance == null) {
            instance = new HeaderKeeper();
        }
        return instance;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
