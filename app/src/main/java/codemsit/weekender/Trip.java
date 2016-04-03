package codemsit.weekender;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import codemsit.weekender.network.Connectiondetector;

/**
 * Created by SHUBHAM on 02-04-2016.
 */
public class Trip extends AppCompatActivity implements ObservableScrollViewCallbacks {
    private static Context VContext;
    private ImageView mImageView;
    private View mToolbarView;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    ProgressDialog mProgressDialog;
    Connectiondetector cd;
    Boolean isInternetPresent= false;
    String startdate;
    Calendar myCalendar = Calendar.getInstance();
    Button plantrip;
    String url="",tripid;
    String Restaurantname;
    String Rsliderimage;
    protected static final String TAG = "Trip activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        // getting intent data
        Intent in = getIntent();
        cd = new Connectiondetector(getApplicationContext());

        isInternetPresent = cd.isConnectingToInternet();

         Restaurantname = in.getStringExtra("rname");
        String Sdescription = in.getStringExtra("rdescription");

        Rsliderimage = in.getStringExtra("rslider");
        mImageView = (ImageView)findViewById(R.id.image);
        Picasso.with(VContext).load(Rsliderimage)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(mImageView);

        url = "https://apricot-sundae-22518.herokuapp.com/trip"+"/"+CustomerLogin.Userid+"/"+Restaurantname+"/";

        String foodtype = in.getStringExtra("foodtype");
        Log.d("this is rslider", Rsliderimage);
        mToolbarView = findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(null);

        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.primary)));


        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);



        TextView id = (TextView) findViewById(R.id.id);
        TextView name = (TextView) findViewById(R.id.body);

        TextView description = (TextView) findViewById(R.id.descriptiondata);
        TextView waiter = (TextView)findViewById(R.id.WaiterBulletPointsdata);
        plantrip = (Button)findViewById(R.id.planbutton);

        View overlay = (View) findViewById(R.id.overlay);
        int opacity = 75; // from 0 to 255
        overlay.setBackgroundColor(opacity * 0x1000000); // black with a variable alpha
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, 580);
        overlay.setLayoutParams(params);
        overlay.invalidate(); // update the view


        name.setText(Restaurantname);
        description.setText(Sdescription);
        waiter.setText(foodtype);


        final   DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        plantrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Trip.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                    url+=startdate+"/2";
                url = url.replaceAll(" ", "%20");
                    new AsyncHttpTask().execute(url);
                Log.d("tripurl",url);


            }
        });


    }


    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startdate = sdf.format(myCalendar.getTime());
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.primary);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        ViewHelper.setTranslationY(mImageView, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                super.onBackPressed();
                break;


        }
        return super.onOptionsItemSelected(item);


    }


    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> implements Serializable {

        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(Trip.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Loading");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);

            // mProgressDialog.show();

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


                Intent i = new Intent(App.getAppContext(), MainActivity.class);
                i.putExtra("title",Restaurantname);
                i.putExtra("imgUrl", Rsliderimage);
                i.putExtra("tripId", tripid);
                startActivity(i);
                finish();

                //adapter.notifyItemInserted(feedsListmore.size());
                // adapter.notifyDataSetChanged();




            } else {

                Toast.makeText(Trip.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);


            tripid = response.optString("trip_id");

            Log.d("tripid",tripid);
            //JSONArray posts = response.optJSONArray("");




               /* item.setThumbnail(post.optString("thumbnail"));
*/






        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
