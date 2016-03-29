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
    private Button btnLinkToSeach;
    private Button btnLinkToViewAll;
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
        setContentView(R.layout.activity_profile);

        textView = (TextView) findViewById(R.id.textViewUserName);

        Intent intent = getIntent();

        username = intent.getStringExtra("username");

        textView.setText("Loged In as: "+username);

        btnLinkToAdd = (Button) findViewById(R.id.AddBook);
//
//        btnLinkToSeach= (Button) findViewById(R.id.SearchOpp);
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
        btnLinkToAdd.setOnClickListener(this);
//
//        btnLinkToSeach.setOnClickListener(this);
//
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
        if(v == btnLinkToAdd){
            Intent intent = new Intent(AdminProfileActivity.this,AddBookActivity.class);
            intent.putExtra(USER_NAME, username);
            startActivity(intent);
        }
//        if(v == btnLinkToSeach){
//            Intent intent = new Intent(ProfileActivity.this,SearchInterfaceActivity.class);
//            intent.putExtra("logged_in_username", username);
//            startActivity(intent);
//        }
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