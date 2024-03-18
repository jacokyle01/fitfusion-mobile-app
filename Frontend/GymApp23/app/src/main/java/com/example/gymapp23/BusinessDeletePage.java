package com.example.gymapp23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymapp23.databinding.ActivityBusinessDeletePageBinding;
import com.example.gymapp23.databinding.ActivityProfilePageBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BusinessDeletePage extends AppCompatActivity {

    private Button NAVLeft, NAVRight, loadBusinessesBtn;

    LinearLayout layout;
    TextView baselineText;
    EditText searchName;
    private String TAG = MainActivity.class.getSimpleName();
    String URL = "http://coms-309-067.class.las.iastate.edu:8080/businesses";
    String userName;
    private ActivityBusinessDeletePageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);binding = ActivityBusinessDeletePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NAVLeft = findViewById(R.id.UserBanNAVBtn);
        NAVRight = findViewById(R.id.groupchatBanNAVBtn);


        loadBusinessesBtn = findViewById(R.id.searchBTN);
        layout = findViewById(R.id.container);
        baselineText = findViewById(R.id.baselineText);

        loadBusinessesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                layout.removeAllViews();
                String serverUrl = URL;
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(serverUrl, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() == 0) {
                                addCard("No Businesses");
                            } else {
                                for (int i = response.length() - 1; i >= 0; i--) {
                                    JSONObject object = response.getJSONObject(i);
                                    String name = object.getString("businessName");
                                    addCard(name);
                                }
                            }
                        } catch (JSONException e) {
                            VolleyLog.d(TAG, "Error:" + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error:" + error.getMessage());
                    }
                });
                RequestQueue requestQueue;
                requestQueue = Volley.newRequestQueue(BusinessDeletePage.this);
                requestQueue.add(jsonArrayRequest);
            }

        });

        NAVRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessDeletePage.this, GroupChatBanPage.class);
                startActivity(i);
                finish();
            }
        });
        NAVLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessDeletePage.this, AdminHomePage.class);
                startActivity(i);
                finish();
            }
        });

    }
    private void addCard(String name){
        View view = getLayoutInflater().inflate(R.layout.simple_box_2btn, null);
        TextView nameView = view.findViewById(R.id.textView);
        Button banBtn = view.findViewById(R.id.btn1);
        banBtn.setBackgroundColor(Color.RED);
        banBtn.setText("BAN");

        Button unbanBtn = view.findViewById(R.id.btn2);
        unbanBtn.setBackgroundColor(Color.GRAY);
        unbanBtn.setText("UNBAN");

        nameView.setText(name);

        banBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    String deleteUserURL = URL +"/@/"+ name + "/ban";
//                   Toast.makeText(adminHomePage.this, name, Toast.LENGTH_LONG).show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, deleteUserURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(BusinessDeletePage.this, response.toString(), Toast.LENGTH_LONG).show();
//                           nameView.setText(response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(BusinessDeletePage.this, "Error: "+ error.toString(), Toast.LENGTH_LONG).show();
//                           nameView.setText(error.toString());
                        }
                    }
                    );

                    RequestQueue requestQueue;
                    requestQueue = Volley.newRequestQueue(BusinessDeletePage.this);
                    requestQueue.add(stringRequest);
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        unbanBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    String unbanURL = URL +"/@/"+ name + "/unban";
//                   Toast.makeText(adminHomePage.this, name, Toast.LENGTH_LONG).show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, unbanURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(BusinessDeletePage.this, response.toString(), Toast.LENGTH_LONG).show();
//                           nameView.setText(response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(BusinessDeletePage.this, "Error: "+ error.toString(), Toast.LENGTH_LONG).show();
//                           nameView.setText(error.toString());
                        }
                    }
                    );

                    RequestQueue requestQueue;
                    requestQueue = Volley.newRequestQueue(BusinessDeletePage.this);
                    requestQueue.add(stringRequest);
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        layout.addView(view);
    }
}