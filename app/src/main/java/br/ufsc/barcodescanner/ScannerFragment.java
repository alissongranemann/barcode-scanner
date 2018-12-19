package br.ufsc.barcodescanner;

import android.content.Context;
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

public class ScannerFragment extends Fragment {

    public static final String BARCODE_VALUE = "br.ufsc.barcodescanner.BARCODE_VALUE";
    private static final String TAG = "ScannerFragment";
    private CameraPreview cameraPreview;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private FrameLayout frameLayout;
    private boolean initilialized = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scanner, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.frameLayout = view.findViewById(R.id.surfaceView);
    }

    private void initialiseDetectorsAndSources() {
        initilialized = true;
        Context context = getActivity().getApplicationContext();
        barcodeDetector = new BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(context, barcodeDetector)
                //.setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        cameraPreview = new CameraPreview(context, cameraSource);
        frameLayout.addView(cameraPreview);

        barcodeDetector.setProcessor(new BarcodeProcessor(context));
    }

}
