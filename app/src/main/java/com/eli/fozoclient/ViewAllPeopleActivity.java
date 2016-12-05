package com.eli.fozoclient;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.appengine.api.datastore.Key;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import model.Account;
import model.Challenge;


public class ViewAllPeopleActivity extends ListActivity {

    ArrayList<String> listItems = new ArrayList<>();

    ArrayAdapter<String> adapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_people);

        viewAllPeople();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        setListAdapter(adapter);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /* Method which will handle dynamic insertion. */
    public void viewAllPeople() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://fozo-145621.appspot.com/accounts";

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

                JSONObject responseJson = null;
                try {
                    responseJson = new JSONObject(responseAsString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray accountsJson = null;
                try {
                    accountsJson = responseJson.getJSONArray("payload");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                for (int i = 0; i < accountsJson.length(); i++) {
                    JSONObject account = null;
                    if (null != accountsJson) {
                        try {
                            account = (JSONObject) accountsJson.get(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    JSONArray challengeKeys = null;
                    String userId = null;
                    Integer numChallenges = 0;
                    if (account != null) {
                        if (!account.isNull("challengesPending")) {
                            try {
                                challengeKeys = account.getJSONArray("challengesPending");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            userId = account.getString("userId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (null == challengeKeys) {
                            numChallenges = 0;
                        } else {
                            numChallenges = challengeKeys.length();
                        }
                    }

                    String entry = "Username: " + userId + "\nChallenges: " + numChallenges;
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ViewAllPeople Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public void logout(View view) {
        HeaderKeeper.getInstance().setHeaders(null);
        HeaderKeeper.getInstance().setUserId(null);

//        StringRequestWithHeaders stringRequest = new StringRequestWithHeaders();
//        stringRequest.setUserId(null);
//        stringRequest.setToken(null);
//        JsonObjectRequestWithHeaders jsonRequest = new JsonObjectRequestWithHeaders();
//        jsonRequest.setUserId(null);
//        jsonRequest.setToken(null);

        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);

    }
}
