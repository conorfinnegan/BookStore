package com.example.conorfinnegan.bookstore;

import android.app.ProgressDialog;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewTransactionsAdmin extends AppCompatActivity {

    JSONArray users = null;

    ArrayList<HashMap<String, String>> userList;

    ListView list;

    private ProgressDialog loading;

    public static final String DATA_URL = "http://confinn93.x10host.com/cgi-bin/ViewTransactions.php";
    public static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_COST = "cost";
    public static final String JSON_ARRAY = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transactions_admin);
        list = (ListView) findViewById(R.id.listViewTransactions);
        userList = new ArrayList<HashMap<String,String>>();
        getData();
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
                        Toast.makeText(ViewTransactionsAdmin.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);

            for(int i=0;i<result.length();i++){
                JSONObject c = result.getJSONObject(i);
                String id = "Transaction ID: "+ c.getString(KEY_ID);
                String username = "Username: " + c.getString(KEY_USERNAME);
                String cost = "Cost: " + c.getString(KEY_COST);

                HashMap<String, String> users = new HashMap<String, String>();

                users.put(KEY_ID, id);
                users.put(KEY_USERNAME, username);
                users.put(KEY_COST, cost);

                userList.add(users);

            }

            ListAdapter adapter = new SimpleAdapter(
                    ViewTransactionsAdmin.this, userList, R.layout.list_item_transactions,
                    new String[]{KEY_ID, KEY_USERNAME, KEY_COST},
                    new int[]{R.id.transactionId, R.id.username, R.id.cost});

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Map<String, String> map = userList.get(position);
                    String bookId = map.get(KEY_ID);

                    Intent intent = getIntent();

                    String logged_in_username = intent.getStringExtra("logged_in_username");

                    Intent myIntent = new Intent(ViewTransactionsAdmin.this, CustomerViewBook.class);
                    myIntent.putExtra("book_id", bookId);
                    myIntent.putExtra("logged_in_user", logged_in_username);
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

}
