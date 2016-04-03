package codemsit.weekender;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.github.florent37.glidepalette.GlidePalette;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import codemsit.weekender.fragments.FoodFragment;
import codemsit.weekender.fragments.POIFragment;
import codemsit.weekender.fragments.TabsFragment;
import codemsit.weekender.ui.ResizableImageView;

public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    String seasonArray, position;
    JSONArray mJsonArray;
    String title;
    String url;
    String tripID;
    CollapsingToolbarLayout collapsingToolbar;
    private int tabColor, statusBarColor, flag = 0, scrollRange = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        url = intent.getStringExtra("imgUrl");
        tripID = intent.getStringExtra("tripid");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(App.getAppContext(), Itenary.class);
                startActivity(i);
            }
        });

//        seasonArray = intent.getStringExtra("seasonArray");
//        position = intent.getStringExtra("position");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(this);
        ResizableImageView imageView = (ResizableImageView) findViewById(R.id.backdrop);

        try {
            //String url = "http://www.holidify.com/images/bgImages/MADHYAMAHESHWAR.jpg";
            Glide.with(this)
                    .load(url)
                    .listener(GlidePalette.with(url)
                            .use(GlidePalette.Profile.VIBRANT)
                            .intoBackground(tabLayout, GlidePalette.Swatch.RGB)
                            .crossfade(true)
                            .intoCallBack(
                                    new GlidePalette.CallBack() {
                                        @Override
                                        public void onPaletteLoaded(Palette palette) {
                                            Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                                            Palette.Swatch darkVibrantSwatch = palette.getDarkVibrantSwatch();
                                            if (darkVibrantSwatch != null && vibrantSwatch != null) {
                                                flag = 1;
                                                tabColor = vibrantSwatch.getRgb();
                                                statusBarColor = darkVibrantSwatch.getRgb();
                                            }
                                        }
                                    }))
                    .into(imageView);
            collapsingToolbar.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FoodFragment(), "Food / Drinks");
        adapter.addFragment(new POIFragment(), "Sight Seeing");

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (flag == 1) {
            if (scrollRange == -1) {
                scrollRange = appBarLayout.getTotalScrollRange();
            }
            if (scrollRange + verticalOffset == 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, null));
                    collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.colorPrimary, null));
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
                }
            } else if (verticalOffset == 0) {
                tabLayout.setBackgroundColor(tabColor);
                collapsingToolbar.setContentScrimColor(tabColor);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(statusBarColor);
                }
            }
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
