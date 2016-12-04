package com.eli.fozoclient;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;


public class ViewAllPeopleActivity extends ListActivity {

    ArrayList<String> listItems = new ArrayList<>();

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_people);

        viewAllPeople();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        setListAdapter(adapter);

    }

    /* Method which will handle dynamic insertion. */
    public void viewAllPeople() {
        /* Instantiate the RequestQueue. */
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://fozo-145621.appspot.com/accounts";


        /* Request a string response from the provided URL. */
        StringRequestWithHeaders stringRequest = new StringRequestWithHeaders(
                Request.Method.GET,
                url,
                createResponseListener(),
                createErrorListener()
        );

        stringRequest.setToken(HeaderKeeper.getInstance().getToken());
        stringRequest.setUserId(HeaderKeeper.getInstance().getUserId());

        queue.add(stringRequest);
    }


    private Response.Listener<String> createResponseListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String responseAsString) {

                ObjectMapper mapper = new ObjectMapper();

                model.Response response = null;

                try {
                    response = mapper.readValue(responseAsString, model.Response.class);
                } catch (IOException e) {
                    System.out.println(e.getMessage() + "\n");
                    e.printStackTrace();
                }


                ArrayList<LinkedHashMap<String, String>> accounts =
                        (ArrayList<LinkedHashMap<String, String>>) response.getPayload();

                for (int i = 0; i < accounts.size(); i++) {

                    LinkedHashMap<String, String> accountMap = accounts.get(i);

                    String userId = accountMap.get("userId");
                    String challenges = accountMap.get("challengesPending");
                    if (null == challenges) {
                        challenges = "none";
                    }
                    String entry = "Username: " + userId + "\nChallenges: " + challenges;

                    listItems.add(entry);
                }

                adapter.notifyDataSetChanged();

            }
        };
    }

    private Response.ErrorListener createErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
    }

}
