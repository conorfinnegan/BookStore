package com.example.conorfinnegan.bookstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewCart extends AppCompatActivity {

    String myJSON;

    JSONArray users = null;

    ArrayList<HashMap<String, String>> userList;

    ListView list;

    public static final String USER_NAME = "USER_NAME";
    public static final String LOGGED_IN_USER ="LOGGED_IN_USERNAME";

    private ProgressDialog loading;

    public static final String DATA_URL = "http://confinn93.x10host.com/cgi-bin/ViewCart.php?username=";
    public static final String KEY_BOOKID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_TITLE = "title";
    public static final String KEY_PRICE = "price";
    public static final String JSON_ARRAY = "result";

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
                String id = c.getString(KEY_BOOKID);
                String username = c.getString(KEY_USERNAME);
                String title = c.getString(KEY_TITLE);
                String price = c.getString(KEY_PRICE);

                HashMap<String, String> users = new HashMap<String, String>();

                users.put(KEY_BOOKID, id);
                users.put(KEY_USERNAME, username);
                users.put(KEY_TITLE, title);
                users.put(KEY_PRICE, price);

                userList.add(users);

            }

            ListAdapter adapter = new SimpleAdapter(
                    ViewCart.this, userList, R.layout.list_item_cart,
                    new String[]{KEY_BOOKID, KEY_USERNAME, KEY_TITLE, KEY_PRICE},
                    new int[]{R.id.bookid, R.id.username, R.id.title, R.id.price});

//            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                    Map<String, String> map = userList.get(position);
//                    String opponent_username = map.get(KEY_OPPONENT_USERNAME);
//                    String username = map.get(KEY_USERNAME);
//                    Intent intent = getIntent();
//
//                    String logged_in_username = intent.getStringExtra(CustomerProfilePage.USER_NAME);
//
//                    Intent myIntent = new Intent(ViewCart.this, ViewBook.class);
//                    myIntent.putExtra(USER_NAME, opponent_username);
//                    myIntent.putExtra("logged_in_username", username);
//                    startActivity(myIntent);
//                }
//            });

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
}
