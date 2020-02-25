package br.ufsc.barcodescanner.service.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.viewmodel.BarcodeChildEventListener;


public class FirebaseBarcodeRepository {

    private static final String BARCODE_PATH = "barcodes";
    private static final int PAGE_LENGTH = 10;
    private static final String TAG = "BarcodeRepository";

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().setPersistenceCacheSizeBytes(50000000);
    }

    private final DatabaseReference reference;

    private FirebaseDatabase database;

    public FirebaseBarcodeRepository() {
        database = FirebaseDatabase.getInstance();
        this.reference = database.getReference(BARCODE_PATH);
    }

    public void setChildEventListener(BarcodeChildEventListener listener) {
        reference.addChildEventListener(listener);
    }

    public void loadPage(LoadPageHandler handler) {
        Query query = reference.orderByChild("dt").limitToLast(PAGE_LENGTH);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Barcode> barcodes = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Barcode barcode = snapshot.getValue(Barcode.class);
                    barcode.value = snapshot.getKey();
                    barcodes.add(barcode);
                }
                Collections.reverse(barcodes);
                handler.handle(barcodes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Load page cancelled: " + databaseError.getMessage());
            }
        };
        query.addListenerForSingleValueEvent(listener);
    }

    public void hasBarcode(String barcodeValue, HasBarcodeHandler handler) {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean hasChild = dataSnapshot.hasChild(barcodeValue);
                handler.handle(hasChild);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, String.format("Check if [%s] was already inserted failed: ",
                        barcodeValue, databaseError.getMessage()));
            }
        };
        reference.addListenerForSingleValueEvent(listener);
    }

    public void save(Barcode barcode, OnSuccessListener<Void> successListener) {
        reference.child(barcode.value).setValue(barcode.toMap())
                .addOnSuccessListener(successListener)
                .addOnFailureListener(e -> Crashlytics.logException(e));
    }

    public void delete(String barcodeValue, OnSuccessListener<Void> handler) {
        reference.child(barcodeValue).removeValue()
                .addOnSuccessListener(handler)
                .addOnFailureListener(e -> Crashlytics.logException(e));
    }

    public void update(Barcode barcode, Map<String, String> photoUrl) {
        if (barcode.images == null) {
            barcode.images = new HashMap<>();
        }
        barcode.images.putAll(photoUrl);
        reference.child(barcode.value).child("img").setValue(barcode.images)
                .addOnCompleteListener(task -> Log.d(TAG, "Barcode updated with image: "
                        + photoUrl.toString()));
    }

    public void update(String barcode, Map<String, String> photoUrl) {
        Map<String, Object> childUpdates = new HashMap<>();
        for (Map.Entry<String,String> entry : photoUrl.entrySet()) {
            childUpdates.put("/img/" + entry.getKey(), entry.getValue());
        }
        reference.child(barcode).updateChildren(childUpdates)
                .addOnCompleteListener(task -> Log.d(TAG, "Barcode updated with image: "
                        + photoUrl.toString()));
    }

    public interface LoadPageHandler {

        void handle(List<Barcode> barcodeList);

    }

    public interface HasBarcodeHandler {

        void handle(boolean hasChild);

    }
}
