package br.ufsc.barcodescanner.viewmodel;

import br.ufsc.barcodescanner.service.repository.FirebaseBarcodeRepository;

public class BarcodeItemViewModel extends AbstractBarcodeViewModel {

    public void insert(final String barcodeValue, final String uuid) {
        repository.save(barcodeValue, uuid, successResult -> uploadDir(barcodeValue));
    }

    private void uploadDir(final String barcodeValue) {
        this.storage.uploadDir(externalStoragePath, barcodeValue);
    }

    public void hasBarcode(String barcodeValue, FirebaseBarcodeRepository.HasBarcodeHandler handler) {
        repository.hasBarcode(barcodeValue, handler);
    }

}
