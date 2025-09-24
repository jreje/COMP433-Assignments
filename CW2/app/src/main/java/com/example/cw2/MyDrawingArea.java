package com.example.cw2;

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

public class MyDrawingArea extends View {
    // Path path = new Path();
    Path drawing = new Path();
    Bitmap bmp;

    public MyDrawingArea(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyDrawingArea(Context context) {
        super(context);
    }

    public MyDrawingArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyDrawingArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5f);

//        path.moveTo(100, 100);
//        path.lineTo(150, 50);
//        path.lineTo(200, 150);
//        path.lineTo(250, 100);
//        path.lineTo(300, 120);
//        path.moveTo(120, 400);
//        path.lineTo(170, 350);
//        path.lineTo(220, 450);
//        path.lineTo(270, 400);
//        path.lineTo(320, 420);
//        canvas.drawPath(path, p);
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
        return true;
    }

    public Bitmap getBitmap() {
        bmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setAntiAlias(true);
        p.setStrokeWidth(5f);
        // c.drawPath(path, p);
        c.drawPath(drawing, p);
        return bmp;
    }

    public void reset(){
        drawing.reset();
    }
}
