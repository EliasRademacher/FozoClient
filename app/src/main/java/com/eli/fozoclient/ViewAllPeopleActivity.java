package com.eli.fozoclient;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

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

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        setListAdapter(adapter);

        viewAllPeople();
    }

    /* Method which will handle dynamic insertion. */
    public void viewAllPeople() {
//        final ListView accountList
//                = (ListView) findViewById(R.id.account_list);
//        accountList.setAdapter();


        listItems.add("Clicked : " + clickCounter++);
        adapter.notifyDataSetChanged();
    }
}
