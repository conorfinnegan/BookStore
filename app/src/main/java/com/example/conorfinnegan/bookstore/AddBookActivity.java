package com.example.conorfinnegan.bookstore;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

public class AddBookActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAddBook;
    private Button btnLinkToLogin;
    private EditText inputTitle;
    private EditText inputAuthor;
    private EditText inputGenre;
    private EditText inputPrice;
    private EditText inputYear;
    private EditText inputQuantity;

    private static final String REGISTER_URL = "http://confinn93.x10host.com/cgi-bin/AddBook.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputTitle = (EditText) findViewById(R.id.title);
        inputAuthor = (EditText) findViewById(R.id.author);
        inputGenre = (EditText) findViewById(R.id.genre);
        inputPrice = (EditText) findViewById(R.id.price);
        inputYear = (EditText) findViewById(R.id.year);
        inputQuantity = (EditText) findViewById(R.id.quantity);
        btnAddBook = (Button) findViewById(R.id.btnAddBook);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToProfile);


//        LevelDropdown = (Spinner) findViewById(R.id.LevelSpinner);
//        String[] levels = new String[]{"Division 1", "Division 2", "Division 3", "Division 4", "Division 5", "Division 6"};
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, levels);
//        LevelDropdown.setAdapter(adapter2);

        btnAddBook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnAddBook) {
            try {
                registerUser();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerUser() throws IOException {
        String title = inputTitle.getText().toString().trim();
        String author = inputAuthor.getText().toString().trim();
        String genre = inputGenre.getText().toString().trim();
        String price = inputPrice.getText().toString().trim();
        String year = inputYear.getText().toString().trim();
        String quantity = inputQuantity.getText().toString().trim();
        String reference = title.replaceAll("\\s+","");


        register(title, author, genre, price, year, quantity, reference);
    }

    private void register(String title, String author, String genre, String price, String year, String quantity, String reference) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddBookActivity.this, "Please Wait", null, true, true);
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
                data.put("title", params[0]);
                data.put("author", params[1]);
                data.put("genre", params[2]);
                data.put("price", params[3]);
                data.put("year", params[4]);
                data.put("quantity", params[5]);
                data.put("reference", params[6]);

                String result = ruc.sendPostRequest(REGISTER_URL, data);

                return result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(title, author, genre, price, year, quantity, reference);
    }
}