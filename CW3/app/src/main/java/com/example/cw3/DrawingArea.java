package com.example.cw3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DrawingArea extends View {
    private Path drawing = new Path();
    private Bitmap bmp;

    public DrawingArea(Context context) {
        super(context);
    }

    public DrawingArea(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawingArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawingArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5f);

        canvas.drawPath(drawing, p);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(), y = event.getY();
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            drawing.moveTo(x, y);
        }
        else if (action == MotionEvent.ACTION_MOVE) {
            drawing.lineTo(x, y);
        }
        invalidate();
        return true;
    }

    public Bitmap getBitmap() {
        // Turn canvas drawing to to bitmap
        bmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setAntiAlias(true);
        p.setStrokeWidth(5f);
        c.drawPath(drawing, p);
        return bmp;
    }

    public void clear(){
        // clear path
        drawing.reset();

        // Redraw canvas so the path doesn't appear
        invalidate();
    }
}
