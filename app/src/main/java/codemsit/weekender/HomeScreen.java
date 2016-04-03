package codemsit.weekender;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codemsit.weekender.adapters.MyRecyclerAdapter4;
import codemsit.weekender.models.FeedItem;
import codemsit.weekender.network.Connectiondetector;

public class HomeScreen extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener {
    ProgressDialog mProgressDialog;
    Connectiondetector cd;
    Boolean isInternetPresent = false;
    private static final String TAG = "Bills";
    private List<FeedItem> feedsList;
    private List<FeedItem> feedsListmore;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter4 adapter;
    private LinearLayoutManager mLayoutManager;
    // private LinearLayoutManager layoutmanager;

    final Context context = this;

    public static Activity ha;
    private int offsetnew = 0;
    private static Context VContext;

    private int flagnew = 0;
    TextView merchName, merchEmail;
    ImageView merchImage;
    Bitmap bitmap;
    protected Handler handler;
    String sign;
    String midchange;
    String checkerror = "";
    SessionManager session;
    private int backpresscount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ha = this;
        cd = new Connectiondetector(getApplicationContext());






        /*String fontPath = "fonts/Walkway_Black.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);*/
        session = new SessionManager(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);




        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.takeorder);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent createbill = new Intent(getApplicationContext(),CreateBill.class);
                startActivity(createbill);
                *//*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*//*
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_home_screen, null);
        navigationView.addHeaderView(header);

        merchName = (TextView)header.findViewById(R.id.MerchantName);
        merchEmail = (TextView) header.findViewById(R.id.MerchantEmail);
        merchImage = (ImageView) header.findViewById(R.id.MerchantImg);


        boolean check = session.checkLogin();

        if(check)
        {
            this.finish();
        }
        else {


            HashMap<String, String> user = session.getUserDetails();

            String merchemail = user.get(SessionManager.KEY_Email);

            // email
            String merchname = user.get(SessionManager.KEY_MerchantName);

            String url = "https://gist.githubusercontent.com/goelvibhor4/89df49bfb222233c2bc3de82867e60f7/raw/fc4f80ac9d40c06f05f019a4d12dd4276ac4fecb/FromDelhi.json";

            new AsyncHttpTask().execute(url);

            merchName.setText(merchname);
            merchEmail.setText(merchemail);

        }
            /*HashMap<String, String> user = session.getUserDetails();

            String merchemail = user.get(SessionManager.KEY_Email);

            // email
            String merchname = user.get(SessionManager.KEY_MerchantName);

            String imgurl = user.get(SessionManager.KEY_LogoUrl);

            final String mid = user.get(SessionManager.KEY_MID);
            Log.d("checkmid", "" + mid);

            String signature = user.get(SessionManager.KEY_Sign);



            Log.d("sign",""+sign);

            midchange = mid;

            urlID += mid +"&reg_id="+signature;

            urlfix += mid +"&limit=50" + "&offset=0" + "&token="+signature;
            Log.d("fixedurl",""+urlfix);
            url += mid + "&limit=50" + "&offset="+offsetnew + "&token="+signature;
            Log.d("firsturl",""+url);
            new AsyncHttpTask().execute(url);


            merchName.setText(merchname);
            merchEmail.setText(merchemail);*/




    //  boolean c = session.isLoggedIn();


    //Log.d("testit",""+merchemail);


    // layoutmanager = new (this);
    final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    // layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    mRecyclerView.setLayoutManager(layoutManager);


    // mRecyclerView.setHasFixedSize(true);
    //recycler view onscroll




    }



    @Override
    public void onResume()
    {
        super.onResume();

        boolean check = session.checkLogin();

        if(check)
        {
            this.finish();
        }
        else
        {
            /*Log.d("sdhs","kgfogfow");
            Intent i = getIntent();
            String merchemail = i.getStringExtra("email");
            Log.d("testit",""+merchemail);
            merchEmail.setText(merchemail);*/
            //session.checkLogin();
        }

    }
    @Override
    public void onBackPressed() {


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CreateMenu(menu);
        return true;
    }

    @SuppressLint("NewApi")
    private void CreateMenu(Menu menu)
    {
        MenuItem menu1=menu.add(0,0,0,"AddItem");
        {
            menu1.setIcon(R.drawable.markerfilled50);
            menu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();
                return true;
            case 0:
                isInternetPresent = cd.isConnectingToInternet();
                if(isInternetPresent) {
                    Toast.makeText(getApplicationContext(), "Select your location", Toast.LENGTH_SHORT).show();
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.prompts, null);

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                    builder.setView(promptsView);

                    final FrameLayout jai = (FrameLayout) promptsView
                            .findViewById(R.id.jaiframe);

                    final FrameLayout mub = (FrameLayout)promptsView.findViewById(R.id.mumframe);
                    final FrameLayout bag = (FrameLayout)promptsView.findViewById(R.id.bagframe);
                    final FrameLayout hyd = (FrameLayout)promptsView.findViewById(R.id.hdbframe);

                    final android.app.AlertDialog alert = builder.create();
                    alert.show();
                    jai.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = "https://gist.githubusercontent.com/goelvibhor4/5eba5ad8b0dd156ecde0ac62a7828d31/raw/3e3bac0b2bf2119a8de9e27e30b77b1d39e0d6fe/FromJaipur.json";
                            new AsyncHttpTask().execute(url);
                            alert.dismiss();
                        }
                    });
                    mub.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = "https://gist.githubusercontent.com/goelvibhor4/bab2d68064f62795cdbd6e0315e17a33/raw/e68c7076982b02579624366cb5ce58b029203a9c/FromBombay";
                            new AsyncHttpTask().execute(url);
                            alert.dismiss();
                        }
                    });
                    bag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = "https://gist.githubusercontent.com/goelvibhor4/4457dac50f2174f2c77d0633754bd0f6/raw/3f933c50b2471068c8a7554ab1b2ffd6a3b1db30/FromBangalore.json";
                            new AsyncHttpTask().execute(url);
                            alert.dismiss();
                        }
                    });
                    hyd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = "https://gist.githubusercontent.com/goelvibhor4/c1ba25e11f9577997202449ade33451c/raw/f4afb4335173ac4ae797d87886c96b859900e3aa/FromHyderabad.json";
                            new AsyncHttpTask().execute(url);
                            alert.dismiss();
                        }
                    });



                    return true;
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not Connected to the Internet", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_menu) {
//            /*Intent about = new Intent(getApplicationContext(),MainActivity.class);
//            startActivity(about);*/
//           // HomeScreen.this.finish();
//            // Handle the camera action
//        } else if (id == R.id.nav_bills) {
//
//        } else if (id == R.id.navByDates) {
//
//            /*Intent bydates = new Intent(getApplicationContext(),BillsByDate.class);
//            startActivity(bydates);*/
//
//        } else if (id == R.id.nav_TandC) {
//
//            /*Intent about = new Intent(getApplicationContext(),TermsPrivacy.class);
//            startActivity(about);*/
//
//        }        else
        if (id == R.id.navAboutus) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Hi download this app and share holidays with me";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Holiday Time");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

            /*Intent about = new Intent(getApplicationContext(),AboutUs.class);
            startActivity(about);*/

        } else if (id == R.id.navLogout) {

            session.logoutUser();
            /*isInternetPresent = cd.isConnectingToInternet();

            if(isInternetPresent)
            {
                Log.d("logout",urlID);

                new AsyncLogoutHttpTask().execute(urlID);

                session.logoutUser();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Not Connected to the Internet",Toast.LENGTH_SHORT).show();
            }*/



        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> implements Serializable {

        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(HomeScreen.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Loading");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);

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
                    Log.d("str",response.toString());
                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            try {
                if ((mProgressDialog != null) &&  mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                /*else if (offsetnew>=10) {
                    mProgressDialog.dismiss();
                }*/
            } catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                mProgressDialog = null;
            }

            if (result == 1) {
                //feedsListmore.add(feedsList);


                    adapter = new MyRecyclerAdapter4(HomeScreen.this, feedsList);
                    mRecyclerView.setAdapter(adapter);
                    //adapter.notifyItemInserted(feedsListmore.size());
                    // adapter.notifyDataSetChanged();




            } else {

                        Toast.makeText(HomeScreen.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }

            }

    }

    private void parseResult(String result) {
        try {
            JSONArray response = new JSONArray(result);



                //JSONArray posts = response.optJSONArray("");
                feedsList = new ArrayList<>();


                for (int i = 0; i < response.length(); i++) {
                    JSONObject post4 = response.getJSONObject(i);
                    //Log.d("value4", post4.toString());
                    FeedItem item = new FeedItem();
                    Log.d("value4", post4.optString("name"));
                    item.setId(post4.optString("id"));
                    item.setName(post4.optString("name"));
                    item.setDescription(post4.optString("intro"));
                    item.setLogo(post4.optString("image"));
                    item.setFoodType(post4.optString("idealNoOfDays"));
                   // item.setSlider(post4.optString("logo"));
                    item.setSliderstring(post4.optString("image"));
                    item.setDistance(post4.optString("distance"));

               /* item.setThumbnail(post.optString("thumbnail"));
*/

                    feedsList.add(item);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }








}
