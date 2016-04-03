package codemsit.weekender;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import codemsit.weekender.network.Connectiondetector;

public class CustomerLogin extends AppCompatActivity implements View.OnClickListener {
    CallbackManager callbackManager;
    LoginButton loginfb;
    TextView fblogin;
    EditText username,password;
    String facebookname= "", facebookemail="",email="",phone= "",name= "";
    public static String Userid;
    TextView signuplink;
    TextView forgotpassword;
    private Button login;
    Connectiondetector cd;
    GoogleCloudMessaging gcm;
    Boolean isInternetPresent= false;
    public static Activity cl;
    Context context;
    String SENDER_ID = "332323308650";
    public static String BillId = null;
    Intent intent;

    ProgressDialog mProgressDialog;
    View b;
    SessionManager session;
    //String useremail,pass;
    private boolean clicked = false;
    String regid="",checkstatus= "";

    private static final int REQUEST_SIGNUP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();

        gcm = GoogleCloudMessaging.getInstance(this);

        new RegisterBackground().execute();

        setContentView(R.layout.activity_customer_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Login");
        session = new SessionManager(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginfb = (LoginButton)findViewById(R.id.fb_login);
        loginfb.setText("CONNECT WITH FACEBOOK");
        cd = new Connectiondetector(getApplicationContext());
        cl=this;
        isInternetPresent = cd.isConnectingToInternet();
        //BillId = intent.getStringExtra("billid");


        loginfb .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        loginfb.setReadPermissions("public_profile email");

        loginfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInternetPresent) {
                    Toast.makeText(getApplicationContext(),
                            "Internet not present", Toast.LENGTH_SHORT).show();
                } else if (isInternetPresent) {


                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();

                        Toast.makeText(getApplicationContext(),
                                "Access token present", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        loginfb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                if (AccessToken.getCurrentAccessToken() != null) {
                    RequestData();
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
            }
        });





        //
        //  login.setOnClickListener(this);

    }


    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object,GraphResponse response) {

                JSONObject json = response.getJSONObject();
                try {
                    if(json != null){
                        facebookname=json.getString("name");
                        facebookemail=json.getString("email");

                        Log.d("facebook name",facebookname);
                        Log.d("facebook email",facebookemail);
                        String urlemailcheck="";


                        Log.d("i came here 2","i came here 2");
                        mProgressDialog = new ProgressDialog(CustomerLogin.this);
                        // Set progressdialog title
                        mProgressDialog.setMessage("Verifying Email");
                        mProgressDialog.setIndeterminate(false);
                        mProgressDialog.setCancelable(false);
                        // Show progressdialog
                        mProgressDialog.show();
                        urlemailcheck = "https://strawberry-crumble-31686.herokuapp.com/login/"+facebookname+"/"+facebookemail+"/1234567890";
                        urlemailcheck = urlemailcheck.replaceAll(" ", "%20");
                        Log.d("url", urlemailcheck);
                        new Checkemail().execute(urlemailcheck);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();



    }
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();


        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


        Log.d("i came here", "i came here");

    }

    @Override
    public void onClick(View v) {

        if(!isInternetPresent) {
            Toast.makeText(getApplicationContext(),
                    "Internet not present", Toast.LENGTH_SHORT).show();}

        else if(isInternetPresent) {



        }}

    public String readJSONFeed(String URL) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }
        } catch (Exception e) {

        }
        return stringBuilder.toString();
    }


    public class Checkemail extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(CustomerLogin.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Verifying Email");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            // Show progressdialog
            mProgressDialog.show();

        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    Log.d("str", response.toString());
                    checkemailfunction(response.toString());

                    result = 1; // Successful

                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                //Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            try {
                if ((mProgressDialog != null) &&  mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            } catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                mProgressDialog = null;
            }
            if (result == 1) {


                session.createLoginSession(facebookemail,facebookname);

                Intent i= new Intent(CustomerLogin.this,HomeScreen.class);
                i.putExtra("USER_ID", Userid);
                i.putExtra("USER_NAME", name);
                i.putExtra("USER_EMAIL", email);

                startActivity(i);





            }

            else {
                Intent i= new Intent(CustomerLogin.this,HomeScreen.class);
                startActivity(i);

                Toast.makeText(CustomerLogin.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void checkemailfunction(String result) {
        try {
            Log.d("this is result", result);
            JSONObject jsonObject= new JSONObject(result);
            JSONObject jsonObject1 = jsonObject.getJSONObject("user");

            Userid = jsonObject1.getString("id");
            name = jsonObject1.getString("name");
            email = jsonObject1.getString("email");




            // checkstatus= "1";
            Log.d("hello", Userid);

        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }


    class RegisterBackground extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }

                regid = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regid;
                Log.d("111", msg);
                sendRegistrationIdToBackend(regid);

            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            Log.d("checking",msg);

        }
        private void sendRegistrationIdToBackend(String storeregid)
        {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            //  HttpPost httppost = new HttpPost("http://158.85.122.170:81/UI/notif/start_notify.php");
            //session.storeregid(storeregid,tokenvalue);


            // Add your data
                /*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("id", regid));
                nameValuePairs.add(new BasicNameValuePair("type","android"));
                nameValuePairs.add(new BasicNameValuePair("imei",imei));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);*/

            System.out.print("here is regid"+ regid);
           // System.out.print("here is imei"+ imei);

            /*} catch (ClientProtocolException e) {
                System.out.print("here is exception"+ e);

                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }*/



        }



    }

}



