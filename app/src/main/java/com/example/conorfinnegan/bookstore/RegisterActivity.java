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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputAddress;
    private EditText inputPassword;
    private EditText inputUsername;
    private EditText inputPaymentMethod;
    private EditText inputName;


    private static final String REGISTER_URL = "http://confinn93.x10host.com/cgi-bin/BSregister.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputPassword = (EditText) findViewById(R.id.password);
        inputUsername= (EditText) findViewById(R.id.username);
        inputAddress= (EditText) findViewById(R.id.address);
        inputPaymentMethod= (EditText) findViewById(R.id.payment_method);
        inputName= (EditText) findViewById(R.id.name);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);


//        LevelDropdown = (Spinner) findViewById(R.id.LevelSpinner);
//        String[] levels = new String[]{"Division 1", "Division 2", "Division 3", "Division 4", "Division 5", "Division 6"};
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, levels);
//        LevelDropdown.setAdapter(adapter2);

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnRegister){
            try {
                registerUser();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerUser() throws IOException {
        String password = inputPassword.getText().toString().trim();
        String username = inputUsername.getText().toString().trim();
        String name = inputName.getText().toString().trim();
        String address = inputAddress.getText().toString().trim();
        String paymentMethod = inputPaymentMethod.getText().toString().trim();


        register(username, password, name, address, paymentMethod);
    }

    private void register(String username, String password, String name, String address, String paymentMethod) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegisterActivity.this, "Please Wait",null, true, true);
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
                data.put("username",params[0]);
                data.put("password",params[1]);
                data.put("name",params[2]);
                data.put("address",params[3]);
                data.put("payment_method",params[4]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(username, password, name, address, paymentMethod);
    }

}


