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

public class PhotoActivity extends AppCompatActivity {

    private EditText idInput;
    private ImageView photoPreview;

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {

                            Bitmap photo = result.getData()
                                    .getParcelableExtra("data");

                            if (photo != null) {
                                photoPreview.setImageBitmap(photo);

                                Toast.makeText(
                                        this,
                                        "Photo captured for ID: "
                                                + idInput.getText()
                                                .toString().trim(),
                                        Toast.LENGTH_SHORT
                                ).show();
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

        Button takePhotoButton =
                findViewById(R.id.takePhotoButton);

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
    }
}
