package com.eli.fozoclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import model.Account;
import model.Person;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
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

    public void authenticateUser(View view) {
        final TextView userIdView = (TextView) findViewById(R.id.userId);
        final String userId = userIdView.getText().toString();
        HeaderKeeper.getInstance().setUserId(userId);


        final TextView passwordView = (TextView) findViewById(R.id.password);
        final String password = passwordView.getText().toString();

        final TextView responseMessageView = (TextView) findViewById(R.id.responseMessage);
        final String url = "http://fozo-145621.appspot.com/accounts/login";

        ObjectMapper objectMapper = new ObjectMapper();
        Account account = new Account(userId);
        account.setPassword(password);

        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(account);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        JsonObjectRequestWithHeaders request = new JsonObjectRequestWithHeaders(
            Request.Method.POST,
            url,
            requestBody,
            createResponseListener(responseMessageView),
            createErrorListener(responseMessageView)
        );

        /* Instantiate the RequestQueue. */
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    private Response.Listener<JSONObject> createResponseListener(final TextView view) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String message = "";
                try {
                    message = response.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                view.setText("Response: " + message);
                startSelectActionActivity();
            }
        };
    }

    private Response.ErrorListener createErrorListener(final TextView view) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                view.setText("Error: " + error.getMessage());
                error.printStackTrace();
            }
        };
    }

    public void startCreateAccountActivity(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        this.startActivity(intent);
    }

    public void startSelectActionActivity() {
        Intent intent = new Intent(this, SelectActionActivity.class);
        this.startActivity(intent);
    }

}
