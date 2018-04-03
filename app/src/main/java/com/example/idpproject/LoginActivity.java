package com.example.idpproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class LoginActivity extends AppCompatActivity{

    EditText etUserName, etPassword;
    Button bLogin;
    TextView tvRegister;
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        tvRegister = (TextView) findViewById(R.id.tvRegister);

        connectionClass = new ConnectionClass();
        progressDialog=new ProgressDialog(this);

        // Change to Register Activity
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        // Click Login
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dologin dologin=new Dologin();
                dologin.execute();
            }
        });
    }


    private class Dologin extends AsyncTask<String,String,String> {

        String userName = etUserName.getText().toString();
        String password = etPassword.getText().toString();

        String z="Incorrect Password";
        boolean isSuccess=false;

        String user,pw, li;


        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            if(userName.trim().equals("")|| password.trim().equals(""))
                z = "Please enter all fields....";
            else
            {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Please check your internet connection";
                    } else {

                        String query=" select * from user where userName='"+userName+"' and password='"+password+"' ";

                        Statement stmt = con.createStatement();
                        // stmt.executeUpdate(query);

                        ResultSet rs=stmt.executeQuery(query);

                        while (rs.next())

                        {
                            user = rs.getString(1);
                            pw =rs.getString(2);
                            li =rs.getString(3);

                            if(user.equals(userName)&& pw.equals(password))
                            {
                                isSuccess=true;
                                z = "Welcome!! "+userName;

                            }
                            else {
                                isSuccess = false;
                            }
                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions"+ex;
                }
            }
            return z;        }

        @Override
        protected void onPostExecute(String s) {
            //Toast.makeText(getBaseContext(),""+z,Toast.LENGTH_LONG).show();
            if(isSuccess) {
                Intent intent = new Intent(LoginActivity.this,MainPage.class);
                Bundle bundle = new Bundle();
                bundle.putString("userName", userName);
                bundle.putString("licenseNumber", li);
                intent.putExtras(bundle);
                startActivity(intent);


            }
            progressDialog.hide();
        }

    }





}
