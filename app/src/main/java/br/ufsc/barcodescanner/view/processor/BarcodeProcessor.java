package br.ufsc.barcodescanner.view.processor;

import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;

import br.ufsc.barcodescanner.view.BarcodeScannedHandler;

public class BarcodeProcessor implements Detector.Processor<Barcode> {

    private static final String TAG = "BarcodeProcessor";
    private BarcodeScannedHandler barcodeScannedHandler;

    public BarcodeProcessor(BarcodeScannedHandler handler) {
        this.barcodeScannedHandler = handler;
    }

    @Override
    public void release() {
        Log.d(TAG, "Barcode scanner has been stopped.");
    }

    @Override
    public void receiveDetections(Detector.Detections<Barcode> detections) {
        final SparseArray<Barcode> barcodes = detections.getDetectedItems();
        if (barcodes.size() != 0) {
            barcodeScannedHandler.onObjectDetected(barcodes.valueAt(0).displayValue);
        }
    }

}
