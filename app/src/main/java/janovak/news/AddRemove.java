package janovak.news;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import twitter4j.Twitter;

public class AddRemove extends ActionBarActivity {
    private static final String TAG = "AddRemove";
    private Twitter twitter;
    ArrayList<String> handles;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove);
        twitter = OAuth.getInstance().authenticateTwitter();
        ListView removeList = (ListView) findViewById(R.id.removeList);

        handles = new ArrayList<String>();
        SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String[] handlesArray = sharedPref.getString("handles", "").split(",");
        Arrays.sort(handlesArray);
        for (String str : handlesArray) {
            handles.add(str);
        }

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, handles);

        removeList.setAdapter(adapter);

        removeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                String handleList = sharedPref.getString("handles", "");
                editor.putString("handles",  handleList.replace(handles.get(position) + ",", ""));
                editor.commit();
                handles.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_remove, menu);
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

    public void addHandle(View view) {
        EditText editText = (EditText) findViewById(R.id.specifyHandle);
        String handle = editText.getText().toString();

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
                editText.setText("");
                SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("handles", sharedPref.getString("handles", "") + handle + ",");
                editor.commit();
                handles.add(handle);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private boolean isAlphaNumeric(String str) {
        String pattern = "[a-zA-z0-9_]*";
        return str.matches(pattern);
    }
}
