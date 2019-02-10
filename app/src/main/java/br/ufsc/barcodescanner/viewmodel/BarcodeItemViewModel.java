package br.ufsc.barcodescanner.viewmodel;

import android.util.Log;

import java.util.Date;

import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.service.repository.FirebaseBarcodeRepository;

public class BarcodeItemViewModel extends AbstractBarcodeViewModel {

    private static final String TAG = "BarcodeItemViewModel";

    public void insert(final String barcodeValue, final String uuid, int groupId,
                       int subgroupId, int imgCount) {
        Barcode barcode = new Barcode();
        barcode.value = barcodeValue;
        barcode.dt = new Date().getTime();
        barcode.id = uuid;
        barcode.g = groupId;
        barcode.sg = subgroupId;
        barcode.ic = imgCount;

        repository.save(barcode, successResult -> {
            Log.e(TAG, String.format("Barcode [%s] successfully saved", barcodeValue));
            uploadDir(barcode);
        });
    }

    private void uploadDir(final Barcode barcode) {
        this.storage.uploadDir(externalStoragePath, barcode);
    }

    public void hasBarcode(String barcodeValue, FirebaseBarcodeRepository.HasBarcodeHandler handler) {
        repository.hasBarcode(barcodeValue, handler);
    }

}
