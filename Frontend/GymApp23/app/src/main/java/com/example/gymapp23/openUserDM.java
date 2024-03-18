package com.example.gymapp23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class openUserDM extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_user_dm);


        EditText otherUserETxt = findViewById(R.id.otherUserETxt);
        Button openDmBtn = findViewById(R.id.openDMBtn);

        openDmBtn.setOnClickListener(new View.OnClickListener() {
            /**
             *
             * @param view
             */
            @Override
            public void onClick(View view) {
                //if username is in the system open DM with them @todo
                String otherUsername = otherUserETxt.getText().toString();
                Intent intent = new Intent(openUserDM.this, DirectMessage.class);
                intent.putExtra("username", getIntent().getStringExtra("username"));
                intent.putExtra("other_username", otherUsername);
                startActivity(intent);
                finish();
            }
        });
    }
}