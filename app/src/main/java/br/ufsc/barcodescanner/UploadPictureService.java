package br.ufsc.barcodescanner;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;

import br.ufsc.barcodescanner.service.repository.FirebaseBarcodeRepository;
import br.ufsc.barcodescanner.service.repository.FirebasePictureStorage;

public class UploadPictureService extends Service {

    protected FirebaseBarcodeRepository repository;
    protected FirebasePictureStorage storage;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.repository = new FirebaseBarcodeRepository();
        this.storage = new FirebasePictureStorage();

        String picturesPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File dir = new File(picturesPath);
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            String barcode = children[i];
            this.storage.uploadDir(picturesPath, barcode, downloadUri ->
                    repository.update(barcode, downloadUri.toMap()));
        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }
}
