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
    private Button btnLinkToDelete;
    private Button btnLinkToViewCart;
    private Button btnLinkToViewShortList;
    private Button btnLinkToLogout;
    private Button btnLinkToSuggestedOpponents;
    private Button btnLinkToViewRequests;
    private Button btnLinkToViewFixtures;

    public static final String USER_NAME = "USER_NAME";

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile_page);

        textView = (TextView) findViewById(R.id.textViewUserName);

        Intent intent = getIntent();

        username = intent.getStringExtra("username");

        textView.setText("Loged In as: "+username);

        btnLinkToSearch = (Button) findViewById(R.id.SearchBook);

        btnLinkToViewCart= (Button) findViewById(R.id.ViewCart);
//
//        btnLinkToViewAll = (Button) findViewById(R.id.ViewAll);
//
//        btnLinkToViewShortList= (Button) findViewById(R.id.ViewShortList);
//
//        btnLinkToSuggestedOpponents = (Button) findViewById(R.id.SuggestedOpponents);
//
//        btnLinkToViewRequests = (Button) findViewById(R.id.IncomingRequests);
//
        btnLinkToLogout= (Button) findViewById(R.id.Logout);
//
//        btnLinkToViewFixtures = (Button) findViewById(R.id.ViewFixtures);
//
        btnLinkToSearch.setOnClickListener(this);

        btnLinkToViewCart.setOnClickListener(this);

//        btnLinkToViewAll.setOnClickListener(this);
//
//        btnLinkToViewShortList.setOnClickListener(this);
//
//        btnLinkToSuggestedOpponents.setOnClickListener(this);
//
//        btnLinkToViewRequests.setOnClickListener(this);
//
        btnLinkToLogout.setOnClickListener(this);
//
//        btnLinkToViewFixtures.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnLinkToSearch){
            Intent intent = new Intent(CustomerProfilePage.this,CustomerSearch.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
        if(v == btnLinkToViewCart){
            Intent intent = new Intent(CustomerProfilePage.this,ViewAllBooks.class);
            startActivity(intent);
        }
//        if(v == btnLinkToViewAll){
//            Intent intent = new Intent(ProfileActivity.this,ViewAllActivity.class);
//            intent.putExtra(USER_NAME, username);
//            startActivity(intent);
//        }
//        if(v == btnLinkToViewShortList){
//            Intent intent = new Intent(ProfileActivity.this,ViewShortlist.class);
//            intent.putExtra("username", username);
//            startActivity(intent);
//        }
//
//        if(v == btnLinkToSuggestedOpponents){
//            Intent intent = new Intent(ProfileActivity.this,SuggestedOpponents.class);
//            intent.putExtra("username", username);
//            startActivity(intent);
//        }
//
//        if(v == btnLinkToViewRequests){
//            Intent intent = new Intent(ProfileActivity.this,ViewRequests.class);
//            intent.putExtra("username", username);
//            startActivity(intent);
//        }

        if(v == btnLinkToLogout){
            this.finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

//        if(v == btnLinkToViewFixtures){
//            Intent intent = new Intent(ProfileActivity.this,ViewFixtures.class);
//            intent.putExtra("username", username);
//            startActivity(intent);
//        }
    }
}