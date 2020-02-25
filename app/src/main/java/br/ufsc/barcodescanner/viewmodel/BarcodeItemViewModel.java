package br.ufsc.barcodescanner.viewmodel;

import android.util.Log;

import java.util.Date;

import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.service.model.PictureUrl;
import br.ufsc.barcodescanner.service.repository.FirebaseBarcodeRepository;

public class BarcodeItemViewModel extends AbstractBarcodeViewModel {

    private static final String TAG = "BarcodeItemViewModel";

    public void insert(final String barcodeValue, final String uuid, int groupId,
                       int subgroupId) {
        Barcode barcode = new Barcode();
        barcode.value = barcodeValue;
        barcode.created_at = new Date().getTime();
        barcode.user_uuid = uuid;
        barcode.group = groupId;
        barcode.subgroup = subgroupId;

        repository.save(barcode, successResult -> {
            Log.d(TAG, String.format("Barcode [%s] successfully saved", barcodeValue));
            uploadDir(barcode);
        });
    }

    private void uploadDir(final Barcode barcode) {
        this.storage.uploadDir(externalStoragePath, barcode.value, downloadUri ->
                repository.update(barcode, downloadUri.toMap()));
    }

    public void hasBarcode(String barcodeValue, FirebaseBarcodeRepository.HasBarcodeHandler handler) {
        repository.hasBarcode(barcodeValue, handler);
    }

    public interface StorageFileUploadedCallback {

        void update(PictureUrl downloadUri);

    }

}
