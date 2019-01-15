package br.ufsc.barcodescanner.view.barcode.scanner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class ViewPort extends ViewGroup {

    public ViewPort(Context context) {
        super(context);
    }

    public ViewPort(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPort(Context context, AttributeSet attrs, int defStyle) {
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

        int x0 = 0;
        int dx = canvas.getWidth();
        int y0 = canvas.getHeight() / 2;
        int dy = canvas.getHeight() / 5;

        Paint eraser = new Paint();
        eraser.setAntiAlias(true);
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        RectF rect = new RectF(x0, y0 - dy, dx, y0 + dy);
        canvas.drawRect(rect, eraser);
    }
}