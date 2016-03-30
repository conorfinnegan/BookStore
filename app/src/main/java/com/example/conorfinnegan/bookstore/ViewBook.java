package com.example.conorfinnegan.bookstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ViewBook extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextId;
    private EditText editTextTitle;
    private EditText editTextAuthor;
    private EditText editTextGenre;
    private EditText editTextPrice;
    private EditText editTextYear;
    private EditText editTextQuantity;
    private EditText editTextInvisible;

    private static final String DELETE_URL = "http://confinn93.x10host.com/cgi-bin/DeleteBook.php?id=";
    private static final String UPDATE_URL = "http://confinn93.x10host.com/cgi-bin/UpdateBook.php?id=";


    private Button deleteButton;
    private Button updateButton;

    private ProgressDialog loading;

    public static final String DATA_URL = "http://confinn93.x10host.com/cgi-bin/ViewBook.php?id=";
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
        setContentView(R.layout.activity_view_book);

        editTextId = (EditText) findViewById(R.id.textViewId);
        editTextTitle = (EditText) findViewById(R.id.textViewTitle);
        editTextAuthor = (EditText) findViewById(R.id.textViewAuthor);
        editTextGenre = (EditText) findViewById(R.id.textViewGenre);
        editTextPrice = (EditText) findViewById(R.id.textViewPrice);
        editTextYear = (EditText) findViewById(R.id.textViewYear);
        editTextQuantity = (EditText) findViewById(R.id.textViewQuantity);
        editTextInvisible = (EditText) findViewById(R.id.textViewInvisible);

        deleteButton = (Button) findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(this);

        updateButton = (Button) findViewById(R.id.EditBook);

        updateButton.setOnClickListener(this);

        editTextQuantity.addTextChangedListener(quantityWatcher);

        getData();
    }

    private final TextWatcher quantityWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            editTextInvisible.setText(editTextQuantity.getText());
            editTextInvisible.setVisibility(View.GONE);
        }
    };


    private void getData() {

        Intent intent = getIntent();

        String bookId = intent.getStringExtra("book_id");

        bookId.trim();
        if (bookId.equals("")) {
            Toast.makeText(this, "No ID Passed", Toast.LENGTH_LONG).show();
            return;
        }
        loading = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);

        String url = DATA_URL + bookId.trim();

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
                        Toast.makeText(ViewBook.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {
        String id = "";
        String title = "";
        String author = "";
        String genre = "";
        String price = "";
        String year = "";
        String quantity = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
            JSONObject userData = result.getJSONObject(0);
            id = userData.getString(KEY_ID);
            title = userData.getString(KEY_TITLE);
            author = userData.getString(KEY_AUTHOR);
            genre = userData.getString(KEY_GENRE);
            price = userData.getString(KEY_PRICE);
            year = userData.getString(KEY_YEAR);
            quantity = userData.getString(KEY_QUANTITY);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        editTextId.setText(id);
        editTextTitle.setText(title);
        editTextAuthor.setText(author);
        editTextGenre.setText(genre);
        editTextPrice.setText(price);
        editTextYear.setText(year);
        editTextQuantity.setText(quantity);
    }

    @Override
    public void onClick(View v) {
        if (v == deleteButton) {
            deleteBook();
        }
        if (v == updateButton) {
            updateBook();
        }
    }

    private void deleteBook() {

        Intent intent = getIntent();

        String bookId = intent.getStringExtra("book_id");

        bookId.trim();

        loading = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);

        String url = DELETE_URL + bookId.trim();

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
                        Toast.makeText(ViewBook.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updateBook() {

        String id = editTextId.getText().toString().trim();
        String title = editTextTitle.getText().toString().trim();
        String author = editTextAuthor.getText().toString().trim();
        String genre = editTextGenre.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();
        String year = editTextYear.getText().toString().trim();
        String quantity = editTextInvisible.getText().toString().trim();

        update(id, title, author, genre, price, year, quantity);
    }



    private void update(String id, String title, String author, String genre, String price, String year, String quantity) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewBook.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("id",params[0]);
                data.put("title",params[1]);
                data.put("author",params[2]);
                data.put("genre",params[3]);
                data.put("price",params[4]);
                data.put("year",params[5]);
                data.put("quantity", params[6]);

                Intent intent = getIntent();

                String bookId = intent.getStringExtra("book_id");

                bookId.trim();

                String url = UPDATE_URL+bookId.trim();

                String result = ruc.sendPostRequest(url,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(id, title, author, genre, price, year, quantity);
    }

}