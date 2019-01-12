package br.ufsc.barcodescanner.service.repository;

import android.support.annotation.NonNull;

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
    private FirebaseDatabase database;

    public FirebaseBarcodeRepository() {
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
    }

    public void loadPage(Date lastDate, int pageLength, LoadPageHandler handler) {
        DatabaseReference reference = database.getReference(BARCODE_PATH);
        Query query = reference.orderByChild("createdAt").limitToFirst(pageLength).startAt(lastDate.getTime());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Barcode> barcodes = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Barcode value = snapshot.getValue(Barcode.class);
                    barcodes.add(value);
                }
                handler.handle(barcodes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean hasBarcode(String barcodeValue, HasBarcodeHandler handler) {
        DatabaseReference reference = database.getReference(BARCODE_PATH);
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

        return false;
    }

    public void save(String barcodeValue, String uuid) {
        Barcode barcode = new Barcode();
        barcode.value = barcodeValue;
        barcode.createdAt = new Date();
        barcode.userUuid = uuid;
        barcode.synced = false;

        DatabaseReference reference = database.getReference(BARCODE_PATH);
        reference.child(barcode.value).setValue(barcode);
    }

    public void delete(String barcodeValue) {
        DatabaseReference reference = database.getReference(BARCODE_PATH);
        reference.child(barcodeValue).removeValue();
    }

    public interface LoadPageHandler {

        void handle(List<Barcode> barcodes);

    }

    public interface HasBarcodeHandler {

        void handle(boolean hasChild);

    }
}
