package com.example.conorfinnegan.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CustomerSearch extends AppCompatActivity implements View.OnClickListener {

    private Button btnSearch;
    private EditText editTextBookName;

    public static final String BOOK_NAME = "BOOK_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_search);


        editTextBookName = (EditText) findViewById(R.id.book_name);

        btnSearch = (Button) findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(this);

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
