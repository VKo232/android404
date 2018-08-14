package com.android.webiis;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
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
                        Intent myIntent = new Intent(getApplicationContext(), TrandingRuleActivityHandler.class);

                        myIntent.putExtra("customerObjSt", customerObjSt); //Optional parameters
                        myIntent.putExtra("accountObjListSt", accountObjListSt); //Optional parameters
                        myIntent.putExtra("accountObjId", accountObj.getID());

                        myIntent.putExtra("tradingRuleObjListSt", tradingRuleObjListSt); //Optional parameters
                        myIntent.putExtra("stockObjSt", mAFstockObjSt); //Optional parameters

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

                if (stockInfo != null) {
                    if (stockInfo.getFopen() > 0) {
                        float ch = (stockInfo.getFclose() - stockInfo.getFopen()) / stockInfo.getFopen();
                        String percent= String.format("%.2f", ch*100);
                        stAccountName += "  " + percent+"%";
                    }
                }
            }

            tv.setText(stAccountName);

            DisplayMetrics dm = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            int dens = dm.densityDpi;
            double wi = (double) width / (double) dens;
            double hi = (double) height / (double) dens;
            double x = Math.pow(wi, 2);
            double y = Math.pow(hi, 2);
            double screenInches = Math.sqrt(x+y);
            int screen = (int)(screenInches+0.5);

            int orientation = Configuration.ORIENTATION_UNDEFINED;
            if(width ==height){
                orientation = Configuration.ORIENTATION_SQUARE;
            } else{
                if(width < height){
                    orientation = Configuration.ORIENTATION_PORTRAIT;
                }else {
                    orientation = Configuration.ORIENTATION_LANDSCAPE;
                }
            }

            String stitle = getformatFieldLV(screen,orientation,"TR Name", "Signal", "price", "perf", "Per%");

            resultsObjects.add(stitle);
            for (int i=0; i<tradingRuleObjList.size(); i++ ) {
                TradingRuleObj tradingRuleObj = tradingRuleObjList.get(i);
                String co1 = tradingRuleObj.getTRname();
                String co2 = (int)tradingRuleObj.getTRsignal()+"";
                String co3 = "-";
                String co4 = "0";
                String co5 = "-";

                if (tradingRuleObj.getShare() >0) {
                    float price = tradingRuleObj.getAmount() / tradingRuleObj.getShare();
                    co3 = String.format("%.2f", price*100);

                    if (stockInfo != null) {
                        float ch = (stockInfo.getFclose() - price) / stockInfo.getFclose();
                        co5 = String.format("%.2f", ch*100);
                    }
                }
                String s = getformatFieldLV(screen,orientation,co1,co2,co3,co4,co5);
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

    private String getformatFieldLV(int screenSiz, int orientation, String co1, String co2, String co3, String co4, String co5){
        String formatSize = "";
        String stTitle = "";
        if (screenSiz> 4) { // greater then 4 inch
            formatSize ="%-8s%10s%10s%10s"; // large size
            stTitle = String.format("  "+formatSize, co1, co2, co3, co4);
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                formatSize ="%-12s%14s%14s%14s%14s";
                stTitle = String.format("  "+formatSize, co1, co2, co3, co4, co5 );
            }
        }else {
            formatSize ="%-8s%10s%6s%6s"; // small size
            stTitle = String.format("  "+formatSize, co1, co2, co3, co4);
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                formatSize ="%-10s%12s%12s%12s%12s";
                stTitle = String.format("  "+formatSize, co1, co2, co3, co4, co5 );
            }
        }
        return stTitle;
    }

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
