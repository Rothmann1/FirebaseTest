package com.katza.myapplication_firebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class PhotoActivity extends AppCompatActivity {

    private EditText idInput;
    private ImageView photoPreview;
    Button takePhotoButton, btnSearchPhoto;

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {

                            Bitmap photo = result.getData()
                                    .getParcelableExtra("data");

                            if (photo != null) {
                                // Display the preview
                                photoPreview.setImageBitmap(photo);

                                String studentId = idInput.getText().toString().trim();

                                // Convert and compress the bitmap to a Base64 string using ImageUtils
                                String base64Image = ImageUtils.convertBitmapToBase64(photo);

                                if (base64Image != null) {
                                    // Update the Student document in Firestore with the Base64 string
                                    FirebaseFirestore.getInstance()
                                            .collection("students")
                                            .document(studentId)
                                            .update("base64Image", base64Image)
                                            .addOnSuccessListener(unused ->
                                                    Toast.makeText(this, "Photo saved for ID: " + studentId, Toast.LENGTH_SHORT).show())
                                            .addOnFailureListener(e ->
                                                    Toast.makeText(this, "Failed to save photo to Firebase: " + e.getMessage(), Toast.LENGTH_LONG).show());
                                } else {
                                    Toast.makeText(this, "Failed to process image.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        idInput = findViewById(R.id.idInput);
        photoPreview = findViewById(R.id.photoPreview);

        takePhotoButton = findViewById(R.id.takePhotoButton);
        btnSearchPhoto = findViewById(R.id.btnSearchPhoto); // Ensure this ID exists in activity_photo.xml

        takePhotoButton.setOnClickListener(v -> {
            String id = idInput.getText().toString().trim();

            if (id.isEmpty()) {
                idInput.setError("תעודת זהות");
                idInput.requestFocus();
                return;
            }

            Intent cameraIntent =
                    new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (cameraIntent.resolveActivity(getPackageManager())
                    != null) {
                cameraLauncher.launch(cameraIntent);
            } else {
                Toast.makeText(
                        this,
                        "No camera app found",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        btnSearchPhoto.setOnClickListener(v -> {
            String id = idInput.getText().toString();

            if (id.isEmpty()) {
                idInput.setError("תעודת זהות");
                idInput.requestFocus();
                return;
            }

            FirebaseFirestore.getInstance()
                    .collection("students")
                    .document(id)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String base64Image = documentSnapshot.getString("base64Image");

                            if (base64Image != null && !base64Image.isEmpty()) {
                                Bitmap bitmap = ImageUtils.convertBase64ToBitmap(base64Image);
                                if (bitmap != null) {
                                    photoPreview.setImageBitmap(bitmap);
                                    Toast.makeText(this, "Photo loaded", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(this, "Failed to decode image data", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "No photo saved for this student", Toast.LENGTH_SHORT).show();
                                photoPreview.setImageDrawable(null); // Clear preview if no image
                            }
                        } else {
                            Toast.makeText(this, "Student ID not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }
}