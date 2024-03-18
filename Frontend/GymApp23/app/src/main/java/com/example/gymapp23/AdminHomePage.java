package com.example.gymapp23;

import androidx.appcompat.app.AlertDialog;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminHomePage extends AppCompatActivity {

    AlertDialog dialog;
    Button search, NAVRight;
    LinearLayout layout;
    TextView baselineText;
    EditText searchName;
    private String TAG = MainActivity.class.getSimpleName();
    String URL = "http://coms-309-067.class.las.iastate.edu:8080/people";
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        Intent intent = getIntent();
        userName = intent.getStringExtra("username");

        search = findViewById(R.id.searchBTN);
        searchName = findViewById(R.id.searchName);
        layout = findViewById(R.id.container);
        baselineText = findViewById(R.id.baselineText);
        NAVRight = findViewById(R.id.businessDeleteNAVBtn);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                layout.removeAllViews();
                String serverUrl = URL + "/@/" + userName + "/search/" + (searchName.getText().toString()) ;
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(serverUrl, new Response.Listener<JSONArray>() {
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
                    requestQueue = Volley.newRequestQueue(AdminHomePage.this);
                    requestQueue.add(jsonArrayRequest);
                }

            });

        NAVRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminHomePage.this, BusinessDeletePage.class);
                startActivity(i);
                finish();
            }
        });
//    private void buildDialog(String user){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
////        View view = getLayoutInflater().inflate(R.layout.dialog, null);
//
////        EditText name = view.findViewById(R.id.nameEdit);
////        builder.setView(view);
////        builder.setTitle("Enter name");
//        addCard(user);
////                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialogInterface, int i) {
////                        addCard(user);//name.getText().toString());
////                    }
////                })
////                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialogInterface, int i) {
////
////                    }
////                });
//        dialog = builder.create();
//    }

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
                            Toast.makeText(AdminHomePage.this, response.toString(), Toast.LENGTH_LONG).show();
//                           nameView.setText(response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AdminHomePage.this, "Error: "+ error.toString(), Toast.LENGTH_LONG).show();
//                           nameView.setText(error.toString());
                        }
                    }
                    );

                    RequestQueue requestQueue;
                    requestQueue = Volley.newRequestQueue(AdminHomePage.this);
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
                            Toast.makeText(AdminHomePage.this, response.toString(), Toast.LENGTH_LONG).show();
//                           nameView.setText(response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AdminHomePage.this, "Error: "+ error.toString(), Toast.LENGTH_LONG).show();
//                           nameView.setText(error.toString());
                        }
                    }
                    );

                    RequestQueue requestQueue;
                    requestQueue = Volley.newRequestQueue(AdminHomePage.this);
                    requestQueue.add(stringRequest);
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        layout.addView(view);
    }
}