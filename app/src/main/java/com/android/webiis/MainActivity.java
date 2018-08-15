package com.android.webiis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


//https://android.jlelse.eu/tablayout-and-viewpager-in-your-android-app-738b8840c38a

public class MainActivity extends AppCompatActivity {
    private String[] optionList = { ""};
    private String failMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList resultsObjects = new ArrayList();
        resultsObjects.add("Login");  // position 0
        resultsObjects.add(" ");
        resultsObjects.add("Register New Customer"); // position 2
        resultsObjects.add(" ");
        resultsObjects.add("Enter Demo Accocunt Without Login"); // position 4
        optionList = (String[]) resultsObjects.toArray(new String[resultsObjects.size()]);

        final ListView lv = (ListView) findViewById(R.id.mainlv);

        // Create an ArrayAdapter from List
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, optionList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.BLUE);

                // Generate ListView Item using TextView
                return view;
            }

        };


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

//                // When clicked, show a toast with the TextView text
//                Toast.makeText(getApplicationContext(),
//                        ((TextView) view).getText(), Toast.LENGTH_SHORT).show();

                if (position == 0) {
                    Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    myIntent.putExtra("gLogin", "");
                    myIntent.putExtra("gPass", "");
                    startActivity(myIntent);
                } else if (position == 4) {
                    Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    myIntent.putExtra("gLogin", "guest");
                    myIntent.putExtra("gPass", "guest");
                    startActivity(myIntent);
                } else if(position == 2) {
                    Intent myIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(myIntent);
                }
            }
        });
        // DataBind ListView with items from ArrayAdapter
        lv.setAdapter(arrayAdapter);

        failMsg = (getIntent().getStringExtra("failMsg"));

        if (failMsg != null) {
            if (failMsg.length() >0) {
                showToast(lv);
            }
        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    private Toast mToastToShow;
    public void showToast(View view) {
        // Set the toast and duration
        int toastDurationInMilliSeconds = 10000;
        mToastToShow = Toast.makeText(this, failMsg, Toast.LENGTH_LONG);

        // Set the countdown to display the toast
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
                mToastToShow.show();
            }
            public void onFinish() {
                mToastToShow.cancel();
            }
        };

        // Show the toast and starts the countdown
        mToastToShow.show();
        toastCountDown.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
