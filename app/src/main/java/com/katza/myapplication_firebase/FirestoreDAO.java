package com.katza.myapplication_firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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

    public void readDocumentByID(String docID, IFirestoreCallback iFirestoreCallback) {

        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection(COLLECTION_NAME).document(docID);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Student student = documentSnapshot.toObject(Student.class);
                //Log.d("DAO_FIREBASE", "SUCCESSFUL READ" + student);
                ArrayList<Student> students = new ArrayList<>();
                students.add(student);
                iFirestoreCallback.onDataReceived(students);
            }
        });
    }

    public void readAllDocuments(IFirestoreCallback iFirestoreCallback) {

        CollectionReference colRef = FirebaseFirestore.getInstance()
                .collection(COLLECTION_NAME);

        colRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Student> students = new ArrayList<>();
                for (DocumentSnapshot docSS : queryDocumentSnapshots.getDocuments()) {
                    Student student = docSS.toObject(Student.class);
                    //Log.d("DAO_FIREBASE", "SUCCESSFUL READ!" + student);
                    students.add(student);
                }
                iFirestoreCallback.onDataReceived(students);
            }
        });
    }

    public void updateDocument(String documentID, int newGrade) {

        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection(COLLECTION_NAME).document(documentID);

        docRef.update("grade", newGrade).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("DAO_FIREBASE", "SUCCESSFUL UPDATE");
            }
        });
    }

    public void deleteDocument(String documentID) {

        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection(COLLECTION_NAME).document(documentID);

        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("DAO_FIREBASE", "SUCCESSFUL DELETE");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DAO_FIREBASE", e.getMessage());
            }
        });
    }
}
