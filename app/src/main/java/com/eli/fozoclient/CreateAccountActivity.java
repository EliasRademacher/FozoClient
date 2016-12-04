package com.eli.fozoclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import model.Account;


public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
    }

    public void createAccount(View view) {
        final TextView userIdView = (TextView) findViewById(R.id.newUserId);
        final String userId = userIdView.getText().toString();

        final TextView passwordView = (TextView) findViewById(R.id.newPassword);
        final String password = passwordView.getText().toString();

        final TextView passwordConfView = (TextView) findViewById(R.id.newPasswordConfirmation);
        final String passwordConf = passwordConfView.getText().toString();

        final TextView responseMessageView = (TextView) findViewById(R.id.responseMessage);
        final String url = "http://fozo-145621.appspot.com/accounts";

        ObjectMapper objectMapper = new ObjectMapper();
        Account account = new Account(userId);

        /* Make sure the two password fields match. */
        if (!password.equals(passwordConf)) {
            responseMessageView.setText("Passwords do not match");
            return;
        }

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

    public void startLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
    }

}
