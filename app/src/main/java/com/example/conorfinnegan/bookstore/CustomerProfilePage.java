package com.example.conorfinnegan.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CustomerProfilePage extends AppCompatActivity implements View.OnClickListener {
    private TextView textView;
    private Button btnLinkToSearch;
    private Button btnLinkToViewCart;
    private Button btnLinkToLogout;

    public static final String USER_NAME = "USER_NAME";

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile_page);

        textView = (TextView) findViewById(R.id.textViewUserName);

        Intent intent = getIntent();

        username = intent.getStringExtra("username");

        textView.setText("Logged In as: "+username);

        btnLinkToSearch = (Button) findViewById(R.id.SearchBook);

        btnLinkToViewCart= (Button) findViewById(R.id.ViewCart);

        btnLinkToLogout= (Button) findViewById(R.id.Logout);

        btnLinkToSearch.setOnClickListener(this);

        btnLinkToViewCart.setOnClickListener(this);

        btnLinkToLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnLinkToSearch){
            Intent newintent = getIntent();
            finish();
            startActivity(newintent);
            Intent intent = new Intent(CustomerProfilePage.this,CustomerSearch.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
        if(v == btnLinkToViewCart){
            Intent newintent = getIntent();
            finish();
            startActivity(newintent);
            Intent intent = new Intent(CustomerProfilePage.this,ViewCart.class);
            intent.putExtra("username", username);
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