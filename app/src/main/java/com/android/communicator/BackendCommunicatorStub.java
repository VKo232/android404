package com.android.communicator;

import android.util.Log;


import com.afweb.model.ConstantKey;
import com.afweb.model.LoginObj;
import com.afweb.model.SymbolNameObj;
import com.afweb.model.account.AccountObj;
import com.afweb.model.account.CustomerObj;
import com.afweb.model.account.TradingRuleObj;
import com.afweb.model.stock.AFstockObj;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class BackendCommunicatorStub implements BackendCommunicator {

    private static final String TAG = "BackendCommunicatorStub";

    @Override
    public boolean postSignIn(final String userName, final String password) throws InterruptedException {
        return false;
    }

    @Override
    public LoginObj getCustomerLogin (String gEmail, String gPass)throws Exception {
        LoginObj loginObj = null;
        String url = ConstantKey.IIS_WEB_BASE_URL+"/cust/login?email="+gEmail+"&pass="+gPass;

        String resultString =  StartHTTPrequest(url);

        if (resultString != null) {
            if (resultString.length() > 0) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    loginObj = objectMapper.readValue(resultString, LoginObj.class);
                    return loginObj;
                } catch (JsonParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


    @Override
    public ArrayList getAccountList(String gUserName, String gPass)throws Exception {
        ArrayList accountObjArray = null;
        String url = ConstantKey.IIS_WEB_BASE_URL+ "/cust/" + gUserName + "/acc/";

        String resultString =  StartHTTPrequest(url);

        if (resultString != null) {
            if (resultString.length() > 0) {
                try {

                    AccountObj[] arrayItem = new ObjectMapper().readValue(resultString, AccountObj[].class);
                    List<AccountObj> listItem = Arrays.<AccountObj>asList(arrayItem);
                    accountObjArray = new ArrayList<AccountObj>(listItem);
                    return accountObjArray;
                } catch (JsonParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
    @Override
    public boolean registerCustomer(String gEmail, String gPass, String firstName, String lastName) {

        String url = ConstantKey.IIS_WEB_BASE_URL+ "/cust/add?email=" + gEmail + "&pass="+ gPass+ "&firstName=" +firstName + "&lastName="+lastName;
        try {
            String result = StartHTTPrequest(url);

        }catch(Exception e) {
            return false;
        }
    }

    @Override
    public AccountObj getAccountObj(String gUserName, String gAccountId)throws Exception {
        AccountObj accObj = null;
        if (gAccountId == null) {
            return null;
        }
        if (gAccountId.length() == 0) {
            return null;
        }
        String url = ConstantKey.IIS_WEB_BASE_URL+ "/cust/" + gUserName + "/acc/" + gAccountId;

        String resultString =  StartHTTPrequest(url);

        if (resultString != null) {
            if (resultString.length() > 0) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    accObj = objectMapper.readValue(resultString, AccountObj.class);
                    return accObj;
                } catch (JsonParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


    @Override
    public ArrayList getAccountStockList(String gUserName, String gAccountId)throws Exception {
        ArrayList accountStockObjArray = null;
        String url = ConstantKey.IIS_WEB_BASE_URL+ "/cust/" + gUserName + "/acc/"+gAccountId+ "/st";

        String resultString =  StartHTTPrequest(url);

        if (resultString != null) {
            if (resultString.length() > 0) {
                try {

                    AFstockObj[] arrayItem = new ObjectMapper().readValue(resultString, AFstockObj[].class);
                    List<AFstockObj> listItem = Arrays.<AFstockObj>asList(arrayItem);
                    accountStockObjArray = new ArrayList<AFstockObj>(listItem);
                    return accountStockObjArray;
                } catch (JsonParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


    @Override
    public int getAccountAddStockList(String gUserName, String gAccountId, String gSymbol)throws Exception {
        ArrayList accountStockObjArray = null;
        String url = ConstantKey.IIS_WEB_BASE_URL+ "/cust/" + gUserName + "/acc/"+gAccountId+ "/st/addsymbol?symbol="+gSymbol;

        String resultString =  StartHTTPrequest(url);

        if (resultString != null) {
            if (resultString.length() > 0) {
                try {
                    int result = new ObjectMapper().readValue(resultString, Integer.class);
                    return result;

                } catch (JsonParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return 0;
    }

    @Override
    public int getAccountRemoveStockList(String gUserName, String gAccountId, String gSymbol)throws Exception {
        ArrayList accountStockObjArray = null;
        String url = ConstantKey.IIS_WEB_BASE_URL+ "/cust/" + gUserName + "/acc/"+gAccountId+ "/st/removesymbol?symbol="+gSymbol;

        String resultString =  StartHTTPrequest(url);

        if (resultString != null) {
            if (resultString.length() > 0) {
                try {
                    int result = new ObjectMapper().readValue(resultString, Integer.class);
                    return result;

                } catch (JsonParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return 0;
    }


    @Override
    public ArrayList getTradingRuleList(String gUserName, String gAccountId, String gSymbol)throws Exception {
        ArrayList TradingRuleObjArray = null;

        SymbolNameObj symObj = new SymbolNameObj(gSymbol);
        String NormalizeSymbol = symObj.getSymbolFileName();
        String url = ConstantKey.IIS_WEB_BASE_URL+ "/cust/" + gUserName + "/acc/"+gAccountId+ "/st/"+NormalizeSymbol+"/tr";

        String resultString =  StartHTTPrequest(url);

        if (resultString != null) {
            if (resultString.length() > 0) {
                try {

                    TradingRuleObj[] arrayItem = new ObjectMapper().readValue(resultString, TradingRuleObj[].class);
                    List<TradingRuleObj> listItem = Arrays.<TradingRuleObj>asList(arrayItem);
                    TradingRuleObjArray = new ArrayList<TradingRuleObj>(listItem);

                    return TradingRuleObjArray;
                } catch (JsonParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    public AFstockObj geAFstockObj(String gSymbol)throws Exception {
        AFstockObj stockObj = null;

        SymbolNameObj symObj = new SymbolNameObj(gSymbol);
        String NormalizeSymbol = symObj.getSymbolFileName();
        String url = ConstantKey.IIS_WEB_BASE_URL+ "/st/"+NormalizeSymbol;

        String resultString =  StartHTTPrequest(url);

        if (resultString != null) {
            if (resultString.length() > 0) {
                try {

                    ObjectMapper objectMapper = new ObjectMapper();
                    stockObj = objectMapper.readValue(resultString, AFstockObj.class);
                    return stockObj;
                } catch (JsonParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    //////////////////////////
    protected String StartHTTPrequest(String url) throws Exception {
        Log.i(TAG, "StartHTTPrequest " +url);
        BufferedReader in = null;
        String resultString = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);


            in = new BufferedReader(
                    new InputStreamReader(
                            response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            resultString = sb.toString();
            Log.i(TAG, "StartHTTPrequest resultString " +resultString);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("Error send REST request");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return resultString;
    }


    protected String StartHTTPpost(String url) {
        Log.i(TAG, "StartHTTPpost " +url);
        BufferedReader in = null;
        String resultString = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(
                    "http://www.androidbook.com/akc/display");
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("url", "DisplayNoteIMPURL"));
            postParameters.add(new BasicNameValuePair("reportId", "4788"));
            postParameters.add(new BasicNameValuePair("ownerUserId", "android"));
            postParameters.add(new BasicNameValuePair("aspire_output_format", "embedded-xml"));

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                    postParameters);

            request.setEntity(formEntity);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(
                    new InputStreamReader(
                            response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            resultString = sb.toString();
            Log.i(TAG, "StartHTTPpost resultString " +resultString);

        } catch(Exception e) {
            // Do something about exceptions
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultString;
    }
}