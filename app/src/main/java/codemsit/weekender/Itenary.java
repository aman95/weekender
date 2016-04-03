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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import codemsit.weekender.network.Connectiondetector;

/**
 * Created by aman on 02/04/16.
 */
public class Itenary extends AppCompatActivity {

    Button invite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itenary);
        /*String fontPath = "fonts/Walkway_Oblique.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);*/

        invite = (Button)findViewById(R.id.invite);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hi download this app and share holidays with me";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Holiday Time");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

    }

}

