package com.eli.fozoclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import model.Person;

/**
 * Created by Elias on 10/29/2016.
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getPerson(View view) {

        final TextView userNameView = (TextView) findViewById(R.id.person_username);
        final TextView emailView = (TextView) findViewById(R.id.person_email);
        final TextView birthdateView = (TextView) findViewById(R.id.person_birthdate);

        final TextView mTextView = (TextView) view;

        /* Instantiate the RequestQueue. */
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://fozotest.appspot.com/people/stevie_wonder";

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

}
