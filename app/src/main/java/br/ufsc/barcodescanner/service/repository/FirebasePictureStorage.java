package br.ufsc.barcodescanner.service.repository;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.service.model.PictureUrl;
import br.ufsc.barcodescanner.viewmodel.BarcodeItemViewModel;

public class FirebasePictureStorage {

    private String TAG = "FirebasePictureStorage";

    private FirebaseStorage storage;

    public FirebasePictureStorage() {
        storage = FirebaseStorage.getInstance();
    }

    public void uploadDir(String externalStoragePath, final Barcode barcode,
                          BarcodeItemViewModel.StorageFileUploadedCallback storageFileUploadedCallback) {
        File dir = new File(externalStoragePath, barcode.value);
        if (!dir.exists() || !dir.isDirectory()) {
            Log.e(TAG, String.format("Uploading [%s] failed, invalid directory: [%s]",
                    barcode.value, dir.getPath()));
            return;
        }
        StorageReference barcodeReference = storage.getReference(barcode.value);
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            File file = new File(dir, children[i]);
            Uri uri = Uri.fromFile(file);
            String filename = uri.getLastPathSegment().replaceFirst("[.][^.]+$", "");
            final StorageReference filePath = barcodeReference.child(filename);
            UploadTask uploadTask = filePath.putFile(uri);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filePath.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, String.format("Uploading [%s] succeed!", filename));
                    Uri downloadUri = task.getResult();
                    Log.d(TAG, "Download uri:" + downloadUri);
                    if (downloadUri != null) {
                        String photoStringLink = downloadUri.toString();
                        storageFileUploadedCallback.update(new PictureUrl(filename, photoStringLink));
                    }
                    file.delete();
                } else {
                    Log.e(TAG,
                            String.format("Uploading [%s] failed: %s", barcode.value,
                                    task.getException().getMessage()));
                }
            });
            dir.delete();
        }
    }

    public void delete(final Barcode barcode) {
        StorageReference barcodeReference = storage.getReference(barcode.value);
        for (String filename : barcode.img.keySet()) {
            barcodeReference.child(filename + ".jpg").delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, String.format("File [%s] deleted!", filename));
                    })
                    .addOnFailureListener(e -> Log.e(TAG,
                            String.format("Delete file [%s] failed: %s", filename, e.getMessage())));
        }
    }

}
