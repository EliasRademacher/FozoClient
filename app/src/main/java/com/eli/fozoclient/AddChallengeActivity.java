package com.eli.fozoclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Random;

import model.Challenge;

public class AddChallengeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_challenge);
    }

    public void createChallenge(View view) {
        final TextView descriptionView = (TextView) findViewById(R.id.description);
        final String description = descriptionView.getText().toString();

        final TextView tagsView = (TextView) findViewById(R.id.tags);
        final String tags = tagsView.getText().toString();

        final TextView pointsView = (TextView) findViewById(R.id.points);
        final String points = pointsView.getText().toString();

        final TextView responseMessageView = (TextView) findViewById(R.id.responseMessage);
        final String url = "http://fozo-145621.appspot.com/challenges";

        ObjectMapper objectMapper = new ObjectMapper();

        Challenge challenge = new Challenge();
        challenge.setDescription(description);
        long pointsAsLong = Long.parseLong(points);
        challenge.setPoints(pointsAsLong);
        ArrayList<String> tagsList = new ArrayList<>();
        tagsList.add(tags);
        challenge.setTags(tagsList);

        Random randomNumberGenerator = new Random();
        challenge.setId(randomNumberGenerator.nextInt());


        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(challenge);
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

        String userId = HeaderKeeper.getInstance().getUserId();
        request.setUserId(userId);

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


}
