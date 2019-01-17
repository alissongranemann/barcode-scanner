package br.ufsc.barcodescanner.view.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import br.ufsc.barcodescanner.R;
import br.ufsc.barcodescanner.view.BarcodeScannedHandler;
import br.ufsc.barcodescanner.view.processor.BarcodeProcessor;

public class BarcodeScannerActivity extends AppCompatActivity implements BarcodeScannedHandler {

    public static final String BARCODE_VALUE = "br.ufsc.barcodescanner.BARCODE_VALUE";
    private static final String TAG = "BarcodeScannerActivity";

    private CameraPreview cameraPreview;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        this.frameLayout = findViewById(R.id.surfaceView);

        FloatingActionButton fab = findViewById(R.id.barcode_list_fab);
        fab.setOnClickListener(view -> this.openBarcodeListActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        initSource();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void initSource() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        barcodeDetector.setProcessor(new BarcodeProcessor(this));
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1024, 768)
                .build();

        cameraPreview = new CameraPreview(this, cameraSource);
        frameLayout.addView(cameraPreview);
    }

    private void releaseCamera() {
        if (cameraSource != null) {
            cameraSource.release();
            cameraSource = null;
        }
    }

    private void openBarcodeListActivity() {
        Intent intent = new Intent(this, BarcodeListActivity.class);
        startActivity(intent);
    }

    @Override
    public void handle(String barcode) {
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);

        Intent intent = new Intent(this, ScannedBarcodeActivity.class);
        intent.putExtra(BarcodeScannerActivity.BARCODE_VALUE, barcode);
        startActivity(intent);
    }

}
