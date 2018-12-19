package br.ufsc.barcodescanner;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;

public class BarcodeProcessor implements Detector.Processor<Barcode> {

    private static final String TAG = "BarcodeProcessor";
    private Context context;

    public BarcodeProcessor(Context context) {
        this.context = context;
    }

    @Override
    public void release() {
        Log.d(TAG, "Barcode scanner has been stopped.");
    }

    @Override
    public void receiveDetections(Detector.Detections<Barcode> detections) {
        final SparseArray<Barcode> barcodes = detections.getDetectedItems();
        if (barcodes.size() != 0) {
            Intent intent = new Intent(this.context, ScannedItemActivity.class);
            intent.putExtra(ScannerFragment.BARCODE_VALUE, barcodes.valueAt(0).displayValue);
            this.context.startActivity(intent);
        }
    }

}
