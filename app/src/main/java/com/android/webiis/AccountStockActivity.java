package com.android.webiis;

import android.app.FragmentTransaction;
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
import android.util.Log;
import android.view.Display;
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
import com.afweb.model.stock.AFstockInfo;
import com.afweb.model.stock.AFstockObj;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountStockActivity extends AppCompatActivity implements OnDialogDoneListener{
    private static final String TAG = "AccountStockA";
    private CustomerObj customerObj;
    private  String customerObjSt;

    private ArrayList<AccountObj> accountObjList;
    private  String accountObjListSt;
    private  AccountObj accountObj;

    private ArrayList <AFstockObj> accountStockList;
    private  String accountStockListSt;

    private String stAccountName;
    private String stToastmsg;

    private String[] stockList = { ""};
    private String currentStockName="";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.add_symbol:
//                Toast.makeText(getApplicationContext(),"navigation_home", Toast.LENGTH_SHORT).show();
                    addSymbolPromptDialog();
                    return true;
                case R.id.remove_symbol:
//                    Toast.makeText(getApplicationContext(),"navigation_dashboard", Toast.LENGTH_SHORT).show();
                    delSymbolPromptDialog();
                    return true;
                case R.id.refresh:
//                    Toast.makeText(getApplicationContext(),"navigation_notifications", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(getApplicationContext(), AccountStockActivityHandler.class);

                    myIntent.putExtra("customerObjSt", customerObjSt); //Optional parameters
                    myIntent.putExtra("accountObjListSt", accountObjListSt); //Optional parameters
                    myIntent.putExtra("accountObjId", accountObj.getID());
                    myIntent.putExtra(PromptDialogFragment.ADD_SYM_CMD, "");
                    myIntent.putExtra(PromptDialogFragment.DEL_SYM_CMD, "");
                    startActivity(myIntent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_stock);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        TextView tv = findViewById(R.id.accountstock_tv);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ArrayList resultsObjects = new ArrayList();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            customerObjSt = (getIntent().getStringExtra("customerObjSt"));
            customerObj = (objectMapper.readValue(customerObjSt, CustomerObj.class));


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

            accountStockListSt =(getIntent().getStringExtra("accountStockListSt"));
            AFstockObj[] arrayItem1 = new ObjectMapper().readValue(accountStockListSt, AFstockObj[].class);
            List<AFstockObj> listItem1 = Arrays.<AFstockObj>asList(arrayItem1);
            accountStockList = (new ArrayList<AFstockObj>(listItem1));

            stToastmsg =getIntent().getStringExtra("Toastmsg");

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

            String screenSt = String.format("%.2f", screenInches);

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
            stToastmsg += " H=" + height + " W=" + width + " S=" + screenSt + " O=" + orientation;


            String accInfo = accountObj.getAccountName();
            stAccountName = "   Account: "+accInfo +"\n";;

            tv.setText(stAccountName);
//            resultsObjects.add(stAccountName);

            currentStockName="";

            String stitle = getformatFieldLV(screenInches,orientation,"Symbol", "Signal", "Per%", "Trend", "DirCh");

            resultsObjects.add(stitle);
            for (int i=0; i<accountStockList.size(); i++ ) {
                AFstockObj stockObj = accountStockList.get(i);
                String co1 = stockObj.getSymbol();
                String co2 = (int)stockObj.getTRsignal()+"";
                String co4 = (int)stockObj.getLongTerm()+"";
                String co5 = (int)stockObj.getDirection()+"";
                AFstockInfo stockInfo = stockObj.getStockInfo();
                String co3 = "-";
                if (stockInfo != null) {
                    if (stockInfo.getFopen() > 0) {
                        float ch = (stockInfo.getFclose() - stockInfo.getFopen()) / stockInfo.getFopen();
                        co3 = String.format("%.2f", ch*100);
                    }
                }
                String s = getformatFieldLV(screenInches,orientation,co1,co2,co3,co4,co5);
                resultsObjects.add(s);

                currentStockName +=","+stockObj.getSymbol().toUpperCase()+",";
            }
            stockList = (String[]) resultsObjects.toArray(new String[resultsObjects.size()]);
        } catch (IOException e) {
            e.printStackTrace();
        }


        final ListView lv =  findViewById(R.id.account_stocklv);

        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, stockList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.BLUE);
                tv.setTextSize(15);
                Typeface  childFont = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/couriernewbold.ttf");
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
                // When clicked, show a toast with the TextView text
                Toast.makeText(getApplicationContext(),
                        ((TextView) view).getText(), Toast.LENGTH_SHORT).show();

                if (position-1 < accountStockList.size()) {

                    try {

                        AFstockObj stockObj = accountStockList.get(position-1);
                        Intent myIntent = new Intent(getApplicationContext(), TrandingRuleActivityHandler.class);

                        myIntent.putExtra("customerObjSt", customerObjSt); //Optional parameters
                        myIntent.putExtra("accountObjListSt", accountObjListSt); //Optional parameters
                        myIntent.putExtra("accountObjId", accountObj.getID());

                        String stockObjSt = new ObjectMapper().writeValueAsString(stockObj);
                        myIntent.putExtra("stockObjSt", stockObjSt); //Optional parameters

                        startActivity(myIntent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }

        });
        // DataBind ListView with items from ArrayAdapter
        lv.setAdapter(arrayAdapter);
        if (stToastmsg != null) {
            if (stToastmsg.length() >0){
                Toast.makeText(this, stToastmsg, Toast.LENGTH_LONG).show();
            }
        }
    }
    ////////////////////////
    private String getformatFieldLV(double screenSize, int orientation, String co1, String co2, String co3, String co4, String co5){
        String formatSize = "";
        String stTitle = "";

        if (screenSize> 6.5) { // large screen
            formatSize ="%-12s%14s%14s%14s%14s";
            stTitle = String.format("  "+formatSize, co1, co2, co3, co4, co5 );
            return stTitle;
        }

        if (screenSize> 4.8) { // greater then 4 inch
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_stock, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){


        int id = item.getItemId();
        switch (id){
            case R.id.accountstock_settings:
                Toast.makeText(getApplicationContext(),"account_stock_settings",Toast.LENGTH_LONG).show();
                return true;
            default:
//                return super.onOptionsItemSelected(item);
                break;
        }

        //back to previous
        Intent myIntent = new Intent(getApplicationContext(), CustomerAccActivity.class);

        myIntent.putExtra("customerObjSt", customerObjSt); //Optional parameters
        myIntent.putExtra("accountObjListSt", accountObjListSt); //Optional parameters

        startActivity(myIntent);
        finish();
        return true;

    }




    private void addSymbolPromptDialog()
    {
        FragmentTransaction ft =
                getFragmentManager().beginTransaction();

        PromptDialogFragment pdf =
                PromptDialogFragment.newInstance(
                        "Enter Symbol to Add");
        pdf.show(ft, PromptDialogFragment.ADD_SYM_CMD);
    }

    private void delSymbolPromptDialog()
    {
        FragmentTransaction ft =
                getFragmentManager().beginTransaction();
        PromptDialogFragment pdf =
                PromptDialogFragment.newInstance(
                        "Enter Symbol to Remove");

        pdf.show(ft, PromptDialogFragment.DEL_SYM_CMD);
    }


    public void onDialogDone(String tag, boolean cancelled, CharSequence message) {
        String s = tag + " responds with: " + message;
        if(cancelled) {
            s = tag + " was cancelled by the user";
        }
//        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        Log.i(TAG, s);

        if (cancelled == false) {


            Intent myIntent = new Intent(getApplicationContext(), AccountStockActivityHandler.class);

            myIntent.putExtra("customerObjSt", customerObjSt); //Optional parameters
            myIntent.putExtra("accountObjListSt", accountObjListSt); //Optional parameters
            myIntent.putExtra("accountObjId", accountObj.getID());
            String symbol = "" + message;

            String testSymbol =","+symbol.toUpperCase()+",";
            if (tag.equals(PromptDialogFragment.ADD_SYM_CMD)) {
                myIntent.putExtra(PromptDialogFragment.ADD_SYM_CMD, symbol); //Optional parameters
                myIntent.putExtra(PromptDialogFragment.DEL_SYM_CMD, "");

                int index = currentStockName.indexOf(testSymbol);
                if (index > 0) {
                    // already exist
                    Toast.makeText(this, "Symbol already added", Toast.LENGTH_LONG).show();
                    return;
                }

            }else {
                myIntent.putExtra(PromptDialogFragment.ADD_SYM_CMD, ""); //Optional parameters
                myIntent.putExtra(PromptDialogFragment.DEL_SYM_CMD, symbol);

                int index = currentStockName.indexOf(testSymbol);
                if (index > 0) {
                    ;
                }   else {
                    // not found
                    Toast.makeText(this, "Symbol not found", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            startActivity(myIntent);
        }

    }


}
