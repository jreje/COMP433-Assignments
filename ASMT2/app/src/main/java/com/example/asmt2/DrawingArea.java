package com.example.asmt2;

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

import java.util.ArrayList;

public class DrawingArea extends View {
    private Path drawing = new Path();
    private Bitmap bmp;
    // === BOUNCE LOGIC VARS ===
    private boolean shaken = false;
    private ArrayList<Float> pathX = new ArrayList<>(100);
    private ArrayList<Float> pathY = new ArrayList<>(100);

    private ArrayList<Ball> balls = new ArrayList<>();



    public DrawingArea(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawingArea(Context context) {
        super(context);
    }

    public DrawingArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawingArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Create canvas, path, and paint tools
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5f);

        Paint p2 = new Paint();
        p2.setColor(Color.BLUE);

        // Draw path on canvas
        canvas.drawPath(drawing, p);

        // Ball bounce logic
        ArrayList<Ball> deadBalls = new ArrayList<>();  // balls that should disappear
        if (shaken) {
            for (Ball ball: balls) {
                canvas.drawCircle(ball.x, ball.y, ball.radius, p2);
                // Ball moves down
                ball.y += ball.velocity;

                // ball at bottom
                if (ball.y + ball.radius >= getHeight()) {
                    ball.y = getHeight() - ball.radius;
                    // Less energy per bounce so velocity * 0.7
                    // Make ball move upwards
                    ball.velocity = -(int)(ball.velocity * .7);
                } else {
                    // Increase velocity (acceleration)
                    // If velocity was negative (ball was moving up), this switches it back downwards
                    ball.velocity += 1;
                }

                // add balls that should disappear
                if (ball.isExpired()) {
                    deadBalls.add(ball);
                }
            }
        }

        balls.removeAll(deadBalls);

        if (!balls.isEmpty() || !deadBalls.isEmpty()) {
            invalidate(); // keep drawing balls if they exist
        } else {
            shaken = false; // otherwise don't generate balls
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();

        // Path drawing
        if (action == MotionEvent.ACTION_DOWN) {
            drawing.moveTo(x, y);
        }
        else if (action == MotionEvent.ACTION_MOVE) {
            drawing.lineTo(x, y);
        }

        // Track path coordinates
        pathX.add(x);
        pathY.add(y);
        // Redraw path
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
        // Stop the balls
        shaken = false;
        balls.clear();
        pathX.clear();
        pathY.clear();

        // Redraw canvas so they don't appear
        invalidate();
    }

    public void shake() {
        shaken = true;
        // add balls at every point in the path
        for (int i = 0; i < pathX.size(); i++) {
            balls.add(new Ball(pathX.get(i), pathY.get(i)));
        }
        // redraw these balls
        invalidate();
    }
}
