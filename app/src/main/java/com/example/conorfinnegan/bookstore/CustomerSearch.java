package com.example.conorfinnegan.bookstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerSearch extends AppCompatActivity implements View.OnClickListener {

    private Button btnSearch;
    private EditText editTextBookName;

    public static final String BOOK_NAME = "BOOK_NAME";

    JSONArray users = null;

    ArrayList<HashMap<String, String>> userList;

    ListView list;

    private ProgressDialog loading;

    public static final String DATA_URL = "http://confinn93.x10host.com/cgi-bin/ViewAllBooks.php";
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_GENRE = "genre";
    public static final String KEY_PRICE = "price";
    public static final String KEY_YEAR = "year";
    public static final String KEY_QUANTITY = "quantity";
    public static final String JSON_ARRAY = "result";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_search);
        list = (ListView) findViewById(R.id.listViewSearch);
        userList = new ArrayList<HashMap<String, String>>();
        getData();

        editTextBookName = (EditText) findViewById(R.id.book_name);

        btnSearch = (Button) findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(this);

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
                        Toast.makeText(CustomerSearch.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
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
                    CustomerSearch.this, userList, R.layout.list_item_customer_search,
                    new String[]{KEY_ID, KEY_TITLE, KEY_AUTHOR, KEY_GENRE, KEY_PRICE, KEY_YEAR, KEY_QUANTITY},
                    new int[]{R.id.bookID, R.id.title, R.id.author, R.id.genre, R.id.price, R.id.year, R.id.quantity});

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Map<String, String> map = userList.get(position);
                    String bookId = map.get(KEY_ID);

                    Intent intent = getIntent();

                    String username = intent.getStringExtra("username");

                    Intent myIntent = new Intent(CustomerSearch.this, ViewBook.class);
                    myIntent.putExtra("book_id", bookId);
                    myIntent.putExtra("username", username);
                    startActivity(myIntent);
                }
            });

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        if (v == btnSearch) {
            String book_name = editTextBookName.getText().toString().trim();

            String searchRef = book_name.replaceAll("\\s+", "");

            Intent myIntent = getIntent();

            String logged_in_username = myIntent.getStringExtra("username");

            Intent intent = new Intent(CustomerSearch.this, SearchReturnActivity.class);
            intent.putExtra(BOOK_NAME, searchRef);
            intent.putExtra("logged_in_username", logged_in_username);
            startActivity(intent);
        }
    }
}
