package br.ufsc.barcodescanner.service.repository;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufsc.barcodescanner.service.model.Barcode;


public class FirebaseBarcodeRepository {

    private static final String BARCODE_PATH = "barcodes";
    private static final int PAGE_LENGTH = 10;

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance();
    }

    private final DatabaseReference reference;

    private FirebaseDatabase database;

    public FirebaseBarcodeRepository(ChildEventListener childListener) {
        database = FirebaseDatabase.getInstance();
        this.reference = database.getReference(BARCODE_PATH);
        this.reference.keepSynced(false);
        if (childListener != null) {
            reference.addChildEventListener(childListener);
        }
    }

    public void loadPage(Date lastDate, LoadPageHandler handler) {
        Query query = reference.orderByChild("c").limitToFirst(PAGE_LENGTH).startAt(lastDate.getTime());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Barcode> barcodes = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Barcode barcode = snapshot.getValue(Barcode.class);
                    barcode.value = snapshot.getKey();
                    barcodes.add(barcode);
                }
                handler.handle(barcodes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    public Barcode save(String barcodeValue, String uuid, OnCompleteListener<Void> handler) {
        Barcode barcode = new Barcode();
        barcode.value = barcodeValue;
        barcode.dt = new Date();
        barcode.id = uuid;

        reference.child(barcode.value).setValue(barcode).addOnCompleteListener(handler);

        return barcode;
    }

    public void delete(String barcodeValue, OnCompleteListener<Void> handler) {
        reference.child(barcodeValue).removeValue().addOnCompleteListener(handler);
    }

    public interface LoadPageHandler {

        void handle(List<Barcode> barcodes);

    }

    public interface HasBarcodeHandler {

        void handle(boolean hasChild);

    }
}
