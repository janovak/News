package janovak.news;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void refresh(View view) {
        String[] usernames = new String[] {"hackequalsone", "ColinHoekje"};

        new GetTweets().execute(usernames);
    }

    class GetTweets extends AsyncTask<String, String, List<Status>> {
        @Override
        protected List<twitter4j.Status> doInBackground(String... usernames) {
            byte[] consumerKey = {27, 113, 10, 11, 46, 31, 117, 38, 113, 124, 42, 30, 20, 10, 125, 40, 54, 29, 106, 7, 100, 61, 1, 109, 29, 54, 34, 59, 17, 10, 45, 60, 48, 2, 15, 47, 44, 52, 9, 59, 43, 61, 19, 35, 27, 51, 24, 21, 45, 29};
            byte[] accessTokenKey = {12, 36, 14, 50, 63, 47, 112, 42, 113, 8, 125, 20, 53, 63, 60, 27, 48, 55, 39, 32, 24, 16, 45, 51, 43, 32, 55, 26, 4, 42, 52, 17, 55, 89, 42, 40, 24, 57, 34, 25, 24, 42, 5, 48, 7};
            byte[] key = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122};

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true);
            cb.setOAuthConsumerKey("8SlS8vv86k99lvRtbB9CgDLBb");
            cb.setOAuthConsumerSecret(new String(xor(consumerKey, key)));
            cb.setOAuthAccessToken("2955289110-Y62hNjZurXZIdOQRTw2opm4tp3RBrcqp9zh1Cwr");
            cb.setOAuthAccessTokenSecret(new String(xor(accessTokenKey, key)));
            Twitter twitter = new TwitterFactory(cb.build()).getInstance();
            List<twitter4j.Status> ret = new LinkedList<>();

            try {
                ResponseList<User> users = twitter.lookupUsers(usernames);
                ResponseList<twitter4j.Status> statuses;

                // Get all recent tweets from all specified users
                for (User user : users) {
                    statuses = twitter.getUserTimeline(user.getId());
                    for (twitter4j.Status status : statuses) {
                        ret.add(status);
                    }
                }
            } catch (TwitterException te) {
                te.printStackTrace();
                System.out.println("Failed to get timeline: " + te.getMessage());
                System.exit(-1);
            }

            return ret;
        }

        protected void onPostExecute(List<twitter4j.Status> statuses) {
            TextView tweets = (TextView) findViewById(R.id.statusCount);

            tweets.setText("" + statuses.size());
        }

        private byte[] xor(final byte[] input, final byte[] secret) {
            final byte[] output = new byte[input.length];
            if (secret.length == 0) {
                throw new IllegalArgumentException("Empty security key");
            }
            for (int i = 0, j = 0; i < input.length; ++i, ++j) {
                output[i] = (byte) (input[i] ^ secret[j]);
                if (j >= secret.length - 1) {
                    j = 0;
                }
            }
            return output;
        }


    }
}
