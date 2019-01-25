package br.ufsc.barcodescanner.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.File;
import java.util.List;

import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.service.repository.FirebaseBarcodeRepository;
import br.ufsc.barcodescanner.service.repository.FirebasePictureStorage;
import br.ufsc.barcodescanner.utils.FileUtils;

public class BarcodeViewModel extends ViewModel {


    private FirebaseBarcodeRepository repository;
    private FirebasePictureStorage storage;
    private MutableLiveData<List<Barcode>> barcodes;
    private File picturesDir;

    public BarcodeViewModel() {
        this.repository = new FirebaseBarcodeRepository(new BarcodeChildEventListener());
        this.storage = new FirebasePictureStorage();
    }

    public void setPicturesDirPath(File picturesDir) {
        this.picturesDir = picturesDir;
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

    public void insert(String barcodeValue, String uuid) {
        repository.save(barcodeValue, uuid, completeResult -> reload(), successResult -> uploadDir());
    }

    private void uploadDir() {
        this.storage.uploadDir(picturesDir);
    }

    public void hasBarcode(String barcodeValue, FirebaseBarcodeRepository.HasBarcodeHandler handler) {
        repository.hasBarcode(barcodeValue, handler);
    }

    public void delete(String barcodeValue) {
        repository.delete(barcodeValue, task -> {
            reload();
            if (picturesDir != null && picturesDir.isDirectory()) {
                storage.delete(picturesDir);
                FileUtils.clearDir(picturesDir);
            }
        });
    }

    private class BarcodeChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            reload();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            reload();
            FileUtils.clearDir(picturesDir);
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }

    }
}
