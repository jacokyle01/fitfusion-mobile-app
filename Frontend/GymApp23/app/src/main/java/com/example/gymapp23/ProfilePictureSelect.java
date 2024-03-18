package com.example.gymapp23;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProfilePictureSelect extends AppCompatActivity {
    private ImageView pic1, pic2, pic3;
    private String TAG = ProfilePictureSelect.class.getSimpleName(), profilePicURL;
    private Button backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture_select);

        profilePicURL = "http://coms-309-067.class.las.iastate.edu:8080/people/@/" + getIntent().getStringExtra("username") + "/profilePicture";


        pic1 = findViewById(R.id.picture1);
        pic2 = findViewById(R.id.picture2);
        pic3 = findViewById(R.id.picture3);
        backBtn = findViewById(R.id.profileSelectBackBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePictureSelect.this, ProfilePage.class);
                i.putExtra("username", getIntent().getStringExtra("username"));
                startActivity(i);
                finish();
            }
        });

        pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadProfilePic("drawable/user_profile.png");

                InputStream inputStream = getResources().openRawResource(R.raw.user_profile);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

// Assume you have the required permissions to write to external storage
                File file = new File(getExternalFilesDir(null), "user_profile.png");

                try (OutputStream os = new FileOutputStream(file)) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Now you can use 'file' as a regular file with a file path
                String filePath = file.getAbsolutePath();
                uploadImage(filePath);
                Log.d("Profile Pic Upload", "pic 1 uploaded");

            }
        });
        pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadProfilePic("drawable/user_profile.png");

                InputStream inputStream = getResources().openRawResource(R.raw.aiprofilepic1);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

// Assume you have the required permissions to write to external storage
                File file = new File(getExternalFilesDir(null), "aiprofilepic1.png");

                try (OutputStream os = new FileOutputStream(file)) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Now you can use 'file' as a regular file with a file path
                String filePath = file.getAbsolutePath();
                uploadImage(filePath);
                Log.d("Profile Pic Upload", "pic 2 uploaded");

            }
        });
        pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadProfilePic("drawable/user_profile.png");

                InputStream inputStream = getResources().openRawResource(R.raw.aiprofilepic2);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

// Assume you have the required permissions to write to external storage
                File file = new File(getExternalFilesDir(null), "aiprofile2.png");

                try (OutputStream os = new FileOutputStream(file)) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Now you can use 'file' as a regular file with a file path
                String filePath = file.getAbsolutePath();
                uploadImage(filePath);
                Log.d("Profile Pic Upload", "pic 3 uploaded");

            }
        });

    }



    /**
     * Uploads an image to a remote server using a multipart Volley request.
     *
     * This method creates and executes a multipart request using the Volley library to upload
     * an image to a predefined server endpoint. The image data is sent as a byte array and the
     * request is configured to handle multipart/form-data content type. The server is expected
     * to accept the image with a specific key ("image") in the request.
     *
     */
    private void uploadImage(String pathName){

        Uri imageUri = Uri.fromFile(new File(pathName));

        Log.d("uploadImage Debug", "URI CREATED" + imageUri.toString());

        byte[] imageData = convertImageUriToBytes(imageUri);
        assert imageData != null;
        Log.d("IMAGE DATA", "Length: " + imageData.length);
        MultipartRequest multipartRequest = new MultipartRequest(
                Request.Method.POST,
                profilePicURL,
                imageData,
                response -> {
                    // Handle response
                    Toast.makeText(ProfilePictureSelect.this, response,Toast.LENGTH_LONG).show();
                    Log.d("Upload", "Response: " + response);
                },
                error -> {
                    // Handle error
                    String errorMessage = error.getMessage();
                    if (errorMessage != null && !errorMessage.isEmpty()) {
                        Toast.makeText(ProfilePictureSelect.this, errorMessage, Toast.LENGTH_LONG).show();
                        Log.e("Upload", "Error: " + error.getMessage());
                    } else {
                        // Handle the case where the error message is null or empty
                        Log.e("Upload", "Error message is null or empty");
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(multipartRequest);
    }

    /**
     * Converts the given image URI to a byte array.
     *
     * This method takes a URI pointing to an image and converts it into a byte array. The conversion
     * involves opening an InputStream from the content resolver using the provided URI, and then
     * reading the content into a byte array. This byte array represents the binary data of the image,
     * which can be used for various purposes such as uploading the image to a server.
     *
     * @param imageUri The URI of the image to be converted. This should be a content URI that points
     *                 to an image resource accessible through the content resolver.
     * @return A byte array representing the image data, or null if the conversion fails.
     * @throws IOException If an I/O error occurs while reading from the InputStream.
     */
    private byte[] convertImageUriToBytes(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            return byteBuffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}