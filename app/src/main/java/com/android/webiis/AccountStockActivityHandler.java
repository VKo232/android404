package com.android.webiis;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.afweb.model.account.AccountObj;
import com.afweb.model.account.CustomerObj;
import com.afweb.model.stock.AFstockObj;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountStockActivityHandler extends AppCompatActivity implements SignInModel.Observer {
    private static final String TAG = "AccountStockAH";
    private static final String TAG_WORKER = "TAG_WORKER";
    private CustomerObj customerObj;
    private  String customerObjSt;

    private ArrayList<AccountObj> accountObjList;
    private  String accountObjListSt;

    private  AccountObj accountObj;

    private String addSymbolCmd;
    private String delSymbolCmd;

    private SignInModel mSignInModel;
    private View mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_stock);

        mProgress = findViewById(R.id.iisweb_progress);
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


            addSymbolCmd =getIntent().getStringExtra(PromptDialogFragment.ADD_SYM_CMD);
            delSymbolCmd =getIntent().getStringExtra(PromptDialogFragment.DEL_SYM_CMD);

        } catch (IOException e) {
            e.printStackTrace();
        }

        final SignInWorkerFragment retainedWorkerFragment =
                (SignInWorkerFragment) getFragmentManager().findFragmentByTag(TAG_WORKER);

        if (retainedWorkerFragment != null) {
            mSignInModel = retainedWorkerFragment.getSignInModel();
        } else {
            final SignInWorkerFragment workerFragment = new SignInWorkerFragment();

            getFragmentManager().beginTransaction()
                    .add(workerFragment, TAG_WORKER)
                    .commit();

            mSignInModel = workerFragment.getSignInModel();
        }

        mSignInModel.registerObserver(this);
        if (addSymbolCmd != null) {
            if (addSymbolCmd.length() >0) {
                mSignInModel.signInAccountAddStock(customerObj.getUserName(), "" + accountObj.getID(), addSymbolCmd);
                return;
            }
        }

        if (delSymbolCmd != null) {
            if (delSymbolCmd.length() >0) {
                mSignInModel.signInAccountRemoveStock(customerObj.getUserName(), "" + accountObj.getID(), delSymbolCmd);
                return;
            }
        }
        mSignInModel.signInAccountStock(customerObj.getUserName(), "" + accountObj.getID());
    }


    ////////////////////////////

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
        mSignInModel.unregisterObserver(this);

        if (isFinishing()) {
            mSignInModel.stopSignIn();
        }
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "onRestart");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }

//	@Override
//	protected void onPause() {
//		Log.i(TAG, "onPause");
//		super.onResume();
//	}

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onSignInStarted(final SignInModel signInModel) {
        Log.i(TAG, "onSignInStarted");
        showProgress(true);
    }

    @Override
    public void onSignInSucceeded(final SignInModel signInModel) {
        Log.i(TAG, "onSignInSucceeded");
        finish();

        try {
            int mIISWebfunction = signInModel.getmIISWebfunction();;

            ArrayList <AFstockObj> accountStockList = signInModel.getAccountStockList();
            if (accountStockList != null) {
                Intent myIntent = new Intent(getApplicationContext(), AccountStockActivity.class);

                myIntent.putExtra("customerObjSt", customerObjSt); //Optional parameters
                myIntent.putExtra("accountObjListSt", accountObjListSt); //Optional parameters
                myIntent.putExtra("accountObjId", accountObj.getID());

                String accountStockListSt = new ObjectMapper().writeValueAsString(accountStockList);
                myIntent.putExtra("accountStockListSt", accountStockListSt); //Optional parameters

                if (mIISWebfunction == SignInModel.IISWEB_GET_ACCOUNTADDSTOCK) {
                    myIntent.putExtra("Toastmsg", "Stock Added Successfully"); //Optional parameters
                } else  if (mIISWebfunction == SignInModel.IISWEB_GET_ACCOUNTREMOVESTOCK) {
                    myIntent.putExtra("Toastmsg", "Stock Removed Successfully"); //Optional parameters
                } else {
                    myIntent.putExtra("Toastmsg", ""); //Optional parameters
                }

                startActivity(myIntent);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            myIntent.putExtra("failMsg", "Network failed: Please try again later");
            startActivity(myIntent);
        }

    }

    @Override
    public void onSignInFailed(final SignInModel signInModel) {
        Log.i(TAG, "onSignInFailed");
//        showProgress(false);
        finish();

        try {

            ArrayList <AFstockObj> accountStockList = signInModel.getAccountStockList();
            if (accountStockList != null) {
                Intent myIntent = new Intent(getApplicationContext(), AccountStockActivity.class);

                myIntent.putExtra("customerObjSt", customerObjSt); //Optional parameters
                myIntent.putExtra("accountObjListSt", accountObjListSt); //Optional parameters
                myIntent.putExtra("accountObjId", accountObj.getID());
                String accountStockListSt = new ObjectMapper().writeValueAsString(accountStockList);
                myIntent.putExtra("accountStockListSt", accountStockListSt); //Optional parameters

                int mIISWebfunction = signInModel.getmIISWebfunction();
                int ResultAddRemoveStock = signInModel.getResultAddRemoveStock();

                switch (mIISWebfunction) {
                    case SignInModel.IISWEB_GET_ACCOUNTADDSTOCK:
                        if (ResultAddRemoveStock == AccountObj.MAX_ALLOW_STOCK_ERROR) {
                            myIntent.putExtra("Toastmsg", "Stock Added Failed - Exceeding 20 stock"); //Optional parameters
                        } else {
                            myIntent.putExtra("Toastmsg", "Stock Added Failed"); //Optional parameters
                        }
                        break;
                    case SignInModel.IISWEB_GET_ACCOUNTREMOVESTOCK:
                        myIntent.putExtra("Toastmsg", "Stock Removed Failed"); //Optional parameters
                        break;

                    default:
                        myIntent.putExtra("Toastmsg", ""); //Optional parameters
                }
                startActivity(myIntent);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            myIntent.putExtra("failMsg", "Network failed: Please try again later");
            startActivity(myIntent);
        }

    }

    private void showProgress(final boolean show) {
        mProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

}



