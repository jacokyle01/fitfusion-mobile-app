package com.example.gymapp23;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class StringRequestWithJsonBody {


    private RequestQueue requestQueue;

    // Constructor
    public StringRequestWithJsonBody(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    // Method to make a POST request with JSON in the body
    public void makePostRequestWithJson(String jsonBody, String POST_URL, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_URL, responseListener, errorListener) {
            @Override
            public byte[] getBody() {
                return jsonBody.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        requestQueue.add(stringRequest);
    }
}