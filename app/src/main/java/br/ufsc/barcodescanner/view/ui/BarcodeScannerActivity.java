package br.ufsc.barcodescanner.view.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import br.ufsc.barcodescanner.R;
import br.ufsc.barcodescanner.view.BarcodeScannedHandler;
import br.ufsc.barcodescanner.view.processor.BarcodeProcessor;
import br.ufsc.barcodescanner.view.processor.FrameCropperDetector;
import br.ufsc.barcodescanner.view.ui.camera.CameraSource;
import br.ufsc.barcodescanner.view.ui.camera.CameraSourcePreview;
import br.ufsc.barcodescanner.viewmodel.BarcodeItemViewModel;

public class BarcodeScannerActivity extends AppCompatActivity implements BarcodeScannedHandler {

    public static final String BARCODE_VALUE = "br.ufsc.barcodescanner.BARCODE_VALUE";
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private static final int RC_HANDLE_GMS = 9001;
    private static final String TAG = "BarcodeScannerActivity";

    private CameraSource cameraSource;
    private CameraSourcePreview mPreview;
    private BarcodeItemViewModel viewModel;
    private FirebaseAuth mAuth;
    private boolean detected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        mPreview = findViewById(R.id.preview);

        FloatingActionButton fab = findViewById(R.id.barcode_list_fab);
        fab.setOnClickListener(view -> this.openBarcodeListActivity());

        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            requestCameraPermission();
        }

        viewModel = ViewModelProviders.of(this).get(BarcodeItemViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.signInAnonymously().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "signInAnonymously success");
            } else {
                Log.w(TAG, "signInAnonymously failure: ", task.getException());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        detected = false;
        startCameraSource();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if (mPreview != null) {
            mPreview.release();
        }
    }

    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        ActivityCompat.requestPermissions(this, permissions,
                RC_HANDLE_CAMERA_PERM);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("InlinedApi")
    private void createCameraSource() {
        Context context = getApplicationContext();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.EAN_8 | Barcode.EAN_13 | Barcode.UPC_A
                        | Barcode.UPC_E | Barcode.ITF | Barcode.CODE_39 | Barcode.CODE_128)
                .build();
        FrameCropperDetector boxDetector = new FrameCropperDetector(barcodeDetector);

        boxDetector.setProcessor(new BarcodeProcessor(this));

        if (!boxDetector.isOperational()) {
            Log.w(TAG, "Detector dependencies are not yet available.");

            IntentFilter lowStorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowStorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

        CameraSource.Builder builder = new CameraSource.Builder(this, boxDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(metrics.heightPixels, metrics.widthPixels)
                .setRequestedFps(30.0f);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        cameraSource = builder.build();
    }

    private void startCameraSource() throws SecurityException {
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg = GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        createCameraSource();

        if (cameraSource != null) {
            try {
                mPreview.start(cameraSource);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            createCameraSource();
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = (dialog, id) -> finish();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Barcode scanner")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    private void openBarcodeListActivity() {
        Intent intent = new Intent(this, BarcodeListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onObjectDetected(String barcode) {
        if (!detected) {
            Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
            detected = true;
            viewModel.hasBarcode(barcode, hasChild -> {
                if (hasChild) {
                    this.releaseCamera();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.overwrite_message)
                            .setNeutralButton(R.string.ok, (dialog, id) -> {
                                this.startCameraSource();
                                detected = false;
                            }).show();
                } else {
                    Intent intent = new Intent(this, ScannedBarcodeActivity.class);
                    intent.putExtra(BarcodeScannerActivity.BARCODE_VALUE, barcode);
                    startActivity(intent);
                }
            });
        }
    }

}
