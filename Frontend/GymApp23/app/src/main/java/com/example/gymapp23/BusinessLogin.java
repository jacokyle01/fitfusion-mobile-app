package com.example.gymapp23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymapp23.databinding.ActivityBusinessLoginBinding;
import com.example.gymapp23.databinding.ActivityProfilePageBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class BusinessLogin extends AppCompatActivity {
    //private String URL = "";
    public final String GETBUSINESSURL = "http://coms-309-067.class.las.iastate.edu:8080/businesses/@/"; //kyle endpoint

    private ActivityBusinessLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusinessLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EditText businessNameETxt = findViewById(R.id.businessNameLogin);
        EditText password = findViewById(R.id.BusinessPassword);
        Button loginBtn = findViewById(R.id.businessLogin_Btn);
        TextView forgotPassword = findViewById(R.id.forgotpasswordBusiness);
        Button backToLogin = findViewById(R.id.backToLogin);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedBtn = v.getId();
                forgotPassword.setText( "" + v.toString());



                String URL = GETBUSINESSURL + businessNameETxt.getText().toString();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                forgotPassword.setText("Recieved from backend: " + response.toString());

                                try {
                                    if (response.getBoolean("banned")) {
                                        forgotPassword.setText("Your Business is banned");
                                    }
                                    else if (businessNameETxt.getText().toString().equals(response.getString("businessName")) /*&&
                                            password.getText().toString().equals(response.getString("password"))*/) {
                                        Intent intent = new Intent(BusinessLogin.this, BusinessProfilePage.class);
                                        intent.putExtra("businessName", response.getString("businessName"));
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        forgotPassword.setText("Username or Password incorrect");
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                forgotPassword.setText("Error: " + error.getMessage());
                            }
                        });


                RequestQueue requestQueue;
                requestQueue = Volley.newRequestQueue(BusinessLogin.this);
                requestQueue.add(jsonObjectRequest);
            }

        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusinessLogin.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        });

    }
}