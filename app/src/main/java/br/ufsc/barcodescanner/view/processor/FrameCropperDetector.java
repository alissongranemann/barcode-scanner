package br.ufsc.barcodescanner.view.processor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;

import java.io.ByteArrayOutputStream;

public class FrameCropperDetector extends Detector {

    private static String TAG = "CameraCrop";

    private Detector mDelegate;

    public FrameCropperDetector(Detector delegate) {
        mDelegate = delegate;
    }

    public SparseArray detect(Frame frame) {
        int width = frame.getMetadata().getWidth();
        int height = frame.getMetadata().getHeight();

        int left = 0;
        int top = height / 4;
        int right = width;
        int bottom = height - (height / 4);

        YuvImage yuvImage = new YuvImage(frame.getGrayscaleImageData().array(),
                ImageFormat.NV21, width, height, null);
        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(left, top, right, bottom),
                100, byteArrayOutputStream);
        byte[] jpegArray = byteArrayOutputStream.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(jpegArray,
                0, jpegArray.length);

        Frame croppedFrame =
                new Frame.Builder()
                        .setBitmap(bitmap)
                        .setRotation(frame.getMetadata().getRotation())
                        .build();

        return mDelegate.detect(croppedFrame);
    }

    public boolean isOperational() {
        return mDelegate.isOperational();
    }

    public boolean setFocus(int id) {
        return mDelegate.setFocus(id);
    }
}