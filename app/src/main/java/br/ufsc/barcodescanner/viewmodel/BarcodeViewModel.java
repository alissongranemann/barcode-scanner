package br.ufsc.barcodescanner.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Date;
import java.util.List;

import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.service.repository.FirebaseBarcodeRepository;

public class BarcodeViewModel extends ViewModel {


    private FirebaseBarcodeRepository repository;
    private MutableLiveData<List<Barcode>> barcodes;

    public BarcodeViewModel() {
        this.repository = new FirebaseBarcodeRepository(new BarcodeChildEventListener());
    }

    public MutableLiveData<List<Barcode>> getBarcodes() {
        if (barcodes == null) {
            barcodes = new MutableLiveData<>();
            reload();
        }
        return barcodes;
    }

    public void reload() {
        if (barcodes != null) {
            repository.loadPage(page -> barcodes.setValue(page));
        }
    }

    public void delete(Barcode barcode) {
        this.delete(barcode.value);
    }

    public void insert(String barcodeValue, String uuid) {
        repository.save(barcodeValue, uuid, task -> reload());
    }

    public void hasBarcode(String barcodeValue, FirebaseBarcodeRepository.HasBarcodeHandler handler) {
        repository.hasBarcode(barcodeValue, handler);
    }

    public void delete(String barcodeValue) {
        repository.delete(barcodeValue, task -> reload());
    }

    private class BarcodeChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Barcode value = dataSnapshot.getValue(Barcode.class);
            reload();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            reload();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }

    }
}
