package janovak.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;

import twitter4j.Twitter;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = "MainActivity";
    private Twitter twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        twitter = OAuth.getInstance().authenticateTwitter();
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

    public void getNews(View view) {
        Intent intent = new Intent(this, DisplayNews.class);
        startActivity(intent);
        SharedPreferences sharedPref = getSharedPreferences("preferences", 0);

        String[] handles = sharedPref.getString("handles", "").split(",");

        for (String str : handles) {
            Log.d(TAG, str);
        }
    }

    public void addHandle(View view) {
        EditText editText = (EditText) findViewById(R.id.textEdit);
        String handle = editText.getText().toString();
        editText.setText("");

        if (!isAlphaNumeric(handle)) {
            // Cannot be a valid Twitter handle
            Log.d(TAG, "Non-alphanumeric");
        } else if (!"".equals(handle)) {
            boolean userExists = false;
            try {
                userExists = new ValidUser(twitter, this).execute(handle).get();
            } catch (InterruptedException ie) {
                Log.e(TAG, "Thread interrupted", ie);
            } catch (ExecutionException ee) {
                Log.e(TAG, "Exception thrown in ValidUser", ee);
            }

            if (userExists) {
                SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("handles", sharedPref.getString("handles", "") + handle + ",");
                editor.commit();
            }
        }
    }

    private boolean isAlphaNumeric(String str) {
        String pattern = "[a-zA-z0-9_]*";
        return str.matches(pattern);
    }
}
