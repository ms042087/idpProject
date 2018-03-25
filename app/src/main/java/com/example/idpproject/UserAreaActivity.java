package com.example.idpproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserAreaActivity extends AppCompatActivity {
    EditText tvWelcomeMsg, etUserName;
    Button bNext;
    TextView tvUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        //create obj
        bNext = (Button) findViewById(R.id.bNext);

        // Change to Main Activiity
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Next = new Intent(UserAreaActivity.this,MainPage.class);
                startActivity(Next);
            }
        });

    }
}
