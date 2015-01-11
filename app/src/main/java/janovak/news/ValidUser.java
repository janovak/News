package janovak.news;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by John on 1/10/2015.
 */
public class ValidUser extends AsyncTask<String, Twitter, Boolean> {
    private static final String TAG = "ValidUser";
    private Twitter twitter;
    private Context context;

    public ValidUser(Twitter twitter, Context context) {
        this.twitter = twitter;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... username) {
        if (!DetectInternet.getInstance().hasInternetConnection(context)) {
            // Do not have an internet connection
            return false;
        }

        try {
            twitter.showUser(username[0]);
            return true;
        } catch (TwitterException te) {
            if (te.getStatusCode() == 404) {
                // TODO: Alert user that Twitter handle was invalid
            } else {
                Log.e(TAG, "Could not access Twitter", te);
                // TODO: Alert user that Twitter could not be accessed
            }
        }
        return false;
    }
}
