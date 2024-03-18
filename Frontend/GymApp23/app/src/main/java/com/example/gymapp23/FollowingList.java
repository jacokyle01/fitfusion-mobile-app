package com.example.gymapp23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymapp23.databinding.ActivityFollowingListBinding;
import com.example.gymapp23.databinding.ActivityProfilePageBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FollowingList extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    LinearLayout layout;

    private ActivityFollowingListBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFollowingListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Button profileBtn = findViewById(R.id.profileBtn);
        layout = findViewById(R.id.container);
        Intent intent = getIntent();
        String userName = intent.getStringExtra("username");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://coms-309-067.class.las.iastate.edu:8080/people/@/" + userName + "/friends", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if (response.length() == 0) {
                        addCard("No People");
                    } else {
                        for (int i = response.length() - 1; i >= 0; i--) {
                            JSONObject object = response.getJSONObject(i);
                            String name = object.getString("username");
                            addCard(name);
                        }
                    }
                } catch (JSONException e) {
                    addCard("Error:" + e.getMessage());
//                    VolleyLog.d(TAG, "Error:" + e.getMessage());
                }
            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error:" + error.getMessage());
            }
        });
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(FollowingList.this);
        requestQueue.add(jsonArrayRequest);

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(FollowingList.this, ProfilePage.class);
                intent2.putExtra("username", userName);
                startActivity(intent2);
                finish();
            }
        });
    }

    private void addCard(String name) {
        View view = getLayoutInflater().inflate(R.layout.simple_text_box, null);
        TextView nameView = view.findViewById(R.id.textView);

        nameView.setText(name);
        layout.addView(view);
    }
}