package com.android.webiis;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.afweb.model.account.TradingRuleObj;
import com.afweb.model.stock.AFstockInfo;
import com.afweb.model.stock.AFstockObj;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrandingRuleActivity extends AppCompatActivity {
    private static final String TAG = "TrandingRuleA";
    private CustomerObj customerObj;
    private  String customerObjSt;

    private ArrayList<AccountObj> accountObjList;
    private  String accountObjListSt;
    private  AccountObj accountObj;

    private ArrayList <TradingRuleObj> tradingRuleObjList;
    private String tradingRuleObjListSt;
    private AFstockObj mAFstockObj;
    private String mAFstockObjSt;

    private String[] trList = { ""};


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.refresh:
                    try {
                        Intent myIntent = new Intent(getApplicationContext(), TrandingRuleActivity.class);

                        myIntent.putExtra("customerObjSt", customerObjSt); //Optional parameters
                        myIntent.putExtra("accountObjListSt", accountObjListSt); //Optional parameters
                        myIntent.putExtra("accountObjId", accountObj.getID());

                        myIntent.putExtra("tradingRuleObjListSt", tradingRuleObjListSt); //Optional parameters
                        myIntent.putExtra("mAFstockObjSt", mAFstockObjSt); //Optional parameters

                        startActivity(myIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranding_rule);

        TextView tv = findViewById(R.id.tradingrule_tv);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ArrayList resultsObjects = new ArrayList();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            customerObjSt=(getIntent().getStringExtra("customerObjSt"));
            customerObj=(objectMapper.readValue(customerObjSt, CustomerObj.class));

            accountObjListSt = (getIntent().getStringExtra("accountObjListSt"));
            AccountObj[] arrayItem = new ObjectMapper().readValue(accountObjListSt, AccountObj[].class);
            List<AccountObj> listItem = Arrays.<AccountObj>asList(arrayItem);
            accountObjList = (new ArrayList<AccountObj>(listItem));

            int accountObjId = getIntent().getIntExtra("accountObjId",0);

            for (int i=0; i<accountObjList.size(); i++) {
                AccountObj accountObjTemp = accountObjList.get(i);
                if (accountObjTemp.getID() == accountObjId) {
                    accountObj= accountObjTemp;
                    break;
                }
            }

            tradingRuleObjListSt = getIntent().getStringExtra("tradingRuleObjListSt");
            TradingRuleObj[] arrayItem1 = new ObjectMapper().readValue(tradingRuleObjListSt, TradingRuleObj[].class);
            List<TradingRuleObj> listItem1 = Arrays.<TradingRuleObj>asList(arrayItem1);
            tradingRuleObjList = new ArrayList<TradingRuleObj>(listItem1);

            mAFstockObjSt = getIntent().getStringExtra("mAFstockObjSt");
            mAFstockObj =objectMapper.readValue(mAFstockObjSt, AFstockObj.class);

            String accInfo = accountObj.getAccountName();
            String stAccountName = "   Account: "+accInfo +" - "+mAFstockObj.getSymbol()+"\n   "+mAFstockObj.getStockName();;
            stAccountName += "\n   "+mAFstockObj.getUpdateDateD();

            AFstockInfo stockInfo = mAFstockObj.getStockInfo();
            if (stockInfo != null) {
                stAccountName += "\n   "+"O - " + stockInfo.getFopen() + "  C - " + stockInfo.getFclose()  + "  V - " + stockInfo.getVolume();
            }

            tv.setText(stAccountName);
//            resultsObjects.add(stAccountName);
            String stitle = String.format("%-8s%14s%10s%10s", "TR Name", "Signal", "Share", "Amount");
            resultsObjects.add(stitle);
            for (int i=0; i<tradingRuleObjList.size(); i++ ) {
                TradingRuleObj tradingRuleObj = tradingRuleObjList.get(i);
                String s = String.format("  %-8s%10s%10s%10s", tradingRuleObj.getTRname(),(int)tradingRuleObj.getTRsignal(), (int)tradingRuleObj.getShare(), "$"+(int) tradingRuleObj.getAmount());
                resultsObjects.add(s);
            }

            trList = (String[]) resultsObjects.toArray(new String[resultsObjects.size()]);

        } catch (IOException e) {
            e.printStackTrace();
        }

        final ListView lv = (ListView) findViewById(R.id.account_trlv);
        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, trList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.BLUE);

                tv.setTextSize(15);
                Typeface childFont = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/couriernewbold.ttf");
                tv.setTypeface(childFont);
                return view;
            }
        };

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                if (position == 0) {
                    return;
                }
                if (position-1 < tradingRuleObjList.size()) {
                    TradingRuleObj tradingRuleObj = tradingRuleObjList.get(position-1);

                }
                // When clicked, show a toast with the TextView text
                Toast.makeText(getApplicationContext(),
                        ((TextView) view).getText(), Toast.LENGTH_SHORT).show();

            }

        });
        // DataBind ListView with items from ArrayAdapter
        lv.setAdapter(arrayAdapter);
    }

    ////////////////////////////////
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_tradingrule, menu);
    return true;
}

    public boolean onOptionsItemSelected(MenuItem item){


        int id = item.getItemId();
        switch (id){
            case R.id.tradingrule_settings:
                Toast.makeText(getApplicationContext(),"trading_rule_settings",Toast.LENGTH_LONG).show();
                return true;
            default:
//                return super.onOptionsItemSelected(item);
                break;
        }

        //back to previous
        Intent myIntent = new Intent(getApplicationContext(), AccountStockActivityHandler.class);

        myIntent.putExtra("customerObjSt", customerObjSt); //Optional parameters
        myIntent.putExtra("accountObjListSt", accountObjListSt); //Optional parameters
        myIntent.putExtra("accountObjId", accountObj.getID());
        myIntent.putExtra(PromptDialogFragment.ADD_SYM_CMD, "");
        myIntent.putExtra(PromptDialogFragment.DEL_SYM_CMD, "");

        startActivity(myIntent);
        finish();
        return true;

    }


}
