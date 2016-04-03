package codemsit.weekender;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import codemsit.weekender.network.Connectiondetector;

/**
 * Created by aman on 02/04/16.
 */
public class MainActivity2 extends AppCompatActivity {
    Boolean isInternetPresent = false;
    Connectiondetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        /*String fontPath = "fonts/Walkway_Oblique.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);*/
        cd = new Connectiondetector(getApplicationContext());
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        // image = (ImageView)findViewById(R.id.imageView2);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "codemsit.weekender",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        isInternetPresent = cd.isConnectingToInternet();
        Log.d("test", isInternetPresent.toString());



        if(isInternetPresent) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    Intent it = new Intent(getApplicationContext(),
                            HomeScreen.class);

                    startActivity(it);

                    MainActivity2.this.finish();

                }
            }, 3000);
        }
        else
        {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    Intent it = new Intent(getApplicationContext(),
                            HomeScreen.class);

                    startActivity(it);

                    MainActivity2.this.finish();
                    Toast.makeText(getApplicationContext(),"Not connected to the internet",Toast.LENGTH_SHORT).show();
                }
            }, 3000);
        }
    }




}

