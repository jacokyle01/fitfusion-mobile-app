package com.example.gymapp23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserPosts extends AppCompatActivity {

    public static final String USERPOSTURL = "http://coms-309-067.class.las.iastate.edu:8080/posts/";

    private Button UserPost, backBtn;
    private EditText title, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posts);

        UserPost = findViewById(R.id.UserPostBtn);
        backBtn = findViewById(R.id.UserPostToUserProfileBtn);

        title = findViewById(R.id.title);
        message = findViewById(R.id.message);

        UserPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String URL = USERPOSTURL + getIntent().getStringExtra("username");

                    //Toast.makeText(UserPosts.this, URL, Toast.LENGTH_SHORT).show();

                    JSONObject jsonPost = new JSONObject();
                    jsonPost.put("title", title.getText().toString());
                    jsonPost.put("message", message.getText().toString());
                    //jsonPost.put("likes", 0);


                    //http://coms-309-067.class.las.iastate.edu:8080/posts/test


                    RequestQueue requestQueue;
                    requestQueue = Volley.newRequestQueue(UserPosts.this);
                    StringRequestWithJsonBody postRequest = new StringRequestWithJsonBody(requestQueue);

                    postRequest.makePostRequestWithJson(jsonPost.toString(), URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Handle the response
                            System.out.println("Response: " + response);
                            Toast.makeText(UserPosts.this, "Message Posted!", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(UserPosts.this, ProfilePage.class);
                            intent.putExtra("username", getIntent().getStringExtra("username"));
                            startActivity(intent);
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle the error
                            System.out.println("Error: " + error.getMessage());
                        }
                    });
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserPosts.this, ProfilePage.class);
                i.putExtra("username", getIntent().getStringExtra("username"));
                startActivity(i);
                finish();
            }
        });
    }

}