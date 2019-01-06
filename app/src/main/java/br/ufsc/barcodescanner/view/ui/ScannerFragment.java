package br.ufsc.barcodescanner.view.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import br.ufsc.barcodescanner.R;
import br.ufsc.barcodescanner.view.BarcodeScannedHandler;
import br.ufsc.barcodescanner.view.FragmentLifecycle;
import br.ufsc.barcodescanner.view.scanner.BarcodeProcessor;
import br.ufsc.barcodescanner.viewmodel.BarcodeViewModel;

public class ScannerFragment extends Fragment implements FragmentLifecycle, BarcodeScannedHandler {

    public static final String BARCODE_VALUE = "br.ufsc.barcodescanner.BARCODE_VALUE";
    private static final String TAG = "ScannerFragment";
    private CameraPreview cameraPreview;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private FrameLayout frameLayout;
    private boolean initialised = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scanner, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        initSource();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.frameLayout = view.findViewById(R.id.surfaceView);
    }

    private void initSource() {
        if (!initialised) {
            Context context = getActivity();
            barcodeDetector = new BarcodeDetector.Builder(context)
                    .setBarcodeFormats(Barcode.ALL_FORMATS)
                    .build();
            barcodeDetector.setProcessor(new BarcodeProcessor(this));

            cameraSource = new CameraSource.Builder(context, barcodeDetector)
                    .setAutoFocusEnabled(true)
                    .build();

            cameraPreview = new CameraPreview(context, cameraSource, barcodeDetector);
            frameLayout.addView(cameraPreview);

            initialised = true;
        }
    }

    private void releaseCamera() {
        if (cameraSource != null) {
            cameraSource.release();
            cameraSource = null;
        }
        initialised = false;
    }

    @Override
    public void onPauseFragment() {
        releaseCamera();
        frameLayout.removeAllViews();
    }

    @Override
    public void onResumeFragment() {
        initSource();
    }

    @Override
    public void handle(String barcode) {
        Intent intent = new Intent(this.getActivity(), ScannedItemActivity.class);
        intent.putExtra(ScannerFragment.BARCODE_VALUE, barcode);
        startActivity(intent);
    }
}
