package com.example.gymapp23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignupOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_options);

        Button createUserBtn = (Button) findViewById(R.id.userSignupBtn);
        Button createBusinessBtn = (Button) findViewById(R.id.signupBusinessBtn);
        createUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupOptions.this, UserSignup.class);
                startActivity(intent);
                finish();
            }
        });

        createBusinessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupOptions.this, BusinessSignup.class);
                startActivity(intent);
            }
        });
    }
}