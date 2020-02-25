package br.ufsc.barcodescanner.service.repository;

import android.net.Uri;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.service.model.PictureUrl;
import br.ufsc.barcodescanner.viewmodel.BarcodeItemViewModel;

public class FirebasePictureStorage {

    private String TAG = "FirebasePictureStorage";

    private FirebaseStorage storage;

    public FirebasePictureStorage() {
        storage = FirebaseStorage.getInstance();
    }

    public void uploadDir(String externalStoragePath, final String barcodeValue,
                          BarcodeItemViewModel.StorageFileUploadedCallback storageFileUploadedCallback) {
        File dir = new File(externalStoragePath, barcodeValue);
        if (!dir.exists() || !dir.isDirectory()) {
            Log.e(TAG, String.format("Uploading [%s] failed, invalid directory: [%s]",
                    barcodeValue, dir.getPath()));
            return;
        }
        StorageReference barcodeReference = storage.getReference(barcodeValue);
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            File file = new File(dir, children[i]);
            Uri uri = Uri.fromFile(file);
            String filename = uri.getLastPathSegment().replaceFirst("[.][^.]+$", "");
            final StorageReference filePath = barcodeReference.child(filename);
            UploadTask uploadTask = filePath.putFile(uri);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    Log.e(TAG, String.format("Uploading [%s] failed: [%s]",
                            barcodeValue, task.getException().getMessage()));
                    Crashlytics.logException(task.getException());
                }
                return filePath.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, String.format("Uploading [%s] succeed!", filename));
                    Uri downloadUri = task.getResult();
                    if (downloadUri != null) {
                        String photoStringLink = downloadUri.toString();
                        storageFileUploadedCallback.update(new PictureUrl(filename, photoStringLink));
                    }
                    file.delete();
                    if(dir.length() == 0) {
                        dir.delete();
                    }
                } else {
                    Log.e(TAG, String.format("Uploading [%s] failed: [%s]",
                            barcodeValue, task.getException().getMessage()));
                    Crashlytics.logException(task.getException());
                }
            });
        }
    }

    public void delete(final Barcode barcode) {
        StorageReference barcodeReference = storage.getReference(barcode.value);
        for (String filename : barcode.images.keySet()) {
            barcodeReference.child(filename + ".jpg").delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, String.format("File [%s] deleted!", filename));
                    })
                    .addOnFailureListener(e -> Crashlytics.logException(e));
        }
    }

}
