package com.katza.myapplication_firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ImageDAO {
    private static final String COLLECTION_IMAGE_NAME = "image_labs";
    public void saveToFirestore(String name, String type, String data,
                                IFirestoreImageCallback<Void> iFirestoreImageCallback) {

        Map<String, Object> item = new HashMap<>();
        item.put("type", type);
        item.put("data", data);

        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection(COLLECTION_IMAGE_NAME).document(name);

        docRef.set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                iFirestoreImageCallback.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iFirestoreImageCallback.onFailure(e);
            }
        });
    }
}
