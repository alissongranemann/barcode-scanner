package ufsc.br.barcode_scanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class ScannerActivity extends AppCompatActivity {

    public static final String BARCODE_VALUE = "br.ufsc.barcodescanner.BARCODE_VALUE";

    TextView txtBarcodeValue;
    FrameLayout frameLayout;
    private CameraPreview cameraPreview;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        frameLayout = findViewById(R.id.surfaceView);
    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started",
                Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                //.setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        cameraPreview = new CameraPreview(this, cameraSource);
        frameLayout.addView(cameraPreview);

        barcodeDetector.setProcessor(new BarcodeProcessor(this, txtBarcodeValue));
    }

}
