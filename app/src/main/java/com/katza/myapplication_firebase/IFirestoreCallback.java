package com.katza.myapplication_firebase;

import java.util.ArrayList;

public interface IFirestoreCallback {
    void onDataReceived(ArrayList<Student> students);
}
