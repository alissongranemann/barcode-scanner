package br.ufsc.barcodescanner.service.repository;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.viewmodel.BarcodeChildEventListener;


public class FirebaseBarcodeRepository {

    private static final String BARCODE_PATH = "barcodes";
    private static final int PAGE_LENGTH = 10;

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().setPersistenceCacheSizeBytes(20000000);
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

            }
        };
        reference.addListenerForSingleValueEvent(listener);
    }

    public Barcode save(String barcodeValue, String uuid, OnSuccessListener<Void> sucessListener) {
        Barcode barcode = new Barcode();
        barcode.value = barcodeValue;
        barcode.dt = new Date().getTime();
        barcode.id = uuid;

        reference.child(barcode.value).setValue(barcode).addOnSuccessListener(sucessListener);

        return barcode;
    }

    public void delete(String barcodeValue, OnSuccessListener<Void> handler) {
        reference.child(barcodeValue).removeValue().addOnSuccessListener(handler);
    }

    public interface LoadPageHandler {

        void handle(List<Barcode> barcodes);

    }

    public interface HasBarcodeHandler {

        void handle(boolean hasChild);

    }
}
