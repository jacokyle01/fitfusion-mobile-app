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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PostEvent extends AppCompatActivity {

    public static final String POSTBUSINESSEVENTURL = "http://coms-309-067.class.las.iastate.edu:8080/businesses/@/";

    private Button postEvent, backBtn;
    private EditText title, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);

        postEvent = findViewById(R.id.postEventBtn);
        backBtn = findViewById(R.id.postEventToBPBtn);

        title = findViewById(R.id.title);
        message = findViewById(R.id.message);

        postEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String URL = POSTBUSINESSEVENTURL + getIntent().getStringExtra("businessName") + "/posts";

                    JSONObject jsonPost = new JSONObject();
                    jsonPost.put("title", title.getText().toString());
                    jsonPost.put("message", message.getText().toString());
                    jsonPost.put("likes", 0);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonPost, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Toast.makeText(PostEvent.this, "Message Posted!", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(PostEvent.this, BusinessProfilePage.class);
                                intent.putExtra("businessName", getIntent().getStringExtra("businessName"));
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(PostEvent.this, "Error Posting your Event",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                    RequestQueue requestQueue;
                    requestQueue = Volley.newRequestQueue(PostEvent.this);
                    requestQueue.add(request);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostEvent.this, BusinessProfilePage.class);
                i.putExtra("businessName", getIntent().getStringExtra("businessName"));
                startActivity(i);
                finish();
            }
        });
    }
}