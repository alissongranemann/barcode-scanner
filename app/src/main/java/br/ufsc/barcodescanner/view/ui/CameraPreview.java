package br.ufsc.barcodescanner.view.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import br.ufsc.barcodescanner.R;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private static final String TAG = "CameraPreview";
    private BarcodeDetector detector; //TODO test

    private SurfaceHolder holder;
    private CameraSource cameraSource;

    public CameraPreview(Context context, CameraSource cameraSource) {
        super(context);

        holder = this.getHolder();
        holder.addCallback(this);

        this.cameraSource = cameraSource;
    }

    //TODO test
    public CameraPreview(Context context, CameraSource cameraSource, BarcodeDetector barcodeDetector) {
        this(context, cameraSource);
        this.detector = barcodeDetector;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!startCamera()) {
            ActivityCompat.requestPermissions((Activity) this.getContext(), new
                    String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public boolean startCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            cameraSource.start(holder);
//            new Thread(new DetectRun()).run(); //TODO test
        } catch (IOException e) {
            Log.e(TAG, "Error at camera source start: " + e.getMessage());
        }

        return true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        cameraSource.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (!startCamera()) {
            Toast.makeText(getContext(), "Camera permission is required.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //TODO test
    class DetectRun implements Runnable {

        public void run() {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.barcode_test);

            Frame outputFrame = new Frame.Builder()
                    .setBitmap(bm)
                    .build();
            detector.receiveFrame(outputFrame);
        }
    }

}
