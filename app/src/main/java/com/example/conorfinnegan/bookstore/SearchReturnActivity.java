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

public class SearchReturnActivity extends AppCompatActivity {

    String myJSON;

    JSONArray users = null;

    ArrayList<HashMap<String, String>> userList;

    ListView list;

    private ProgressDialog loading;

    public static final String DATA_URL = "http://confinn93.x10host.com/cgi-bin/SearchBooks.php";
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_GENRE = "genre";
    public static final String KEY_PRICE = "price";
    public static final String KEY_YEAR = "year";
    public static final String KEY_QUANTITY = "quantity";
    public static final String JSON_ARRAY = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_return);
        list = (ListView) findViewById(R.id.listViewSearchReturn);
        userList = new ArrayList<HashMap<String,String>>();
        getData();
    }
    private void getData() {

        Intent intent = getIntent();

        String searchRef = String.valueOf(intent.getStringExtra(CustomerSearch.BOOK_NAME));

        searchRef.trim();


            loading = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);

            String url = DATA_URL + "?reference=" + searchRef.trim();

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
                            Toast.makeText(SearchReturnActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {

        Intent intent = getIntent();

        String book_name = intent.getStringExtra(CustomerSearch.BOOK_NAME);

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);

            for(int i=0;i<result.length();i++){
                JSONObject c = result.getJSONObject(i);
                String id = c.getString(KEY_ID);
                String title = "Title: " + c.getString(KEY_TITLE);
                String author = "Author: " + c.getString(KEY_AUTHOR);
                String genre = "Genre: " + c.getString(KEY_GENRE);
                String price ="Price: " + c.getString(KEY_PRICE);
                String year = "Year: " + c.getString(KEY_YEAR);
                String quantity = "Quantity: " + c.getString(KEY_QUANTITY);

                HashMap<String, String> users = new HashMap<String, String>();

                users.put(KEY_ID, id);
                users.put(KEY_TITLE, title);
                users.put(KEY_AUTHOR, author);
                users.put(KEY_GENRE, genre);
                users.put(KEY_PRICE, price);
                users.put(KEY_YEAR, year);
                users.put(KEY_QUANTITY, quantity);

                userList.add(users);

            }

            ListAdapter adapter = new SimpleAdapter(
                    SearchReturnActivity.this, userList, R.layout.list_item_search_return,
                    new String[]{KEY_ID, KEY_TITLE, KEY_AUTHOR, KEY_GENRE, KEY_PRICE, KEY_YEAR, KEY_QUANTITY},
                    new int[]{R.id.bookID, R.id.title, R.id.author, R.id.genre, R.id.price, R.id.year, R.id.quantity});

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Map<String, String> map = userList.get(position);
                    String bookId = map.get(KEY_ID);

                    Intent intent = getIntent();

                    String logged_in_username = intent.getStringExtra("logged_in_username");

                    Intent myIntent = new Intent(SearchReturnActivity.this, CustomerViewBook.class);
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