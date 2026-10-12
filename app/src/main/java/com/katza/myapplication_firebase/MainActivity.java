package com.katza.myapplication_firebase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etId, etGrade;
    private Button btnInsert, btnSelect, btnUpdate, btnDelete;

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

        FirestoreDAO fsDAO = new FirestoreDAO();

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fsDAO.createDocument(etName.getText().toString(),
                        etId.getText().toString(),
                        Integer.parseInt(etGrade.getText().toString()));
            }
        });
    }
}