package janovak.news;

import android.util.Log;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by John on 1/10/2015.
 */
public class OAuth {
    private static final String TAG = "OAuth";
    private static OAuth instance = null;

    protected OAuth () {
    }

    public static OAuth getInstance() {
        if (instance == null) {
            instance = new OAuth();
        }
        return instance;
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

    protected Twitter authenticateTwitter() {
        // TODO: Check if already logged in

        byte[] consumerKey = {27, 113, 10, 11, 46, 31, 117, 38, 113, 124, 42, 30, 20, 10, 125, 40, 54, 29, 106, 7, 100, 61, 1, 109, 29, 54, 34, 59, 17, 10, 45, 60, 48, 2, 15, 47, 44, 52, 9, 59, 43, 61, 19, 35, 27, 51, 24, 21, 45, 29};
        byte[] accessTokenKey = {12, 36, 14, 50, 63, 47, 112, 42, 113, 8, 125, 20, 53, 63, 60, 27, 48, 55, 39, 32, 24, 16, 45, 51, 43, 32, 55, 26, 4, 42, 52, 17, 55, 89, 42, 40, 24, 57, 34, 25, 24, 42, 5, 48, 7};
        byte[] key = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122};

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey("8SlS8vv86k99lvRtbB9CgDLBb");
        cb.setOAuthAccessToken("2955289110-Y62hNjZurXZIdOQRTw2opm4tp3RBrcqp9zh1Cwr");
        try {
            cb.setOAuthConsumerSecret(new String(xor(consumerKey, key)));
            cb.setOAuthAccessTokenSecret(new String(xor(accessTokenKey, key)));
        } catch (IllegalArgumentException iae) {
            Log.e(TAG, "Illegal argument", iae);
        }
        return new TwitterFactory(cb.build()).getInstance();
    }
}
