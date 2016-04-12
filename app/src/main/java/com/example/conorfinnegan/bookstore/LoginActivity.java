package com.example.conorfinnegan.bookstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton btnLogin;
    private Button btnLinkToRegister;
    private EditText inputUsername;
    private EditText inputPassword;

    public static final String USER_NAME = "USER_NAME";

    public static final String PASSWORD = "PASSWORD";

    private static final String LOGIN_URL = "http://confinn93.x10host.com/cgi-bin/login.php";

    public class LoginCommand {
        public void adminLogin() {
            String username = inputUsername.getText().toString().trim();
            Intent intent = new Intent(LoginActivity.this, AdminProfileActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
        public void customerLogin() {
            String username = inputUsername.getText().toString().trim();
            Intent intent = new Intent(LoginActivity.this, CustomerProfilePage.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
    }

    class ExecuteLoginCustomer implements CommandInterface {
        private LoginCommand lc;
        public ExecuteLoginCustomer (LoginCommand L) {
            lc  =  L;
        }
        public void execute( ) {
            lc.customerLogin();
        }
    }

    class ExecuteLoginAdmin implements CommandInterface {
        private LoginCommand lc;
        public ExecuteLoginAdmin (LoginCommand L) {
            lc  =  L;
        }
        public void execute( ) {
            lc.adminLogin();
        }
    }

    class CompleteLogin {
        private CommandInterface correct;
        public CompleteLogin(CommandInterface Up) {
            correct = Up; // concrete Command registers itself with the invoker
        }
        void DoLogin() { // invoker calls back concrete Command, which executes the Command on the receiver
            correct.execute() ;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUsername = (EditText) findViewById(R.id.username_login);
        inputPassword = (EditText) findViewById(R.id.password_login);
        btnLogin = (AppCompatButton) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        //Adding click listener
        btnLogin.setOnClickListener(this);

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void login(){
        String username = inputUsername.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        userLogin(username, password);
    }

    private void userLogin(final String username, final String password){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this,"Please Wait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("success")){
                    if(inputUsername.getText().toString().trim().equalsIgnoreCase("admin") && inputPassword.getText().toString().trim().equalsIgnoreCase("admin")) {
                        LoginCommand lc = new LoginCommand();
                        ExecuteLoginAdmin adminLog = new ExecuteLoginAdmin(lc);
                        CompleteLogin ts = new CompleteLogin(adminLog);
                        ts.DoLogin();
                    }
                    else if(!inputUsername.getText().toString().trim().equalsIgnoreCase("admin") && !inputPassword.getText().toString().trim().equalsIgnoreCase("admin")){
                        LoginCommand lc = new LoginCommand();
                        ExecuteLoginCustomer custLog = new ExecuteLoginCustomer(lc);
                        CompleteLogin ts = new CompleteLogin(custLog);
                        ts.DoLogin();
                    }
                }else{
                    Toast.makeText(LoginActivity.this,s,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("username",params[0]);
                data.put("password",params[1]);

                RegisterUserClass ruc = new RegisterUserClass();

                String result = ruc.sendPostRequest(LOGIN_URL,data);

                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(username, password);
    }

    @Override
    public void onClick(View v) {
        if(v == btnLogin){
            login();
        }
    }
}