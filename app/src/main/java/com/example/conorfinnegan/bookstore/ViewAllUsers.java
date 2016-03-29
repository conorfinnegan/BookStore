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

public class ViewAllUsers extends AppCompatActivity {

    String myJSON;

    JSONArray users = null;

    ArrayList<HashMap<String, String>> userList;

    ListView list;

    public static final String USER_NAME = "USER_NAME";
    public static final String LOGGED_IN_USER ="LOGGED_IN_USER";

    private ProgressDialog loading;

    public static final String DATA_URL = "http://cf000whs.netne.net/fetchAllUsers.php";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_TEAMNAME = "team_name";
    public static final String KEY_SPORT = "sport";
    public static final String KEY_AGE_GROUP = "age_group";
    public static final String KEY_LEVEL = "level";
    public static final String KEY_COUNTY = "county";
    public static final String JSON_ARRAY = "result";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        list = (ListView) findViewById(R.id.listView);
        userList = new ArrayList<HashMap<String, String>>();
        getData();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void getData() {

        loading = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);

        String url = DATA_URL;

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
                        Toast.makeText(ViewAllActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
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
                String team_name = "Team Name: " + c.getString(KEY_TEAMNAME);
                String sport = "Sport: " + c.getString(KEY_SPORT);
                String age_group = "Age Group: " + c.getString(KEY_AGE_GROUP);
                String level = "Level: " + c.getString(KEY_LEVEL);
                String county = "County: " + c.getString(KEY_COUNTY);

                HashMap<String, String> users = new HashMap<String, String>();

                users.put(KEY_USERNAME, username);
                users.put(KEY_TEAMNAME, team_name);
                users.put(KEY_SPORT, sport);
                users.put(KEY_AGE_GROUP, age_group);
                users.put(KEY_LEVEL, level);
                users.put(KEY_COUNTY, county);

                userList.add(users);

            }

            ListAdapter adapter = new SimpleAdapter(
                    ViewAllActivity.this, userList, R.layout.list_item,
                    new String[]{KEY_TEAMNAME, KEY_SPORT, KEY_AGE_GROUP, KEY_LEVEL, KEY_COUNTY},
                    new int[]{R.id.team_name, R.id.sport, R.id.age_group, R.id.level, R.id.county});

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Map<String, String> map = userList.get(position);
                    String username = map.get(KEY_USERNAME);
                    Intent intent = getIntent();

                    String logged_in_username = intent.getStringExtra(ProfileActivity.USER_NAME);

                    Intent myIntent = new Intent(ViewAllActivity.this, ViewUser.class);
                    myIntent.putExtra("username", username);
                    myIntent.putExtra("logged_in_username", logged_in_username);
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
