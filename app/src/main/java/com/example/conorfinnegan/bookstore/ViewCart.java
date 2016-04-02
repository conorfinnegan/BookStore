package com.example.conorfinnegan.bookstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewCart extends AppCompatActivity {

    String myJSON;

    JSONArray users = null;

    ArrayList<HashMap<String, String>> userList;

    ListView list;

    public static final String USER_NAME = "USER_NAME";
    public static final String LOGGED_IN_USER ="LOGGED_IN_USERNAME";

    private ProgressDialog loading;

    public static final String DATA_URL = "http://cf000whs.netne.net/viewShortlist.php?username=";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_OPPONENT_USERNAME = "opponent_username";
    public static final String KEY_DATE_ADDED = "date_added";
    public static final String JSON_ARRAY = "result";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        list = (ListView) findViewById(R.id.listViewCart);
        userList = new ArrayList<HashMap<String, String>>();
        getData();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void getData() {

        loading = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);

        Intent intent = getIntent();

        String username = intent.getStringExtra("username");

        String url = DATA_URL+username.trim();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ViewCart.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject c = result.getJSONObject(i);
                String username = c.getString(KEY_USERNAME);
                String opponent_username = c.getString(KEY_OPPONENT_USERNAME);
                String date_added = "Date Added: " + c.getString(KEY_DATE_ADDED);

                HashMap<String, String> users = new HashMap<String, String>();

                users.put(KEY_USERNAME, username);
                users.put(KEY_OPPONENT_USERNAME, opponent_username);
                users.put(KEY_DATE_ADDED, date_added);

                userList.add(users);

            }

            ListAdapter adapter = new SimpleAdapter(
                    ViewCart.this, userList, R.layout.list_item_shortlist,
                    new String[]{KEY_USERNAME, KEY_OPPONENT_USERNAME, KEY_DATE_ADDED},
                    new int[]{R.id.username, R.id.opponent_username, R.id.date_added});

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Map<String, String> map = userList.get(position);
                    String opponent_username = map.get(KEY_OPPONENT_USERNAME);
                    String username = map.get(KEY_USERNAME);
                    Intent intent = getIntent();

                    String logged_in_username = intent.getStringExtra(CustomerProfilePage.USER_NAME);

                    Intent myIntent = new Intent(ViewCart.this, ViewUserShortlist.class);
                    myIntent.putExtra(USER_NAME, opponent_username);
                    myIntent.putExtra("logged_in_username", username);
                    startActivity(myIntent);
                }
            });

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
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
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ViewAll Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.conorfinnegan.finalyearproject20/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ViewAll Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.conorfinnegan.finalyearproject20/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
