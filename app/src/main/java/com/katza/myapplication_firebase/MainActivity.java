package com.katza.myapplication_firebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etId, etGrade;
    private Button btnInsert, btnSelect, btnUpdate, btnDelete, btnOpenPhoto;
    private TextView tvContent;
    private ImageView imageView;
    private ActivityResultLauncher<Intent> cameraLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etName = findViewById(R.id.fullname);
        etId = findViewById(R.id.tz);
        etGrade = findViewById(R.id.grade);
        btnInsert = findViewById(R.id.btnInsert);
        tvContent = findViewById(R.id.tvcontent);
        btnSelect = findViewById(R.id.btnSelect);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        imageView = findViewById(R.id.imageView);
        btnOpenPhoto = findViewById(R.id.openPhotoActivityButton);


        FirestoreDAO fsDAO = new FirestoreDAO();

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fsDAO.createDocument(etName.getText().toString(),
                        etId.getText().toString(),
                        Integer.parseInt(etGrade.getText().toString()));
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvContent.setText("");

                if (!etGrade.getText().toString().isEmpty()) {
                        fsDAO.readStudentsByMinGrade(Integer.parseInt(etGrade.getText().toString()), new IFirestoreCallback() {
                            @Override
                            public void onDataReceived(ArrayList<Student> students) {
                                for (int i = 0; i < students.size(); i++) {
                                    tvContent.setText(tvContent.getText().toString() + students.get(i).toString());
                                }
                            }
                        });
                } else
                    fsDAO.readAllDocuments(new IFirestoreCallback() {
                        @Override
                        public void onDataReceived(ArrayList<Student> students) {
                            for (int i = 0; i < students.size(); i++) {
                                tvContent.setText(tvContent.getText().toString() + students.get(i).toString());
                            }
                        }
                    });
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fsDAO.updateDocument(etId.getText().toString(), Integer.parseInt(etGrade.getText().toString()));
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fsDAO.deleteDocument(etId.getText().toString());
            }
        });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK
                            && result.getData() != null) {

                        Bundle extras = result.getData().getExtras();

                        if (extras != null) {
                            Bitmap photo =
                                    (Bitmap) extras.get("data");

                            if (photo != null) {
                                imageView.setImageBitmap(photo);
                            }
                        }
                    } else {
                        Toast.makeText(
                                this,
                                "Image capture cancelled",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        btnOpenPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    PhotoActivity.class
            );
            startActivity(intent);
        });
    }
}