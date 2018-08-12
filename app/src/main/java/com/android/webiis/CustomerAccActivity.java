package com.android.webiis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afweb.model.account.AccountObj;
import com.afweb.model.account.CustomerObj;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerAccActivity extends AppCompatActivity {

    private static final String TAG = "CustomerA";
    private CustomerObj customerObj;
    private  String customerObjSt;
    private ArrayList<AccountObj> accountObjList;
    private  String accountObjListSt;

    private String[] accountList = { ""};


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
//            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_acc);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        TextView tv = findViewById(R.id.customer_acc_tv);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            customerObjSt = (getIntent().getStringExtra("customerObjSt"));
            customerObj = (objectMapper.readValue(customerObjSt, CustomerObj.class));

            accountObjListSt = (getIntent().getStringExtra("accountObjListSt"));
            AccountObj[] arrayItem = new ObjectMapper().readValue(accountObjListSt, AccountObj[].class);
            List<AccountObj> listItem = Arrays.<AccountObj>asList(arrayItem);
            accountObjList = (new ArrayList<AccountObj>(listItem));

            ArrayList resultsObjects = new ArrayList();
            String custInfo = customerObj.getEmail();
            if (custInfo  == null) {
                custInfo = customerObj.getUserName();
            }
            String custInfoSt = "   Customer: "+custInfo+"\n";
            tv.setText(custInfoSt);

            for (int i=0; i<accountObjList.size(); i++ ) {
                AccountObj accObj = accountObjList.get(i);
                resultsObjects.add("     "+accObj.getAccountName());
            }

            accountList = (String[]) resultsObjects.toArray(new String[resultsObjects.size()]);


        } catch (IOException e) {
            e.printStackTrace();
        }

        final ListView lv = (ListView) findViewById(R.id.customer_acclv);
        // Create an ArrayAdapter from List
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, accountList){
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

                // When clicked, show a toast with the TextView text
                Toast.makeText(getApplicationContext(),
                        ((TextView) view).getText(), Toast.LENGTH_SHORT).show();

                if (position < accountObjList.size()) {

                    try {

                        AccountObj accObj = accountObjList.get(position);
                        Intent myIntent = new Intent(getApplicationContext(), AccountStockActivityHandler.class);

                        myIntent.putExtra("customerObjSt", customerObjSt); //Optional parameters
                        myIntent.putExtra("accountObjListSt", accountObjListSt); //Optional parameters
                        myIntent.putExtra("accountObjId", accObj.getID());


                        myIntent.putExtra(PromptDialogFragment.ADD_SYM_CMD, ""); //Optional parameters
                        myIntent.putExtra(PromptDialogFragment.DEL_SYM_CMD, "");

                        startActivity(myIntent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        });
        // DataBind ListView with items from ArrayAdapter
        lv.setAdapter(arrayAdapter);
    }
    ///////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customer, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){


        int id = item.getItemId();
        switch (id){
            case R.id.customer_settings:
                Toast.makeText(getApplicationContext(),"customer_settings",Toast.LENGTH_LONG).show();
                return true;
            default:
//                return super.onOptionsItemSelected(item);
                break;
        }

        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(myIntent);
        finish();
        return true;

    }
}
