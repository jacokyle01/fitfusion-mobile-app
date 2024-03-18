package com.example.gymapp23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.gymapp23.databinding.ActivityHomePageBinding;
import com.example.gymapp23.databinding.ActivityLoginPageBinding;
import com.example.gymapp23.databinding.ActivityProfilePageBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginPage extends AppCompatActivity {



    //public static final String URL_STRING_REQ = "https://dbfca566-da98-43bd-8c06-e71b77d0240d.mock.pstmn.io/username";
    //public static final String URL_STRING_REQ = "http://coms-309-067.class.las.iastate.edu:8080/people"; //kyle endpoint
    public static final String URL = "http://coms-309-067.class.las.iastate.edu:8080/people/@/test2"; //kyle endpoint
    public final String GETPERSONURL = "http://coms-309-067.class.las.iastate.edu:8080/people/@/"; //kyle endpoint
    //public static final String URL = "https://dbfca566-da98-43bd-8c06-e71b77d0240d.mock.pstmn.io/jsonrequest"; //postman test
    //returns { "username" : mjinman, "password" : "password!" }
    public static final String USERNAME = "username";

    private String username;

    private EditText usernameETX;
    private EditText passwordETX;

    private ActivityLoginPageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        usernameETX = findViewById(R.id.username);
        passwordETX = findViewById(R.id.passwrd);
        TextView forgotPassword = findViewById(R.id.forgotpassword);
        Button loginBtn = (Button) findViewById(R.id.loginbtn);
        Button homeBtn = (Button) findViewById(R.id.homebtn);
        Button goToSignupBtn = (Button) findViewById(R.id.signupbtn);
        Button goToBusinessLoginBtn = findViewById(R.id.businessLoginBtn);
        String URL = GETPERSONURL;
        //admin and admin
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAdmin();
                int clickedBtn = v.getId();
                forgotPassword.setText( "" + v.toString());



                        String URL = GETPERSONURL + usernameETX.getText().toString();
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        forgotPassword.setText("Recieved from backend: " + response.toString());

                                            try {
                                                if (usernameETX.getText().toString().equals(response.getString("username")) &&
                                                        passwordETX.getText().toString().equals(response.getString("password"))) {
                                                    if (response.getBoolean("banned")) {
                                                        forgotPassword.setText("You are Banned!");
                                                    }
                                                    else {
                                                        Intent intent = new Intent(LoginPage.this, HomePage.class);
                                                        intent.putExtra(USERNAME, response.getString("username"));
                                                        startActivity(intent);
                                                        finish();
                                                    }

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
                        requestQueue = Volley.newRequestQueue(LoginPage.this);
                        requestQueue.add(jsonObjectRequest);

            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginPage.this, HomePage.class);
                in.putExtra("username", "test");
                startActivity(in);
                finish();
            }
        });
        goToSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginPage.this, SignupOptions.class);
                startActivity(in);
                finish();
            }
        });
        goToBusinessLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginPage.this, BusinessLogin.class);
                startActivity(in);
                finish();
            }
        });




    }

    private void checkAdmin() {
        if (usernameETX.getText().toString().equals("admin") && passwordETX.getText().toString().equals("admin")) {
            Intent i = new Intent(LoginPage.this, AdminHomePage.class);
            i.putExtra("username", usernameETX.getText().toString());
            startActivity(i);
            finish();
        }
    }
}