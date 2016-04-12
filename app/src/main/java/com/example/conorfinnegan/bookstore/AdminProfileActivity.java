package com.example.conorfinnegan.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AdminProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textView;
    private Button btnLinkToAdd;
    private Button btnLinkToViewTransactions;
    private Button btnLinkToViewAll;
    private Button btnLinkToLogout;

    public static final String USER_NAME = "USER_NAME";

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textView = (TextView) findViewById(R.id.textViewUserName);

        Intent intent = getIntent();

        username = intent.getStringExtra("username");

        textView.setText("Loged In as: "+username);

        btnLinkToAdd = (Button) findViewById(R.id.AddBook);

        btnLinkToViewAll= (Button) findViewById(R.id.ViewAllBooks);

        btnLinkToViewTransactions = (Button) findViewById(R.id.ViewTransactions);

        btnLinkToLogout= (Button) findViewById(R.id.Logout);

        btnLinkToAdd.setOnClickListener(this);

        btnLinkToViewAll.setOnClickListener(this);

        btnLinkToViewTransactions.setOnClickListener(this);

        btnLinkToLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnLinkToAdd){
            Intent intent = new Intent(AdminProfileActivity.this,AddBookActivity.class);
            intent.putExtra(USER_NAME, username);
            startActivity(intent);
        }
        if(v == btnLinkToViewAll){
            Intent intent = new Intent(AdminProfileActivity.this,ViewAllBooks.class);
            startActivity(intent);
        }
        if(v == btnLinkToViewTransactions){
            Intent intent = new Intent(AdminProfileActivity.this,ViewTransactionsAdmin.class);
            intent.putExtra(USER_NAME, username);
            startActivity(intent);
        }

        if(v == btnLinkToLogout){
            this.finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}