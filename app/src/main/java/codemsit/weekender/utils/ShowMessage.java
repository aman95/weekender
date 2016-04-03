package codemsit.weekender.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import codemsit.weekender.App;

/**
 * Created by aman on 2/10/15.
 */
public class ShowMessage {
    static Context context = App.getAppContext();

    public static void toast(String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
        Log.d("ShowMessage",string);
    }

    public static void log(String string) {

        Log.d("ShowMessage",string);
    }

    public static void snackBar(String string, View view) {

        Log.d("ShowMessage",string);
        Snackbar.make(view, string, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }
}
