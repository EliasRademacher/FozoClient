package com.eli.fozoclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

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

    public void makeGETRequest(View view) {

        Person person = new Person();

        // The connection URL
        final String url = "http://fozotest.appspot.com/people";

        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Add the String message converter
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        // Make the HTTP GET request, marshaling the response to a String
        String result = restTemplate.getForObject(url, String.class, "Android");

        System.out.println(result);
    }

}
