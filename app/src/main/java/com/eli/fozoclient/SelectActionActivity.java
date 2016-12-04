package com.eli.fozoclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SelectActionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_action2);
    }

    public void startViewAllPeopleActivity(View view) {
        Intent intent = new Intent(this, ViewAllPeopleActivity.class);
        this.startActivity(intent);
    }


}
