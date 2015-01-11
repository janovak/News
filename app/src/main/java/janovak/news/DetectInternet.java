package janovak.news;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by John on 1/11/2015.
 */
public class DetectInternet {
    private static final String TAG = "DetectInternet";
    private static DetectInternet instance = null;

    public DetectInternet() {
    }

    public static DetectInternet getInstance() {
        if (instance == null) {
            instance = new DetectInternet();
        }
        return instance;
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        return (network != null && network.isConnected());
    }

    public boolean hasInternetConnection(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection url = (HttpURLConnection) (new URL("http;//www.google.com").openConnection());
                url.setRequestProperty("User-Agent", "Test");
                url.setRequestProperty("Connection", "close");
                url.setConnectTimeout(1500);
                url.connect();
                return (url.getResponseCode() == 200);
            } catch (MalformedURLException mue) {
                Log.e(TAG, "Malformed URL", mue);
            } catch (IOException ioe) {
                Log.e(TAG, "IO Exception", ioe);
            }
        } else {
            Log.d(TAG, "No network is available");
        }
        return false;
    }
}
