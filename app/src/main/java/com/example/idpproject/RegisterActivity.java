package com.example.idpproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.sql.Connection;
import java.sql.Statement;

public class RegisterActivity extends AppCompatActivity {

    EditText etUserName, etPassword, etLicense, etFirstName, etLastName;
    Button bRegister;
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etLicense = (EditText) findViewById(R.id.etLicense);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        bRegister = (Button) findViewById(R.id.bRegister);

        connectionClass = new ConnectionClass();
        progressDialog=new ProgressDialog(this);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Doregister doregister = new Doregister();
                doregister.execute("");
            }
        });
    }

    // Start of Register
    public class Doregister extends AsyncTask<String,String,String>
    {

        final String userName = etUserName.getText().toString();
        final String password = etPassword.getText().toString();
        final String license = etLicense.getText().toString();
        final String firstName = etFirstName.getText().toString();
        final String lastName = etLastName.getText().toString();
        String z="";
        boolean isSuccess=false;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if(userName.trim().equals("")|| password.trim().equals("") ||license.trim().equals("")||firstName.trim().equals("")||lastName.trim().equals(""))
                z = "Please enter all fields....";
            else
            {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Please check your internet connection";
                    } else {

                        String query="insert into User values('"+userName+"','"+password+"','"+license+"','"+firstName+"','"+lastName+"')";

                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query);

                        z = "Welcome!! "+userName;
                        isSuccess=true;

                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions"+ex;
                }
            }
            return z;
        }
        // Start of Post
        @Override
        protected void onPostExecute(String s) {

            Toast.makeText(getBaseContext(),""+z,Toast.LENGTH_LONG).show();


            if(isSuccess) {
                Intent intent = new Intent(RegisterActivity.this,MainPage.class);
                Bundle bundle = new Bundle();
                bundle.putString("userName", userName);
                bundle.putString("licenseNumber", license);
                intent.putExtras(bundle);
                startActivity(intent);

            }
            progressDialog.hide();
        }
        // End of Post
    }
    // End of Register
}
