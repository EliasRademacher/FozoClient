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

public class ViewAllPeopleActivity extends ListActivity {

    /* List of array strings which will serve as list items. */
    ArrayList<String> listItems = new ArrayList<>();

    /* Defining a string adapter which will handle the data of the listview. */
    ArrayAdapter<String> adapter;

    /* Recording how many times the button has been clicked. */
    int clickCounter = 0;

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
//        final ListView accountList
//                = (ListView) findViewById(R.id.account_list);
//        accountList.setAdapter();



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

        String token = stringRequest.getToken();
        String userId = stringRequest.getUserId();

        queue.add(stringRequest);
    }


    private Response.Listener<String> createResponseListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String responseAsString) {
//                Response response = null;
//                try {
//                    response = (Response) jsonResponse.get("payload");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


                ObjectMapper mapper = new ObjectMapper();

                model.Response response = null;
                try {
                    response = mapper.readValue(responseAsString, model.Response.class);
                } catch (IOException e) {
                    System.out.println(e.getMessage() + "\n");
                    e.printStackTrace();
                }

                System.out.println("Retrieved list of Accounts!");

                listItems.add("Clicked : " + clickCounter++);
                adapter.notifyDataSetChanged();

            }
        };
    }

    private Response.ErrorListener createErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                view.setText("Error: " + error.getMessage());
                error.printStackTrace();
            }
        };
    }

}
