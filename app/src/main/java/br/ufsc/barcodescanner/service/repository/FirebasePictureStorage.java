package br.ufsc.barcodescanner.service.repository;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class FirebasePictureStorage {

    private FirebaseStorage storage;

    public FirebasePictureStorage() {
        storage = FirebaseStorage.getInstance();
    }

    public void uploadDir(File dir) {
        StorageReference barcodeReference = storage.getReference(dir.getName());
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                File file = new File(dir, children[i]);
                Uri uri = Uri.fromFile(file);
                barcodeReference.child(file.getName()).putFile(uri).addOnSuccessListener(command -> file.delete());
            }
        }
    }

    public void delete(File dir) {
        StorageReference barcodeReference = storage.getReference(dir.getName());
        String[] files = dir.list();
        for (int i = 0; i < files.length; i++) {
            Log.i("FirebasePictureStorage", "Deleting file: " + files[i]);
            barcodeReference.child(files[i]).delete();
        }
    }

}
