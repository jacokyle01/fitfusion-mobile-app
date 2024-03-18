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
import com.example.gymapp23.databinding.ActivityBusinessSignupBinding;
import com.example.gymapp23.databinding.ActivityProfilePageBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class BusinessSignup extends AppCompatActivity {
    public final String POSTBUSINESSURL = "http://coms-309-067.class.las.iastate.edu:8080/businesses";
    //public final String POSTBUSINESSURL = "https://dbfca566-da98-43bd-8c06-e71b77d0240d.mock.pstmn.io/people/create"; //mason postman link

    private ActivityBusinessSignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusinessSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button toLoginBtn = findViewById(R.id.businessSignupToLoginPageBtn);
        Button createBusiness = findViewById(R.id.createBusinessAccountBtn);

        EditText businessName = findViewById(R.id.businessNameETxt);
        EditText password = findViewById(R.id.passwordBusinessETxt);
        EditText city = findViewById(R.id.businessCityETxt);
        EditText zip = findViewById(R.id.businessStateETxt);
        EditText address = findViewById(R.id.businessAddressETxt);

        //final String URL = "https://dbfca566-da98-43bd-8c06-e71b77d0240d.mock.pstmn.io/people/create?username&password";
        //final String URL = "https://dbfca566-da98-43bd-8c06-e71b77d0240d.mock.pstmn.io/people/create";

        createBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //post request
                    JSONObject json = new JSONObject();
                    json.put("businessName", businessName.getText().toString());
                    json.put("address", address.getText().toString());
                    json.put("zipcode", zip.getText().toString());
                    json.put("city", city.getText().toString());

                    //json.put("password", password.getText().toString());

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, POSTBUSINESSURL, json, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Toast.makeText(BusinessSignup.this, "Business Created", Toast.LENGTH_LONG).show();
                                Toast.makeText(BusinessSignup.this, response.toString(),
                                        Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(BusinessSignup.this, BusinessLogin.class);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(BusinessSignup.this, "error",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                    RequestQueue requestQueue;
                    requestQueue = Volley.newRequestQueue(BusinessSignup.this);
                    requestQueue.add(request);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        toLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BusinessSignup.this, BusinessLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }
}