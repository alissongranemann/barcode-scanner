package br.ufsc.barcodescanner.service.repository;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import br.ufsc.barcodescanner.service.model.Barcode;

public class FirebasePictureStorage {

    private String TAG = "FirebasePictureStorage";

    private FirebaseStorage storage;

    public FirebasePictureStorage() {
        storage = FirebaseStorage.getInstance();
    }

    private static String getDir(Barcode barcode) {
        return "Grupo " + barcode.g + '/' + "Subgrupo " +
                barcode.sg + '/' + barcode.value;
    }

    public void uploadDir(String externalStoragePath, final Barcode barcode) {
        File dir = new File(externalStoragePath, barcode.value);
        if (!dir.exists() || !dir.isDirectory()) {
            Log.e(TAG, String.format("Uploading [%s] failed, invalid directory: [%s]",
                    barcode.value, dir.getPath()));
            return;
        }
        String dirName = getDir(barcode);
        StorageReference barcodeReference = storage.getReference(dirName);
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            File file = new File(dir, children[i]);
            Uri uri = Uri.fromFile(file);
            barcodeReference.child(uri.getLastPathSegment()).putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        file.delete();
                        Log.i(TAG, String.format("Uploading [%s] succeed!", barcode.value));
                    })
                    .addOnFailureListener(e -> Log.e(TAG,
                            String.format("Uploading [%s] failed: %s", barcode.value,
                                    e.getMessage())));
        }
        dir.delete();
    }

    public void delete(final Barcode barcode) {
        String dir = getDir(barcode);
        StorageReference barcodeReference = storage.getReference(dir);
        for (int i = 1; i <= barcode.ic; i++) {
            final String filename = String.format("%s_%d.jpg", barcode.value, i);
            barcodeReference.child(filename).delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, String.format("File [%s] deleted!", filename));
                    })
                    .addOnFailureListener(e -> Log.e(TAG,
                            String.format("Delete file [%s] failed: %s", filename, e.getMessage())));
        }
    }

}
