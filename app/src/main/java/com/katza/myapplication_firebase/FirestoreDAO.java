package com.katza.myapplication_firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirestoreDAO {
    private static final String COLLECTION_NAME = "students";

    public void createDocument(String name, String id, int grade) {
        Map<String, Object> studentRecord = new HashMap<>();
        studentRecord.put("fullName", name);
        studentRecord.put("id", id);
        studentRecord.put("grade", grade);

        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection(COLLECTION_NAME).document(id);

        docRef.set(studentRecord).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("DAO_FIREBASE", "SUCCESSFUL WRITE");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DAO_FIREBASE", e.getMessage().toString());
            }
        });
    }
}
