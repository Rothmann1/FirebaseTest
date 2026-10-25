package com.katza.myapplication_firebase;

public interface IFirestoreImageCallback<T> {

    void onSuccess();

    void onFailure(Exception e);
}