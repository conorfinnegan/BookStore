package com.example.conorfinnegan.bookstore;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
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

public class ViewBook extends AppCompatActivity implements View.OnClickListener {

    String myJSON;

    JSONArray users = null;

    ArrayList<HashMap<String, String>> userList;

    ListView list;

    private EditText editTextId;
    private EditText editTextTitle;
    private EditText editTextAuthor;
    private EditText editTextGenre;
    private EditText editTextPrice;
    private EditText editTextYear;
    private EditText editTextQuantity;
    private EditText editTextInvisible;

    private RatingBar ratingBar;
    private EditText editTextComments;

    private static final String DELETE_URL = "http://confinn93.x10host.com/cgi-bin/DeleteBook.php?id=";
    private static final String UPDATE_URL = "http://confinn93.x10host.com/cgi-bin/UpdateBook.php?id=";
    private static final String REVIEW_URL = "http://confinn93.x10host.com/cgi-bin/LeaveReview.php";
    private static final String FETCH_REVIEW_URL = "http://confinn93.x10host.com/cgi-bin/FetchReview.php?book_id=";

    private Button deleteButton;
    private Button reviewButton;
    private Button updateButton;

    private String starRating= null;

    private ProgressDialog loading;
    private ProgressDialog loading2;

    public static final String DATA_URL = "http://confinn93.x10host.com/cgi-bin/ViewBook.php?id=";
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_GENRE = "genre";
    public static final String KEY_PRICE = "price";
    public static final String KEY_YEAR = "year";
    public static final String KEY_QUANTITY = "quantity";
    public static final String JSON_ARRAY = "result";

    public static final String REVIEW_ID = "review_id";
    public static final String BOOK_ID = "book_id";
    public static final String USERNAME = "username";
    public static final String RATING = "rating";
    public static final String COMMENTS = "comments";
    public static final String JSON_ARRAY_REVIEW = "review_result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);
        list = (ListView) findViewById(R.id.listViewReviews);
        userList = new ArrayList<HashMap<String, String>>();

        editTextId = (EditText) findViewById(R.id.textViewId);
        editTextTitle = (EditText) findViewById(R.id.textViewTitle);
        editTextAuthor = (EditText) findViewById(R.id.textViewAuthor);
        editTextGenre = (EditText) findViewById(R.id.textViewGenre);
        editTextPrice = (EditText) findViewById(R.id.textViewPrice);
        editTextYear = (EditText) findViewById(R.id.textViewYear);
        editTextQuantity = (EditText) findViewById(R.id.textViewQuantity);
        editTextInvisible = (EditText) findViewById(R.id.textViewInvisible);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        editTextComments = (EditText) findViewById(R.id.textViewComments);

        deleteButton = (Button) findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(this);

        updateButton = (Button) findViewById(R.id.EditBook);

        updateButton.setOnClickListener(this);

        reviewButton = (Button) findViewById(R.id.reviewButton);

        reviewButton.setOnClickListener(this);

        editTextQuantity.addTextChangedListener(quantityWatcher);

        getData();
        getReview();
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

    private void getReview() {

        loading2 = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);

        Intent intent = getIntent();

        String book_id = intent.getStringExtra("book_id");

        String url = FETCH_REVIEW_URL+book_id.trim();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response2) {
                loading2.dismiss();
                showJSONreview(response2);
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

    private void showJSONreview(String response2) {
        try {
            JSONObject jsonObject = new JSONObject(response2);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY_REVIEW);

            for (int i = 0; i < result.length(); i++) {
                JSONObject c = result.getJSONObject(i);
                String id = c.getString(REVIEW_ID);
                String book_id = c.getString(BOOK_ID);
                String username = c.getString(USERNAME);
                String rating = c.getString(RATING);
                String comments = c.getString(COMMENTS);


                HashMap<String, String> users = new HashMap<String, String>();

                users.put(USERNAME, username);
                users.put(RATING, rating);
                users.put(COMMENTS, comments);

                System.out.println("Thease are the reviews: " + users);

                userList.add(users);

            }

            ListAdapter adapter = new SimpleAdapter(
                    ViewBook.this, userList, R.layout.list_item_reviews,
                    new String[]{USERNAME, RATING, COMMENTS},
                    new int[]{R.id.username, R.id.rating, R.id.comments});


            list.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == deleteButton) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to delete this book?")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialogInterface, int i) {
                            deleteBook();
                        }
                    })
                    .show();
        }
        if (v == updateButton) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to save changes to this book?")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialogInterface, int i) {
                            updateBook();
                        }
                    })
                    .show();
        }
        if (v == reviewButton) {
            ratingBar = (RatingBar) findViewById(R.id.ratingBar);

            Intent intent = getIntent();

            String book_id = intent.getStringExtra("book_id");
            String username = intent.getStringExtra("username");

            starRating = String.valueOf(ratingBar.getRating());


            System.out.println("This is the star rating: " + book_id);

            String comments = editTextComments.getText().toString().trim();

            System.out.println("This is the comment rating: " + username);


            review(book_id, username, comments, starRating);
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


    private void review(String book_id, String username, String comments, String starRating) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewBook.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("book_id", params[0]);
                data.put("username", params[1]);
                data.put("comments", params[2]);
                data.put("rating", params[3]);

                String result = ruc.sendPostRequest(REVIEW_URL, data);

                return result;
            }
        }

        RegisterUser ru1 = new RegisterUser();
        ru1.execute(book_id, username, comments, starRating);
    }

}