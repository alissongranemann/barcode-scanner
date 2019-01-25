package br.ufsc.barcodescanner.view.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class CameraViewCrop extends ViewGroup {

    private static final String TAG = "CameraCrop";

    public CameraViewCrop(Context context) {
        super(context);
    }

    public CameraViewCrop(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraViewCrop(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        int height = canvas.getHeight();
        int width = canvas.getWidth();

        int left = 0;
        int top = height / 4;
        int right = width;
        int bottom = height - (height / 4);

        Paint eraser = new Paint();
        eraser.setAntiAlias(true);
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        RectF rect = new RectF(left, top, right, bottom);
        canvas.drawRect(rect, eraser);
    }
}