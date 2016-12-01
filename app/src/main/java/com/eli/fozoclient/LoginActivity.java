package com.eli.fozoclient;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.Account;
import model.Person;

/**
 * Created by Elias on 10/29/2016.
 */
public class LoginActivity extends AppCompatActivity {


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void getUser(View view) {

        final TextView userNameView = (TextView) findViewById(R.id.person_username);
        final TextView emailView = (TextView) findViewById(R.id.person_email);
        final TextView birthdateView = (TextView) findViewById(R.id.person_birthdate);

        final EditText userNameQueryView = (EditText) findViewById(R.id.person_username_query);
        String userNameQuery = userNameQueryView.getText().toString();


        /* Instantiate the RequestQueue. */
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://fozotest.appspot.com/people/" + userNameQuery;

        /* Request a string response from the provided URL. */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ObjectMapper mapper = new ObjectMapper();

                        Person person = null;
                        try {
                            person = mapper.readValue(response, Person.class);
                        } catch (IOException e) {
                            System.out.println(e.getMessage() + "\n");
                            e.printStackTrace();
                        }

                        userNameView.setText("Retreived " + person.getUserName());
                        emailView.setText("Email: " + person.getEmail());
                        birthdateView.setText("Birthdate: " + person.getBirthDate().toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                userNameView.setText("That didn't work!");
            }
        });

        /* Add the request to the RequestQueue. */
        queue.add(stringRequest);

    }

    public void authenticateUser1(View view) {
        final TextView userIdView = (TextView) findViewById(R.id.userId);
        String userId = userIdView.getText().toString();

        final TextView passwordView = (TextView) findViewById(R.id.password);
        String password = passwordView.getText().toString();

        /* Instantiate the RequestQueue. */
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://fozo-145621.appspot.com/accounts/login";

        Account account = new Account(userId);
        account.setPassword(password);
    }

    /* Found at https://gist.github.com/mombrea/7250835 */
    public void authenticateUser2(View view) {

        final TextView userIdView = (TextView) findViewById(R.id.userId);
        final String userId = userIdView.getText().toString();

        final TextView passwordView = (TextView) findViewById(R.id.password);
        final String password = passwordView.getText().toString();

        /* Instantiate the RequestQueue. */
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://fozo-145621.appspot.com/accounts/login";

        final Account account = new Account(userId);
        account.setPassword(password);

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("request completed:\n");
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return super.getBody();
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                params.put("password", account.getPassword());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };

        queue.add(sr);
    }



    public void authenticateUser(View view) {
        final TextView userIdView = (TextView) findViewById(R.id.userId);
        final String userId = userIdView.getText().toString();

        final TextView passwordView = (TextView) findViewById(R.id.password);
        final String password = passwordView.getText().toString();

        final TextView responseMessageView = (TextView) findViewById(R.id.responseMessage);
        String url = "http://fozo-145621.appspot.com/accounts/login";

        ObjectMapper objectMapper = new ObjectMapper();
        Account account = new Account(userId);
        account.setPassword(password);

        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(account);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        responseMessageView.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseMessageView.setText("Error: " + error.getMessage());
                        error.printStackTrace();

                    }
                });

        /* Instantiate the RequestQueue. */
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
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
}
