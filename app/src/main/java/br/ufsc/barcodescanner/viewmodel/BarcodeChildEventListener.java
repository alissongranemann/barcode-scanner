package br.ufsc.barcodescanner.viewmodel;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.annotations.Nullable;

public class BarcodeChildEventListener implements ChildEventListener {

    private String TAG = "BarcodeChildEventListener";
    private BarcodeListObserver observer;

    public BarcodeChildEventListener(BarcodeListObserver observer) {
        this.observer = observer;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.i(TAG, String.format("Child [%s] added", dataSnapshot.getKey()));
        observer.reload();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.i(TAG, String.format("Child [%s] changed", dataSnapshot.getKey()));
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        Log.i(TAG, String.format("Child [%s] removed", dataSnapshot.getKey()));
        observer.reload();
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.i(TAG, String.format("Child [%s] moved", dataSnapshot.getKey()));
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.i(TAG, "Child listener cancelled: " + databaseError.getMessage());
    }

}