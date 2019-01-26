package br.ufsc.barcodescanner.service.repository;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class FirebasePictureStorage {

    private String TAG = "FirebasePictureStorage";

    private FirebaseStorage storage;

    public FirebasePictureStorage() {
        storage = FirebaseStorage.getInstance();
    }

    public void uploadDir(String externalStoragePath, String barcodeValue) {
        File dir = new File(externalStoragePath, barcodeValue);
        if (!dir.exists() || !dir.isDirectory()) {
            Log.e(TAG, String.format("Uploading %s failed, invalid directory", barcodeValue));
            return;
        }
        StorageReference barcodeReference = storage.getReference(dir.getName());
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            File file = new File(dir, children[i]);
            Uri uri = Uri.fromFile(file);
            barcodeReference.child(file.getName()).putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> Log.d(TAG, "Upload succeed!"))
                    .addOnFailureListener(e -> Log.d(TAG, "Upload failed: " + e.getMessage()));
        }
    }

    public void delete(String externalStoragePath, String barcodeValue) {
        File pictureDir = new File(externalStoragePath, barcodeValue);
        if (!pictureDir.exists() || !pictureDir.isDirectory()) {
            Log.d(TAG, String.format("Directory %s is invalid.", barcodeValue));
            return;
        }
        StorageReference barcodeReference = storage.getReference(pictureDir.getName());
        File[] files = pictureDir.listFiles();
        for (File file : files) {
            String filename = file.getName();
            barcodeReference.child(filename).delete()
                    .addOnSuccessListener(aVoid -> {
                        file.delete();
                        Log.d(TAG, String.format("File %s deleted!", filename));
                    })
                    .addOnFailureListener(e -> Log.d(TAG, "Delete failed: " + e.getMessage()));
        }
    }

}
