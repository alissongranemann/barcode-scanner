package br.ufsc.barcodescanner.view;

public interface BarcodeScannedHandler {

    void onObjectDetected(String barcode);
}
